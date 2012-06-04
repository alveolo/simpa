package org.alveolo.simpa.query;

import java.util.List;

import org.alveolo.simpa.jdbc.QueryCallbacks;


public class JunctionBuilder<B extends ConditionBuilder<B,T>, T>
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

	public JunctionBuilder<B, T> conjunction() {
		return builder(new Conjunction());
	}

	public JunctionBuilder<B, T> disjunction() {
		return builder(new Disjunction());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JunctionBuilder<B, T> builder(Junction junction) {
		getConditions().add(junction);
		return new JunctionBuilder(callbacks, builder, junction.conditions);
	}

	public B end() {
		return builder;
	}
}
