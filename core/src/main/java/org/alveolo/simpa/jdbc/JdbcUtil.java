package org.alveolo.simpa.jdbc;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Array;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alveolo.simpa.EnumType;
import org.alveolo.simpa.Enumerated;
import org.alveolo.simpa.PersistenceException;
import org.alveolo.simpa.Temporal;
import org.alveolo.simpa.metamodel.Attribute;
import org.alveolo.simpa.util.GenericTypeUtils;


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
				stmt.setBoolean(index, (boolean) jdbcValue);
			}
			return;
		}

		if (jdbcType == Double.TYPE || jdbcType.isAssignableFrom(Double.class)) {
			if (value == null) {
				stmt.setNull(index, Types.DOUBLE);
			} else {
				stmt.setDouble(index, (double) jdbcValue);
			}
			return;
		}

		if (jdbcType == Float.TYPE || jdbcType.isAssignableFrom(Float.class)) {
			if (value == null) {
				stmt.setNull(index, Types.FLOAT);
			} else {
				stmt.setFloat(index, (float) jdbcValue);
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

		if (jdbcType.isAssignableFrom(Array.class)) {
			Array array = stmt.getConnection().createArrayOf(
					getPgType(attribute.getGenericType()), (Object[]) jdbcValue);
			if (value == null) {
				stmt.setNull(index, Types.ARRAY);
			} else {
				stmt.setArray(index, array);
			}
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

		if (jdbcType.isAssignableFrom(Array.class)) {
			Array array = rset.getArray(index);
			if (array != null) {
				try {
					return array.getArray();
				} finally {
					array.free();
				}
			}
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

		org.alveolo.simpa.Array array = accessible.getAnnotation(org.alveolo.simpa.Array.class);
		if (array != null) {
			return Array.class;
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

	// TODO: Extract PostgreSQL specific stuff
	public static String getPgType(Type type) {
		Class<?> c = getComponentType(type);

		if (String.class.isAssignableFrom(c)) {
			return "varchar";
		}

		if (c == Long.TYPE || Long.class.isAssignableFrom(c)) {
			return "bigint";
		}

		if (c == Integer.TYPE || Integer.class.isAssignableFrom(c)) {
			return "integer";
		}

		if (c == Short.TYPE || Short.class.isAssignableFrom(c)) {
			return "smallint";
		}

		if (c == Byte.TYPE || Byte.class.isAssignableFrom(c)) {
			return "smallint"; // there is no single-byte type
		}

		if (c == Boolean.TYPE || Boolean.class.isAssignableFrom(c)) {
			return "boolean";
		}

		if (c == Double.TYPE || Double.class.isAssignableFrom(c)) {
			return "double precision";
		}

		if (c == Float.TYPE || Float.class.isAssignableFrom(c)) {
			return "real";
		}

		throw new PersistenceException("Unknown database type for: " + type);
	}

	public static Class<?> getComponentType(Type type) {
		// Multidimensional array
		if (type instanceof GenericArrayType) {
			GenericArrayType at = (GenericArrayType) type;
			return getComponentType(at.getGenericComponentType());
		}

		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;

			// Multidimensional collection
			if (Collection.class.isAssignableFrom((Class<?>) pt.getRawType())) {
				return getComponentType(GenericTypeUtils.getGenericTypeArgument(Collection.class, type, 0));
			}
		}

		if (type instanceof Class) {
			Class<?> c = (Class<?>) type;
			return (c.isArray()) ? getComponentType(c.getComponentType()) : c;
		}

		throw new PersistenceException("Unknown database type for: " + type);
	}

	private static final Map<Class<?>, Class<?>> primitiveToWrapper = new HashMap<Class<?>, Class<?>>();
	private static final Map<Class<?>, Class<?>> wrapperToPrimitive = new HashMap<Class<?>, Class<?>>();
	private static void addPrimitiveWrapper(Class<?> wrapper) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Class<?> primitive = (Class<?>) wrapper.getField("TYPE").get(wrapper);
		primitiveToWrapper.put(primitive, wrapper);
		wrapperToPrimitive.put(wrapper, primitive);
	}
	static {
		try {
			addPrimitiveWrapper(Long.class);
			addPrimitiveWrapper(Integer.class);
			addPrimitiveWrapper(Short.class);
			addPrimitiveWrapper(Byte.class);
			addPrimitiveWrapper(Boolean.class);
			addPrimitiveWrapper(Double.class);
			addPrimitiveWrapper(Float.class);
			addPrimitiveWrapper(Character.class);
		} catch (ReflectiveOperationException e) {
			throw new Error(e);
		}
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

			org.alveolo.simpa.Array array = accessible.getAnnotation(org.alveolo.simpa.Array.class);
			if (array != null) {
				// Normalize to Object[] expected by JDBC
				Class<?> componentType = getComponentType(attribute.getGenericType());
				Class<?> wrapper = primitiveToWrapper.get(componentType);
				return convertToArrayType(value, (wrapper != null) ? wrapper : componentType);
			}
		}

		return value;
	}

	// TODO: Multidimensional arrays
	public static Object convertToArrayType(Object value, Class<?> toComponentType) {
		if (value instanceof Collection) {
			if (toComponentType.isPrimitive()) {
				// Must iterate on primitive conversion
				Iterator<?> iter = ((Collection<?>) value).iterator();
				int length = java.lang.reflect.Array.getLength(value);
				Object converted = java.lang.reflect.Array.newInstance(toComponentType, length);
				for (int i = 0; i < length; i++) {
					java.lang.reflect.Array.set(converted, i, iter.next());
				}
			} else {
				// Target is object array - fast copy
				Object[] a = (Object[]) java.lang.reflect.Array.newInstance(
						toComponentType, ((Collection<?>) value).size());
				a = ((Collection<?>) value).toArray(a);
				return a;
			}
		}
		
		Class<?> fromComponentType = value.getClass().getComponentType();

		if (fromComponentType == toComponentType) {
			return value;
		}

		int length = java.lang.reflect.Array.getLength(value);
		Object converted = java.lang.reflect.Array.newInstance(toComponentType, length);

		if (toComponentType.isPrimitive() || fromComponentType.isPrimitive()) {
			// Must iterate on primitive conversion
			for (int i = 0; i < length; i++) {
				java.lang.reflect.Array.set(converted, i, java.lang.reflect.Array.get(value, i));
			}
		} else {
			// Both are object arrays - fast copy
			System.arraycopy(value, 0, converted, 0, length);
		}

		return converted;
	}
	
	/**
	 * Converts a value from database format to object format and assigns it to entity.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setPersistenceValue(Field field, Object entity, Object value) {
		try {
			Class<?> javaType = field.getType();

			if (value != null) {
				if (javaType.isEnum()) {
					Enumerated enumerated = field.getAnnotation(Enumerated.class);

					if (enumerated != null && enumerated.value() == EnumType.STRING) {
						value = Enum.valueOf((Class<Enum>) javaType, (String) value);
					} else {
						value = javaType.getEnumConstants()[(int) value];
					}
				} else if (javaType.isArray()) {
					value = convertToArrayType(value, javaType.getComponentType());
				} else if (Collection.class.isAssignableFrom(javaType)) {
					if (javaType.isInterface()) {
						if (javaType.isAssignableFrom(List.class)) {
							value = Arrays.asList((Object[]) value);
						} else if (javaType.isAssignableFrom(Set.class)) {
							value = new LinkedHashSet(Arrays.asList((Object[]) value));
						} else {
							// TODO: Enum sets, multidimentional sets.
							throw new PersistenceException("Unsupported collection type: " + javaType);
						}
					} else {
						Collection c = (Collection) javaType.newInstance();
						Collections.addAll(c, (Object[]) value);
						value = c;
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
