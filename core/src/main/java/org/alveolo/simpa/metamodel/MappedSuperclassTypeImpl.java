package org.alveolo.simpa.metamodel;

import javax.persistence.metamodel.MappedSuperclassType;


public class MappedSuperclassTypeImpl<X> extends IdentifiableTypeImpl<X> implements MappedSuperclassType<X> {
	public MappedSuperclassTypeImpl(MetamodelImpl metamodel, Class<X> javaType) {
		super(metamodel, javaType);
	}

	@Override
	public PersistenceType getPersistenceType() {
		return PersistenceType.MAPPED_SUPERCLASS;
	}
}
