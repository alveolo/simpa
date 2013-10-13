package org.alveolo.simpa.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.alveolo.simpa.PersistenceException;
import org.alveolo.simpa.metamodel.Attribute;
import org.alveolo.simpa.metamodel.Attribute.PersistentAttributeType;
import org.alveolo.simpa.metamodel.ManagedType;
import org.alveolo.simpa.metamodel.SingularAttribute;
import org.alveolo.simpa.query.AttrSelect;
import org.alveolo.simpa.query.RawSelect;
import org.alveolo.simpa.query.SelectVisitor;


public class JdbcRowMapperVisitor<T> implements SelectVisitor {
	protected final ResultSet rset;
	protected final Class<T> javaType;

	protected Object object;
	protected int index;

	public JdbcRowMapperVisitor(ResultSet rset, Class<T> javaType) {
		this.rset = rset;
		this.javaType = javaType;
	}

	@SuppressWarnings("unchecked")
	public T getEntity() {
		return (T) object;
	}

	public void reset() {
		object = null;
		index = 0;
	}

	@Override
	public void visit(AttrSelect select) {
		if (index == 0) {
			object = newInstance(javaType);
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

		throw new PersistenceException("Not implemented!");
	}

	// TODO: Cache constructors (consider embedded objects)
	protected static <T> T newInstance(Class<T> type) {
		try {
			Constructor<T> constructor = type.getDeclaredConstructor();
			constructor.setAccessible(true);
			T entity = constructor.newInstance();
			return entity;
		} catch (ReflectiveOperationException e) {
			throw new PersistenceException(e);
		}
	}

	/**
	 * Must only be called exactly after all visits have passed.
	 */ 
	public int getCountOver() {
		try {
			return rset.getInt(++index);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
}
