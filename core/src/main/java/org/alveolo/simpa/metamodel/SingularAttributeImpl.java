package org.alveolo.simpa.metamodel;

import java.lang.reflect.Member;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.PersistenceException;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;


public class SingularAttributeImpl<X, T> extends AttributeImpl<X, T> implements SingularAttribute<X, T> {
	public SingularAttributeImpl(ManagedTypeImpl<X> declaringType, Member javaMember) {
		super(declaringType, javaMember);

		Temporal temporal = getAnnotation(Temporal.class);
		if (temporal != null) {
			Class<?> type = getJavaType();
			if (!type.equals(java.util.Date.class) && !type.equals(java.util.Calendar.class)) {
				throw new PersistenceException("@Temporal annotation can be set only on " +
						"java.util.Date or java.util.Calendar fields");
			}
		}
	}

	@Override
	public BindableType getBindableType() {
		// TODO: Support for plural and entity references
		return BindableType.SINGULAR_ATTRIBUTE;
	}

	@Override
	public Class<T> getBindableJavaType() {
		// TODO: Support for collections
		return getJavaType();
	}

	@Override
	public boolean isId() {
		return getAnnotation(Id.class) != null || getAnnotation(EmbeddedId.class) != null;
	}

	@Override
	public boolean isVersion() {
		return getAnnotation(Version.class) != null;
	}

	@Override
	public boolean isOptional() {
		Column column = getAnnotation(Column.class);
		return (column == null) ? true : column.nullable();
	}

	@Override
	public Type<T> getType() {
		// TODO: association entity types

		if (getAnnotation(Embedded.class) != null || getAnnotation(EmbeddedId.class) != null) {
			return getMetamodel().embeddable(javaType);
		}

		return new BasicTypeImpl<>(javaType);
	}
}
