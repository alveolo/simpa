package org.alveolo.simpa.jdbc;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import org.alveolo.simpa.Column;
import org.alveolo.simpa.PersistenceException;
import org.alveolo.simpa.SequenceGenerator;
import org.alveolo.simpa.Table;


public class DefaultNaming {
	private final String defaultCatalog;
	private final String defaultSchema;

	public DefaultNaming(String defaultCatalog, String defaultSchema) {
		this.defaultCatalog = defaultCatalog;
		this.defaultSchema = defaultSchema;
	}

	public DefaultNaming() {
		this(null, null);
	}

	public String getCatalog(Class<?> type) {
		return getCatalog(type, type.getAnnotation(Table.class));
	}

	protected String getCatalog(Class<?> type, Table table) {
		if (table != null) {
			String catalog = table.catalog();
			if (catalog.length() > 0) {
				return catalog;
			}
		}

		return defaultCatalog;
	}

	public String getSchema(Class<?> type) {
		return getSchema(type, type.getAnnotation(Table.class));
	}

	protected String getSchema(Class<?> type, Table table) {
		if (table != null) {
			String schema = table.schema();
			if (schema.length() > 0) {
				return schema;
			}
		}

		return defaultSchema;
	}

	public String getTable(Class<?> type) {
		return getTable(type, type.getAnnotation(Table.class));
	}

	protected String getTable(Class<?> type, Table table) {
		if (table != null) {
			String name = table.name();
			if (name.length() > 0) {
				return name;
			}
		}

		return toTableName(type);
	}

	public String getQualifiedSequenceName(SequenceGenerator annotation) {
		String catalog = annotation.catalog();
		if (catalog.length() == 0) {
			catalog = defaultCatalog;
		}

		String schema = annotation.schema();
		if (schema.length() == 0) {
			schema = defaultSchema;
		}

		String name = annotation.sequenceName();
		if (name.length() == 0) {
			name = "simpa_sequence";
		}

		if (catalog == null && schema == null) {
			return name;
		}

		StringBuilder buf = new StringBuilder();

		if (catalog != null) {
			buf.append(catalog).append('.');
		}

		if (schema != null) {
			buf.append(schema).append('.');
		}

		return buf.append(name).toString();
	}

	public String getQualifiedTableName(Class<?> type) {
		Table table = type.getAnnotation(Table.class);

		String catalog = getCatalog(type, table);
		String schema = getSchema(type, table);
		String name = getTable(type, table);

		if (catalog == null && schema == null) {
			return name;
		}

		StringBuilder buf = new StringBuilder();

		if (catalog != null) {
			buf.append(catalog).append('.');
		}

		if (schema != null) {
			buf.append(schema).append('.');
		}

		return buf.append(name).toString();
	}

	public String getColumnName(Member member) {
		Column column = ((AccessibleObject) member).getAnnotation(Column.class);

		if (column != null) {
			String name = column.name();
			if (name.length() > 0) {
				return name;
			}
		}

		if (member instanceof Field) {
			return NamingUtil.camelCaseToUnderscore(member.getName());
		}

		if (member instanceof Method) {
			return NamingUtil.camelCaseToUnderscore(member.getName().substring(3));
		}

		throw new PersistenceException("Only field or setter method are expected, but found " + member);
	}

	protected String toTableName(Class<?> type) {
		return NamingUtil.camelCaseToUnderscore(type.getSimpleName());
	}
}
