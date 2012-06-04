package org.alveolo.simpa.jdbc;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.persistence.metamodel.Attribute;

import org.alveolo.simpa.metamodel.EntityTypeImpl;


public class RawMapper<T> {
	private static final DefaultNaming naming = new DefaultNaming();

	private final List<Attribute<? super T, ?>> attributes;

	public RawMapper(ResultSetMetaData meta, Class<T> javaType) throws SQLException {
		EntityTypeImpl<T> type = new EntityTypeImpl<>(null, javaType, null);

		Map<String, Attribute<? super T, ?>> columns = new HashMap<>();
		for (Attribute<? super T, ?> attr : type.getAttributes()) {
			columns.put(naming.getColumnName(attr.getJavaMember()), attr);
		}

		int length = meta.getColumnCount();

		attributes = new ArrayList<>(length);

		for (int i = 0; i < length; i++) {
			String name = meta.getColumnLabel(i+1);
			Attribute<? super T, ?> attr = columns.get(name);

			if (attr == null) {
				throw new PersistenceException("No mapping defined for column: " + name);
			}

			Field field = (Field) attr.getJavaMember();
			field.setAccessible(true);

			attributes.add(attr);
		}
	}

	public void setEntityValues(ResultSet rset, T entity) throws SQLException, ReflectiveOperationException {
		for (int i = 0; i < attributes.size(); i++) {
			Attribute<? super T, ?> attr = attributes.get(i);
			Field field = (Field) attr.getJavaMember();
			field.set(entity, JdbcUtil.getValue(rset, i+1, JdbcUtil.getJdbcType(attr)));
		}
	}
}
