package org.alveolo.simpa.query;

import java.util.List;

import org.alveolo.simpa.jdbc.QueryCallbacks;


public class WhereBuilder<T> extends ConditionBuilder<WhereBuilder<T>, T> {
	public WhereBuilder(QueryCallbacks callbacks, Class<T> type) {
		super(callbacks, new Query<>(type));
	}

	@Override
	protected List<Condition> getConditions() {
		return query.where.conditions;
	}

	public GroupBuilder<T> group() {
		return new GroupBuilder<>(callbacks, query);
	}

	public int delete() {
		return callbacks.delete(query);
	}
}
