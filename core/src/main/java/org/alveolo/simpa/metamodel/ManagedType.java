package org.alveolo.simpa.metamodel;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.alveolo.simpa.PersistenceException;
import org.alveolo.simpa.Transient;


public abstract class ManagedType<X> extends Type<X> {
	private final Metamodel metamodel;

	private final Map<String, Attribute<? super X, ?>> attributeMap = new LinkedHashMap<>();
	private final Set<Attribute<? super X, ?>> attributes;
	private final Set<SingularAttribute<? super X, ?>> singularAttributes;

	public ManagedType(Metamodel metamodel, Class<X> javaType) {
		super(javaType);

		this.metamodel = metamodel;

		try {
			javaType.getDeclaredConstructor(); // Ensure there is a default constructor!
		} catch (NoSuchMethodException e) {
			throw new PersistenceException("There is no default constructor in: " + javaType);
		}

		Set<Attribute<? super X, ?>> attributes = new LinkedHashSet<>();
		Set<SingularAttribute<? super X, ?>> singularAttributes = new LinkedHashSet<>();

		for (Field field : javaType.getDeclaredFields()) {
			if (isSyntheticStaticOrTransient(field)) {
				continue;
			}

			SingularAttribute<X, Object> attribute = new SingularAttribute<>(this, field);

			attributeMap.put(field.getName(), attribute);
			attributes.add(attribute);
			singularAttributes.add(attribute);
		}

		// TODO: Support for annotated getters and property access type, analyze superclass

		this.attributes = Collections.unmodifiableSet(attributes);
		this.singularAttributes = Collections.unmodifiableSet(singularAttributes);
	}

	public Metamodel getMetamodel() {
		return metamodel;
	}

	public Set<Attribute<? super X, ?>> getAttributes() {
		return attributes;
	}

	public Set<SingularAttribute<? super X, ?>> getSingularAttributes() {
		return singularAttributes;
	}

	public Attribute<? super X, ?> getAttribute(String name) {
		Attribute<? super X, ?> attribute = attributeMap.get(name);
		if (attribute == null) {
			throw new IllegalArgumentException("Attribute is missing: " + name);
		}

		return attribute;
	}

	public SingularAttribute<? super X, ?> getSingularAttribute(String name) {
		Attribute<? super X, ?> attribute = attributeMap.get(name);
		if (attribute == null || !(attribute instanceof SingularAttribute)) {
			throw new IllegalArgumentException("Attribute is missing: " + name);
		}

		return (SingularAttribute<? super X, ?>) attribute;
	}

	private boolean isSyntheticStaticOrTransient(Member member) {
		return member.isSynthetic()
			|| (member.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT)) != 0
			|| ((AccessibleObject) member).getAnnotation(Transient.class) != null;
	}
}
