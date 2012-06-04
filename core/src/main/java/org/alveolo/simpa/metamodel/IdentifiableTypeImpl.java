package org.alveolo.simpa.metamodel;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;


public abstract class IdentifiableTypeImpl<X> extends ManagedTypeImpl<X> implements IdentifiableType<X> {
	public Set<SingularAttribute<? super X, ?>> idAttributes = new LinkedHashSet<>();

	public IdentifiableTypeImpl(MetamodelImpl metamodel, Class<X> javaType) {
		super(metamodel, javaType);

		for (SingularAttribute<? super X, ?> attribute : getSingularAttributes()) {
			SingularAttributeImpl<? super X, ?> impl = (SingularAttributeImpl<? super X, ?>) attribute;
			if (impl.getAnnotation(Id.class) == null && impl.getAnnotation(EmbeddedId.class) == null) {
				continue;
			}

			idAttributes.add(attribute);
		}

		// TODO: Support for annotated getters and property access type, analyze superclass
	}

	@Override @SuppressWarnings("unchecked")
	public <Y> SingularAttribute<? super X, Y> getId(Class<Y> type) {
		if (!hasSingleIdAttribute() || getIdType().getJavaType() != type) {
			throw new IllegalArgumentException("Unexpected id type: " + toString());
		}

		return (SingularAttribute<? super X, Y>) idAttributes.iterator().next();
	}

	@Override
	public <Y> SingularAttribute<X, Y> getDeclaredId(Class<Y> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Y> SingularAttribute<? super X, Y> getVersion(Class<Y> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Y> SingularAttribute<X, Y> getDeclaredVersion(Class<Y> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IdentifiableType<? super X> getSupertype() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasSingleIdAttribute() {
		return idAttributes.size() == 1;
	}

	@Override
	public boolean hasVersionAttribute() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<SingularAttribute<? super X, ?>> getIdClassAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type<?> getIdType() {
		return idAttributes.iterator().next().getType();
	}
}
