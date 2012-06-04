package org.alveolo.simpa.query;

import javax.persistence.metamodel.Attribute;


public class AttrCondition implements Condition {
	public String op;
	public Attribute<?, ?> attribute;
	public Object value;

	public AttrCondition(String op, Attribute<?, ?> attribute, Object value) {
		if (attribute == null) {
			throw new NullPointerException("attribute");
		}

		this.op = op;
		this.attribute = attribute;
		this.value = value;
	}

	@Override
	public void accept(ConditionVisitor visitor) {
		visitor.visit(this);
	}
}
