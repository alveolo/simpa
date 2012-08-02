package org.alveolo.simpa.util;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.List;

import org.alveolo.simpa.PersistenceException;
import org.alveolo.simpa.metamodel.Attribute;
import org.alveolo.simpa.metamodel.Attribute.PersistentAttributeType;
import org.alveolo.simpa.metamodel.EntityType;
import org.alveolo.simpa.metamodel.ManagedType;
import org.alveolo.simpa.metamodel.SingularAttribute;
import org.alveolo.simpa.query.AttrCondition;
import org.alveolo.simpa.query.Condition;
import org.alveolo.simpa.query.Conjunction;


public class EntityUtil {
	public static Object getId(EntityType<?> meta, Object entity) {
		try {
			SingularAttribute<?, ?> id = meta.getId(meta.getIdType().getJavaType());

			Member member = id.getJavaMember();
			if (member instanceof Field) {
				Field field = (Field) member;
				return field.get(entity);
			}

			Method method = (Method) member;
			return  method.invoke(entity);
		} catch (ReflectiveOperationException e) {
			throw new PersistenceException(e);
		}
	}

	public static Object getValue(Attribute<?, ?> attribute, Object entity) {
		try {
			Member member = attribute.getJavaMember();
			if (member instanceof Field) {
				Field field = (Field) member;
				return field.get(entity);
			}

			Method method = (Method) member;
			return  method.invoke(entity);
		} catch (ReflectiveOperationException e) {
			throw new PersistenceException(e);
		}
	}

	public static Conjunction conditionsForId(EntityType<?> meta, Object identity) {
		Conjunction conjunction = new Conjunction();

		// TODO: Plural attribute and IdClass
		SingularAttribute<?, ?> idAttribute = meta.getId(meta.getIdType().getJavaType());

		addConditions(conjunction, idAttribute, identity);

		return conjunction;
	}

	public static Conjunction conditionsForEntity(EntityType<?> meta, Object entity) {
		Conjunction conjunction = new Conjunction();

		// TODO: Plural attribute and IdClass
		SingularAttribute<?, ?> idAttribute = meta.getId(meta.getIdType().getJavaType());

		addConditions(conjunction, idAttribute, EntityUtil.getValue(idAttribute, entity));

		return conjunction;
	}

	private static void addConditions(Conjunction conjunction, SingularAttribute<?, ?> attribute, Object value) {
		List<Condition> conditions = conjunction.conditions;

		if (attribute.getPersistentAttributeType() == PersistentAttributeType.EMBEDDED) {
			@SuppressWarnings("unchecked")
			ManagedType<Object> type = (ManagedType<Object>) attribute.getType();
			for (Attribute<?, ?> a : type.getAttributes()) {
				conditions.add(new AttrCondition("=", a, EntityUtil.getValue(a, value)));
			}
		} else {
			conditions.add(new AttrCondition("=", attribute, value));
		}
	}
}
