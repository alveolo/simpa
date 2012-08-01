package org.alveolo.simpa.query;

import org.alveolo.simpa.metamodel.Attribute;


public class AttrOrder implements Order {
	public final Attribute<?, ?> attribute;
	public final boolean reverse;

	public AttrOrder(Attribute<?, ?> attribute, boolean reverse) {
		this.attribute = attribute;
		this.reverse = reverse;
	}

	@Override
	public void accept(OrderVisitor visitor) {
		visitor.visit(this);
	}
}
