package org.alveolo.simpa.query;


public class RawSelect implements Select {
	public final Class<?> javaType;
	public final String sql;

	public RawSelect(Class<?> javaType, String sql) {
		this.javaType = javaType;
		this.sql = sql;
	}

	@Override
	public void accept(SelectVisitor visitor) {
		visitor.visit(this);
	}
}
