package org.alveolo.simpa.metamodel;


public class EmbeddableType<X> extends ManagedType<X> {
	public EmbeddableType(Metamodel metamodel, Class<X> javaType) {
		super(metamodel, javaType);
	}

	@Override
	public PersistenceType getPersistenceType() {
		return PersistenceType.EMBEDDABLE;
	}
}
