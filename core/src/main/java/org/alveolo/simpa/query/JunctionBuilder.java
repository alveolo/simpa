package org.alveolo.simpa.query;

import java.util.List;

import org.alveolo.simpa.jdbc.QueryCallbacks;


public class JunctionBuilder<B extends BaseJunctionBuilder<B,T>, T>
extends BaseJunctionBuilder<JunctionBuilder<B, T>, T> {
	private final B builder;
	private final List<Condition> conditions;

	public JunctionBuilder(QueryCallbacks callbacks, B builder, List<Condition> conditions) {
		super(callbacks, builder.query);
		this.builder = builder;
		this.conditions = conditions;
	}

	@Override
	protected List<Condition> getConditions() {
		return conditions;
	}

	@SuppressWarnings("unchecked")
	public JunctionBuilder<B, T> conjunction() {
		return builder(builder, new Conjunction());
	}

	@SuppressWarnings("unchecked")
	public JunctionBuilder<B, T> disjunction() {
		return builder(builder, new Disjunction());
	}

	public B end() {
		return builder;
	}
}
