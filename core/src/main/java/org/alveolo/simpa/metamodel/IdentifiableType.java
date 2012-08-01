package org.alveolo.simpa.metamodel;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;


public abstract class IdentifiableType<X> extends ManagedType<X> {
	public Set<SingularAttribute<? super X, ?>> idAttributes = new LinkedHashSet<>();

	public IdentifiableType(Metamodel metamodel, Class<X> javaType) {
		super(metamodel, javaType);

		for (SingularAttribute<? super X, ?> attribute : getSingularAttributes()) {
			if (attribute.getAnnotation(Id.class) == null && attribute.getAnnotation(EmbeddedId.class) == null) {
				continue;
			}

			idAttributes.add(attribute);
		}

		// TODO: Support for annotated getters and property access type, analyze superclass
	}

	@SuppressWarnings("unchecked")
	public <Y> SingularAttribute<? super X, Y> getId(Class<Y> type) {
		if (!hasSingleIdAttribute() || getIdType().getJavaType() != type) {
			throw new IllegalArgumentException("Unexpected id type: " + toString());
		}

		return (SingularAttribute<? super X, Y>) idAttributes.iterator().next();
	}

	public boolean hasSingleIdAttribute() {
		return idAttributes.size() == 1;
	}

	public Type<?> getIdType() {
		return idAttributes.iterator().next().getType();
	}
}
