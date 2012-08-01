package org.alveolo.simpa.query;

import org.alveolo.simpa.metamodel.Attribute;


public class IsNotNullCondition implements Condition {
	public Attribute<?, ?> attribute;

	public IsNotNullCondition(Attribute<?, ?> attribute) {
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
