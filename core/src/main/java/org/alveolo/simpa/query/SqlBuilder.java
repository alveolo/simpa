package org.alveolo.simpa.query;

import java.util.ArrayList;
import java.util.List;

import org.alveolo.simpa.jdbc.RawCallbacks;


/**
 * Builder for creating and executing pure raw SQL SELECT query and mapping the result by the constructor.
 */
public class SqlBuilder {
	protected final RawCallbacks callbacks;
	protected final List<Raw> fragments = new ArrayList<>();

	public SqlBuilder(RawCallbacks callbacks) {
		this.callbacks = callbacks;
	}

	public SqlBuilder sql(String sql, Object... values) {
		fragments.add(new Raw(sql, values));
		return this;
	}

	public <T> List<T> list(Class<T> javaType) {
		return callbacks.list(javaType, fragments);
	}

	public <T> T find(Class<T> javaType) {
		return callbacks.find(javaType, fragments);
	}
}
