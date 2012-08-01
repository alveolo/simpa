package org.alveolo.simpa.metamodel;


public class EntityType<X> extends IdentifiableType<X> implements Bindable<X> {
	private final String name;

	public EntityType(Metamodel metamodel, Class<X> javaType, String name) {
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

	public String getName() {
		return name;
	}

	@Override
	public PersistenceType getPersistenceType() {
		return PersistenceType.ENTITY;
	}
}
