package org.alveolo.simpa.query;

import java.util.List;

import org.alveolo.simpa.jdbc.QueryCallbacks;


public class HavingBuilder<T> extends ConditionBuilder<HavingBuilder<T>, T> {
	public HavingBuilder(QueryCallbacks callbacks, Query<T> query) {
		super(callbacks, query);
	}

	@Override
	protected List<Condition> getConditions() {
		return query.having.conditions;
	}
}
