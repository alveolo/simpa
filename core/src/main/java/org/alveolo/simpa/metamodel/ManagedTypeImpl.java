package org.alveolo.simpa.metamodel;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.PersistenceException;
import javax.persistence.Transient;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;


public abstract class ManagedTypeImpl<X> extends TypeImpl<X> implements ManagedType<X> {
	private final MetamodelImpl metamodel;

	private final Map<String, Attribute<? super X, ?>> attributeMap = new LinkedHashMap<>();
	private final Set<Attribute<? super X, ?>> attributes;
	private final Set<SingularAttribute<? super X, ?>> singularAttributes;

	public ManagedTypeImpl(MetamodelImpl metamodel, Class<X> javaType) {
		super(javaType);

		this.metamodel = metamodel;

		try {
			javaType.getDeclaredConstructor(); // Ensure there is a default constructor!
		} catch (NoSuchMethodException e) {
			throw new PersistenceException("There is no default constructor in: " + javaType);
		}

		Set<Attribute<? super X, ?>> attributes = new LinkedHashSet<>();
		Set<SingularAttribute<? super X, ?>> singularAttributes = new LinkedHashSet<>();

		for (Field field : javaType.getDeclaredFields()) {
			if (isSyntheticStaticOrTransient(field)) {
				continue;
			}

			SingularAttributeImpl<X, Object> attribute = new SingularAttributeImpl<>(this, field);

			attributeMap.put(field.getName(), attribute);
			attributes.add(attribute);
			singularAttributes.add(attribute);
		}

		// TODO: Support for annotated getters and property access type, analyze superclass

		this.attributes = Collections.unmodifiableSet(attributes);
		this.singularAttributes = Collections.unmodifiableSet(singularAttributes);
	}

	public MetamodelImpl getMetamodel() {
		return metamodel;
	}

	@Override
	public Set<Attribute<? super X, ?>> getAttributes() {
		return attributes;
	}

	@Override
	public Set<Attribute<X, ?>> getDeclaredAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Y> SingularAttribute<? super X, Y> getSingularAttribute(String name, Class<Y> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Y> SingularAttribute<X, Y> getDeclaredSingularAttribute(String name, Class<Y> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<SingularAttribute<? super X, ?>> getSingularAttributes() {
		return singularAttributes;
	}

	@Override
	public Set<SingularAttribute<X, ?>> getDeclaredSingularAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E> CollectionAttribute<? super X, E> getCollection(String name, Class<E> elementType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E> CollectionAttribute<X, E> getDeclaredCollection(String name, Class<E> elementType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E> SetAttribute<? super X, E> getSet(String name, Class<E> elementType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E> SetAttribute<X, E> getDeclaredSet(String name, Class<E> elementType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E> ListAttribute<? super X, E> getList(String name, Class<E> elementType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E> ListAttribute<X, E> getDeclaredList(String name, Class<E> elementType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <K, V> MapAttribute<? super X, K, V> getMap(String name, Class<K> keyType, Class<V> valueType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <K, V> MapAttribute<X, K, V> getDeclaredMap(String name, Class<K> keyType, Class<V> valueType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<PluralAttribute<? super X, ?, ?>> getPluralAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<PluralAttribute<X, ?, ?>> getDeclaredPluralAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute<? super X, ?> getAttribute(String name) {
		Attribute<? super X, ?> attribute = attributeMap.get(name);
		if (attribute == null) {
			throw new IllegalArgumentException("Attribute is missing: " + name);
		}

		return attribute;
	}

	@Override
	public Attribute<X, ?> getDeclaredAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SingularAttribute<? super X, ?> getSingularAttribute(String name) {
		Attribute<? super X, ?> attribute = attributeMap.get(name);
		if (attribute == null || !(attribute instanceof SingularAttribute)) {
			throw new IllegalArgumentException("Attribute is missing: " + name);
		}

		return (SingularAttribute<? super X, ?>) attribute;
	}

	@Override
	public SingularAttribute<X, ?> getDeclaredSingularAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CollectionAttribute<? super X, ?> getCollection(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CollectionAttribute<X, ?> getDeclaredCollection(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SetAttribute<? super X, ?> getSet(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SetAttribute<X, ?> getDeclaredSet(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListAttribute<? super X, ?> getList(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListAttribute<X, ?> getDeclaredList(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MapAttribute<? super X, ?, ?> getMap(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MapAttribute<X, ?, ?> getDeclaredMap(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean isSyntheticStaticOrTransient(Member member) {
		return member.isSynthetic()
			|| (member.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT)) != 0
			|| ((AccessibleObject) member).getAnnotation(Transient.class) != null;
	}
}
