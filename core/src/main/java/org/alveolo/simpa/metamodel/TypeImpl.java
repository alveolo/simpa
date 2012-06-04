package org.alveolo.simpa.metamodel;

import javax.persistence.metamodel.Type;


public abstract class TypeImpl<X> implements Type<X> {
	protected final Class<X> javaType;

	public TypeImpl(Class<X> javaType) {
		this.javaType = javaType;
	}

	@Override
	public Class<X> getJavaType() {
		return javaType;
	}
}
