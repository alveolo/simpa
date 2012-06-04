package org.alveolo.simpa.query;

import org.alveolo.simpa.jdbc.QueryCallbacks;


public abstract class ConditionBuilder<B extends ConditionBuilder<B, T>, T> extends BaseJunctionBuilder<B, T> {
	public ConditionBuilder(QueryCallbacks callbacks, Query<T> query) {
		super(callbacks, query);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JunctionBuilder<B, T> disjunction() {
		Disjunction disjunction = new Disjunction();
		getConditions().add(disjunction);
		return new JunctionBuilder(callbacks, this, disjunction.conditions);
	}

	public OrderBuilder<T> order() {
		return new OrderBuilder<>(callbacks, query);
	}
}
