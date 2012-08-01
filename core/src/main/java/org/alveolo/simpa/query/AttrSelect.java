package org.alveolo.simpa.query;

import org.alveolo.simpa.metamodel.Attribute;


public class AttrSelect implements Select {
	public final Attribute<?, ?> attribute;

	public AttrSelect(Attribute<?, ?> attribute) {
		this.attribute = attribute;
	}

	@Override
	public void accept(SelectVisitor visitor) {
		visitor.visit(this);
	}
}
