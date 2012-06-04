package org.alveolo.simpa.metamodel;

import javax.persistence.metamodel.EntityType;


public class EntityTypeImpl<X> extends IdentifiableTypeImpl<X> implements EntityType<X> {
	private final String name;

	public EntityTypeImpl(MetamodelImpl metamodel, Class<X> javaType, String name) {
		super(metamodel, javaType);

		this.name = (name == null) ? javaType.getSimpleName() : name;
	}

	@Override
	public BindableType getBindableType() {
		return BindableType.ENTITY_TYPE;
	}

	@Override
	public Class<X> getBindableJavaType() {
		return javaType;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public PersistenceType getPersistenceType() {
		return PersistenceType.ENTITY;
	}
}
