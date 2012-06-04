package org.alveolo.simpa.metamodel;

import javax.persistence.metamodel.BasicType;


public class BasicTypeImpl<X> extends TypeImpl<X> implements BasicType<X> {
	public BasicTypeImpl(Class<X> javaType) {
		super(javaType);
	}

	@Override
	public PersistenceType getPersistenceType() {
		return PersistenceType.BASIC;
	}
}
