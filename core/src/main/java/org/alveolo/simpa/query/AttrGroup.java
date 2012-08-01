package org.alveolo.simpa.query;

import org.alveolo.simpa.metamodel.Attribute;


public class AttrGroup implements Group {
	public final Attribute<?, ?> attribute;

	public AttrGroup(Attribute<?, ?> attribute) {
		this.attribute = attribute;
	}

	@Override
	public void accept(GroupVisitor visitor) {
		visitor.visit(this);
	}
}
