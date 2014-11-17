package org.alveolo.simpa.metamodel;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collection;

import org.alveolo.simpa.Array;
import org.alveolo.simpa.Embeddable;
import org.alveolo.simpa.PersistenceException;


public class Attribute<X, Y> {
	public static enum PersistentAttributeType {
		/* TODO: Many-to-one, One-to-one, Many-to-many, One-to-many association */

		/** Basic attribute */
		BASIC,

		/** Array attribute */
		ARRAY,

		/** Embedded class attribute */
		EMBEDDED,
	}

	protected final ManagedType<X> declaringType;
	protected final Member javaMember;
	protected final String name;
	protected final Class<Y> javaType;
	protected final java.lang.reflect.Type genericType;
	protected final PersistentAttributeType persistentAttributeType;

	@SuppressWarnings("unchecked")
	public Attribute(ManagedType<X> declaringType, Member javaMember) {
		this.declaringType = declaringType;
		this.javaMember = javaMember;

		((AccessibleObject) javaMember).setAccessible(true);

		String memberName = javaMember.getName();

		if (javaMember instanceof Method && memberName.startsWith("get")) {
			name = memberName.substring(3);
		} else {
			name = memberName;
		}

		if (javaMember instanceof Field) {
			javaType = (Class<Y>) ((Field) javaMember).getType();
			genericType = ((Field) javaMember).getGenericType();
		} else {
			javaType = (Class<Y>) ((Method) javaMember).getReturnType();
			genericType = ((Method) javaMember).getGenericReturnType();
		}

		if (getAnnotation(Array.class) != null) {
			if (!Collection.class.isAssignableFrom(javaType)) {
				if (!javaType.isArray()) {
					throw new PersistenceException(javaMember.getName() + " is not a collection type!");
				}
			}
			persistentAttributeType = PersistentAttributeType.ARRAY;
		} else if (javaType.getAnnotation(Embeddable.class) != null) {
			persistentAttributeType = PersistentAttributeType.EMBEDDED;
		} else {
			if (Collection.class.isAssignableFrom(javaType)) {
				throw new PersistenceException(javaMember.getName() + " is a collection type! Have you missed the @Array annotation?");
			}
			persistentAttributeType = PersistentAttributeType.BASIC;
		}
	}

	protected Metamodel getMetamodel() {
		return declaringType.getMetamodel();
	}

	public String getName() {
		return name;
	}

	public PersistentAttributeType getPersistentAttributeType() {
		return persistentAttributeType;
	}

	public ManagedType<X> getDeclaringType() {
		return declaringType;
	}

	public Class<Y> getJavaType() {
		return javaType;
	}
	
	public java.lang.reflect.Type getGenericType() {
		return genericType;
	}

	public Member getJavaMember() {
		return javaMember;
	}

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return ((AccessibleObject) javaMember).getAnnotation(annotationClass);
	}
}
