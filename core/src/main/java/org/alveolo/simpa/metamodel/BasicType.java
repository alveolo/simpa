package org.alveolo.simpa.metamodel;


public class BasicType<X> extends Type<X> {
	public BasicType(Class<X> javaType) {
		super(javaType);
	}

	@Override
	public PersistenceType getPersistenceType() {
		return PersistenceType.BASIC;
	}
}
