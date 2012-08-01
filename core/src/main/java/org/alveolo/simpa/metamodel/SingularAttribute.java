package org.alveolo.simpa.metamodel;

import java.lang.reflect.Member;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.PersistenceException;
import javax.persistence.Temporal;
import javax.persistence.Version;


public class SingularAttribute<X, T> extends Attribute<X, T> implements Bindable<T> {
	public SingularAttribute(ManagedType<X> declaringType, Member javaMember) {
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

	public boolean isId() {
		return getAnnotation(Id.class) != null || getAnnotation(EmbeddedId.class) != null;
	}

	public boolean isVersion() {
		return getAnnotation(Version.class) != null;
	}

	public boolean isOptional() {
		Column column = getAnnotation(Column.class);
		return (column == null) ? true : column.nullable();
	}

	public Type<T> getType() {
		// TODO: association entity types

		if (getAnnotation(Embedded.class) != null || getAnnotation(EmbeddedId.class) != null) {
			return getMetamodel().embeddable(javaType);
		}

		return new BasicType<>(javaType);
	}
}
