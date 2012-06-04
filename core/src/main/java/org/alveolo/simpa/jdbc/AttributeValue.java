package org.alveolo.simpa.jdbc;

import javax.persistence.metamodel.Attribute;


public class AttributeValue {
	public final Attribute<?, ?> attribute;
	public final Object value;

	public AttributeValue(Attribute<?, ?> attribute, Object value) {
		this.attribute = attribute;
		this.value = value;
	}
}
