package org.alveolo.simpa.metamodel;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alveolo.simpa.Embeddable;
import org.alveolo.simpa.Entity;
import org.alveolo.simpa.MappedSuperclass;
import org.alveolo.simpa.PersistenceException;
import org.alveolo.simpa.SequenceGenerator;
import org.alveolo.simpa.jdbc.IdGenerator;
import org.alveolo.simpa.jdbc.JdbcSequenceGenerator;
import org.alveolo.simpa.jdbc.UuidGenerator;
import org.alveolo.simpa.metamodel.Type.PersistenceType;


public class Metamodel {
	private final Map<Class<?>, ManagedType<?>> managedTypeMap;

	private final Set<ManagedType<?>> managedTypes;
	private final Set<EntityType<?>> entities;
	private final Set<EmbeddableType<?>> embeddables;

	private final Map<String, IdGenerator> generators;

	public Metamodel(List<Class<?>> classes) {
		Map<Class<?>, ManagedType<?>> managedTypeMap = new HashMap<>();

		Set<ManagedType<?>> managedTypes = new HashSet<>();
		Set<EntityType<?>> entities = new HashSet<>();
		Set<EmbeddableType<?>> embeddables = new HashSet<>();
		Map<String, IdGenerator> generators = new HashMap<>();

		IdGenerator generator = new UuidGenerator();
		generators.put(generator.getName(), generator);

		for (Class<?> javaType : classes) {
			ManagedType<?> managedType = createManagedType(javaType);

			managedTypeMap.put(javaType, managedType);
			managedTypes.add(managedType);

			if (managedType.getPersistenceType() == PersistenceType.ENTITY) {
				entities.add((EntityType<?>) managedType);
			}

			if (managedType.getPersistenceType() == PersistenceType.EMBEDDABLE) {
				embeddables.add((EmbeddableType<?>) managedType);
			}

			SequenceGenerator annotation = javaType.getAnnotation(SequenceGenerator.class);
			if (annotation != null) {
				generator = new JdbcSequenceGenerator(annotation);
				generators.put(generator.getName(), generator);
			}
		}

		this.managedTypeMap = Collections.unmodifiableMap(managedTypeMap);
		this.managedTypes = Collections.unmodifiableSet(managedTypes);
		this.entities = Collections.unmodifiableSet(entities);
		this.embeddables = Collections.unmodifiableSet(embeddables);
		this.generators = Collections.unmodifiableMap(generators);
	}

	private <X> ManagedType<X> createManagedType(Class<X> javaType) {
		Entity entity = javaType.getAnnotation(Entity.class);
		Embeddable embeddable = javaType.getAnnotation(Embeddable.class);
		MappedSuperclass mappedSuperclass = javaType.getAnnotation(MappedSuperclass.class);

		int count = 0;
		if (entity != null) {
			++count;
		}
		if (embeddable != null) {
			++count;
		}
		if (mappedSuperclass != null) {
			++count;
		}

		if (count > 1) {
			throw new IllegalArgumentException(javaType +
					" only one managed type annotation is expected on a managed class");
		}

		if (entity != null) {
			return new EntityType<>(this, javaType, entity.name());
		}

		if (embeddable != null) {
			return new EmbeddableType<>(this, javaType);
		}

		if (mappedSuperclass != null) {
			return new MappedSuperclassType<>(this, javaType);
		}

		throw new IllegalArgumentException(javaType + " is not properly annotated as managed type");
	}

	@SuppressWarnings("unchecked")
	public <X> ManagedType<X> managedType(Class<X> cls) {
		ManagedType<?> managedType = managedTypeMap.get(cls);
		if (managedType == null) {
			throw new IllegalArgumentException(cls + " is not a managed type");
		}
		return (ManagedType<X>) managedType;
	}

	public <X> EntityType<X> entity(Class<X> cls) {
		ManagedType<X> managedType = managedType(cls);
		try {
			return (EntityType<X>) managedType;
		} catch (ClassCastException e) {
			throw new IllegalArgumentException(cls + " is not an entity type");
		}
	}

	public <X> EmbeddableType<X> embeddable(Class<X> cls) {
		ManagedType<X> managedType = managedType(cls);
		try {
			return (EmbeddableType<X>) managedType;
		} catch (ClassCastException e) {
			throw new IllegalArgumentException(cls + " is not an embeddable type");
		}
	}

	public Set<ManagedType<?>> getManagedTypes() {
		return managedTypes;
	}

	public Set<EntityType<?>> getEntities() {
		return entities;
	}

	public Set<EmbeddableType<?>> getEmbeddables() {
		return embeddables;
	}

	public IdGenerator getGenerator(String name) {
		IdGenerator generator = generators.get(name);
		if (generator == null) {
			throw new PersistenceException("Generator not found: " + name);
		}

		return generator;
	}
}
