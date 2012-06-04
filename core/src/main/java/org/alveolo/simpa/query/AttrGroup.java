package org.alveolo.simpa.query;

import javax.persistence.metamodel.Attribute;


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
