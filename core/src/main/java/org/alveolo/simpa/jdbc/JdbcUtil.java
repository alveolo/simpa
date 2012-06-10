package org.alveolo.simpa.jdbc;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PersistenceException;
import javax.persistence.Temporal;
import javax.persistence.metamodel.Attribute;


public class JdbcUtil {
	public static void setParameter(PreparedStatement stmt, int index, Attribute<?, ?> attribute, Object value)
	throws SQLException {
		Class<?> jdbcType = (attribute == null) ? value.getClass() : getJdbcType(attribute);
		Object jdbcValue = (attribute == null) ? value : getJdbcValue(attribute, value);

		if (jdbcType.isAssignableFrom(String.class)) {
			if (value == null) {
				stmt.setNull(index, Types.VARCHAR);
			} else {
				stmt.setString(index, (String) jdbcValue);
			}
			return;
		}

		if (jdbcType == Long.TYPE || jdbcType.isAssignableFrom(Long.class)) {
			if (value == null) {
				stmt.setNull(index, Types.BIGINT);
			} else {
				stmt.setLong(index, (long) jdbcValue);
			}
			return;
		}

		if (jdbcType == Integer.TYPE || jdbcType.isAssignableFrom(Integer.class)) {
			if (value == null) {
				stmt.setNull(index, Types.INTEGER);
			} else {
				stmt.setInt(index, (int) jdbcValue);
			}
			return;
		}

		if (jdbcType == Short.TYPE || jdbcType.isAssignableFrom(Short.class)) {
			if (value == null) {
				stmt.setNull(index, Types.SMALLINT);
			} else {
				stmt.setShort(index, (short) jdbcValue);
			}
			return;
		}

		if (jdbcType == Byte.TYPE || jdbcType.isAssignableFrom(Byte.class)) {
			if (value == null) {
				stmt.setNull(index, Types.TINYINT);
			} else {
				stmt.setByte(index, (byte) jdbcValue);
			}
			return;
		}

		if (jdbcType == Boolean.TYPE || jdbcType.isAssignableFrom(Boolean.class)) {
			if (value == null) {
				stmt.setNull(index, Types.BOOLEAN);
			} else {
				stmt.setBoolean(index, (Boolean) jdbcValue);
			}
			return;
		}

		if (jdbcType.isAssignableFrom(Timestamp.class)) {
			stmt.setTimestamp(index, (Timestamp) jdbcValue);
			return;
		}

		if (jdbcType.isAssignableFrom(Date.class)) {
			stmt.setDate(index, (Date) jdbcValue);
			return;
		}

		if (jdbcType.isAssignableFrom(Time.class)) {
			stmt.setTime(index, (Time) jdbcValue);
			return;
		}

		stmt.setObject(index, jdbcValue);
	}

	public static Object getValue(ResultSet rset, int index, Attribute<?, ?> attribute) throws SQLException {
		return getValue(rset, index, getJdbcType(attribute));
	}

	public static Object getValue(ResultSet rset, int index, Class<?> jdbcType) throws SQLException {
		if (jdbcType.isAssignableFrom(String.class)) {
			return rset.getString(index);
		}

		if (jdbcType.isAssignableFrom(Timestamp.class)) {
			return rset.getTimestamp(index);
		}

		if (jdbcType.isAssignableFrom(Date.class)) {
			return rset.getDate(index);
		}

		if (jdbcType.isAssignableFrom(Time.class)) {
			return rset.getTime(index);
		}

		return rset.getObject(index);
	}

	public static Class<?> getJdbcType(Attribute<?, ?> attribute) {
		AccessibleObject accessible = (AccessibleObject) attribute.getJavaMember();
		Class<?> javaType = attribute.getJavaType();

		if (javaType.isEnum()) {
			Enumerated enumerated = accessible.getAnnotation(Enumerated.class);
			if (enumerated != null && enumerated.value() == EnumType.STRING) {
				return String.class;
			}

			return Integer.TYPE;
		}

		Temporal temporal = accessible.getAnnotation(Temporal.class);
		if (temporal != null) {
			switch (temporal.value()) {
				case DATE: return Date.class;
				case TIME: return Time.class;
				default: return Timestamp.class;
			}
		}

		return javaType;
	}

	/**
	 * Extracts a value from entity ready and converts it to ready to be stored to database format.
	 */
	public static Object getJdbcValue(Attribute<?, ?> attribute, Object value) {
		AccessibleObject accessible = (AccessibleObject) attribute.getJavaMember();

		if (value != null) {
			if (value instanceof Enum) {
				Enumerated enumerated = accessible.getAnnotation(Enumerated.class);
				if (enumerated != null && enumerated.value() == EnumType.STRING) {
					return ((Enum<?>) value).name();
				}

				return ((Enum<?>) value).ordinal();
			}

			Temporal temporal = accessible.getAnnotation(Temporal.class);
			if (temporal != null) {
				long millis;

				if (value instanceof Calendar) {
					millis = ((Calendar) value).getTimeInMillis();
				} else {
					millis = ((java.util.Date) value).getTime();
				}

				switch (temporal.value()) {
					case DATE: return new Date(millis);
					case TIME: return new Time(millis);
					default: return new Timestamp(millis);
				}
			}
		}

		return value;
	}

	/**
	 * Converts a value from database format to object format and assigns it to entity.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setPersistenceValue(Field field, Object entity, Object value) {
		try {
			Class<Enum> javaType = (Class<Enum>) field.getType();

			if (value != null) {
				if (javaType.isEnum()) {
					Enumerated enumerated = field.getAnnotation(Enumerated.class);

					if (enumerated != null && enumerated.value() == EnumType.STRING) {
						value = Enum.valueOf(javaType, (String) value);
					} else {
						value = javaType.getEnumConstants()[(Integer) value];
					}
				} else {
					Temporal temporal = field.getAnnotation(Temporal.class);
					if (temporal != null) {
						if (javaType.equals(java.util.Date.class)) {
							value = new java.util.Date(((java.util.Date) value).getTime());
						}
					}
				}
			}

			field.set(entity, value);
		} catch (ReflectiveOperationException e) {
			throw new PersistenceException(e);
		}
	}
}
