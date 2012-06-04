package org.alveolo.simpa.metamodel;

import javax.persistence.metamodel.EmbeddableType;


public class EmbeddableTypeImpl<X> extends ManagedTypeImpl<X> implements EmbeddableType<X> {
	public EmbeddableTypeImpl(MetamodelImpl metamodel, Class<X> javaType) {
		super(metamodel, javaType);
	}

	@Override
	public PersistenceType getPersistenceType() {
		return PersistenceType.EMBEDDABLE;
	}
}
