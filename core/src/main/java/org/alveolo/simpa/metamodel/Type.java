package org.alveolo.simpa.metamodel;


public abstract class Type<X> {
	public static enum PersistenceType {
		/**
		 * Entity
		 */
		ENTITY,

		/**
		 * Embeddable class
		 */
		EMBEDDABLE,

		/**
		 * Mapped superclass
		 */
		MAPPED_SUPERCLASS,

		/**
		 * Basic type
		 */
		BASIC
	}

	protected final Class<X> javaType;

	public Type(Class<X> javaType) {
		this.javaType = javaType;
	}

	public Class<X> getJavaType() {
		return javaType;
	}

	public abstract PersistenceType getPersistenceType();
}
