package org.alveolo.simpa.query;


public class Raw implements Condition, Group, Order {
	public final String sql;
	public final Object[] values;

	public Raw(String sql, Object[] values) {
		if (sql == null) {
			throw new NullPointerException("sql");
		}

		this.sql = sql;
		this.values = values;
	}

	@Override
	public void accept(ConditionVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void accept(GroupVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void accept(OrderVisitor visitor) {
		visitor.visit(this);
	}
}
