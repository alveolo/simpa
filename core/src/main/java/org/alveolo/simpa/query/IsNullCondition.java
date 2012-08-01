package org.alveolo.simpa.query;

import org.alveolo.simpa.metamodel.Attribute;


public class IsNullCondition implements Condition {
	public Attribute<?, ?> attribute;

	public IsNullCondition(Attribute<?, ?> attribute) {
		if (attribute == null) {
			throw new NullPointerException("attribute");
		}

		this.attribute = attribute;
	}

	@Override
	public void accept(ConditionVisitor visitor) {
		visitor.visit(this);
	}
}
