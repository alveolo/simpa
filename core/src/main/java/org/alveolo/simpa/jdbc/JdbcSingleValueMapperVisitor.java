package org.alveolo.simpa.jdbc;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.PersistenceException;

import org.alveolo.simpa.metamodel.Attribute;
import org.alveolo.simpa.metamodel.Attribute.PersistentAttributeType;
import org.alveolo.simpa.metamodel.ManagedType;
import org.alveolo.simpa.metamodel.SingularAttribute;
import org.alveolo.simpa.query.AttrSelect;
import org.alveolo.simpa.query.RawSelect;


public class JdbcSingleValueMapperVisitor<T> extends JdbcRowMapperVisitor<T> {
	private static final Set<Class<?>> simpleTypes = new HashSet<>();
	static {
		simpleTypes.add(Long.class);
		simpleTypes.add(Long.TYPE);
		simpleTypes.add(Integer.class);
		simpleTypes.add(Integer.TYPE);
		simpleTypes.add(Short.class);
		simpleTypes.add(Short.TYPE);
		simpleTypes.add(Byte.class);
		simpleTypes.add(Byte.TYPE);
		simpleTypes.add(Boolean.class);
		simpleTypes.add(Boolean.TYPE);
		simpleTypes.add(String.class);
		simpleTypes.add(UUID.class);
	}

	public JdbcSingleValueMapperVisitor(ResultSet rset, Class<T> javaType) {
		super(rset, javaType);
	}

	@Override
	public void visit(AttrSelect select) {
		if (index != 0) {
			throw new IllegalStateException();
		}

		setEntityField(rset, select.attribute);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setEntityField(ResultSet rset, Attribute attribute) {
		try {
			Object value;

			if (attribute.getPersistentAttributeType() == PersistentAttributeType.EMBEDDED) {
				SingularAttribute singular = (SingularAttribute) attribute;
				ManagedType<Object> type = (ManagedType<Object>) singular.getType();

				Object parent = object;
				value = object = newInstance(type.getJavaType());

				for (Attribute a : type.getAttributes()) {
					setEntityField(rset, a);
				}

				object = parent;
			} else {
				value = JdbcUtil.getValue(rset, ++index, attribute);
			}

			if (object == null) {
				object = value;
				return;
			}

			Field field = (Field) attribute.getJavaMember(); // TODO: method
			JdbcUtil.setPersistenceValue(field, object, value);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void visit(RawSelect raw) {
		if (index != 0) {
			throw new IllegalStateException();
		}

		if (simpleTypes.contains(raw.javaType)) {
			try {
				object = JdbcUtil.getValue(rset, ++index, raw.javaType);
			} catch (SQLException e) {
				throw new PersistenceException(e);
			}
		} else {
			// composite object with field mapping
			throw new PersistenceException("Not implemented!");
		}
	}
}
