package org.alveolo.simpa.metamodel;


public class MappedSuperclassType<X> extends IdentifiableType<X> {
	public MappedSuperclassType(Metamodel metamodel, Class<X> javaType) {
		super(metamodel, javaType);
	}

	@Override
	public PersistenceType getPersistenceType() {
		return PersistenceType.MAPPED_SUPERCLASS;
	}
}
