package org.alveolo.simpa.query;

import java.util.Collection;

import javax.persistence.metamodel.Attribute;


public class InCondition implements Condition {
	public Attribute<?, ?> attribute;
	public Collection<?> values;

	public InCondition(Attribute<?, ?> attribute, Collection<?> values) {
		if (attribute == null) {
			throw new NullPointerException("attribute");
		}

		if (values == null || values.size() == 0) {
			throw new IllegalArgumentException("values");
		}

		this.attribute = attribute;
		this.values = values;
	}

	@Override
	public void accept(ConditionVisitor visitor) {
		visitor.visit(this);
	}
}
