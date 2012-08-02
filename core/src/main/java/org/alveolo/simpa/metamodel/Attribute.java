package org.alveolo.simpa.metamodel;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import org.alveolo.simpa.Embeddable;


public class Attribute<X, Y> {
	public static enum PersistentAttributeType {
		/**
		 * Many-to-one association
		 */
		MANY_TO_ONE,

		/**
		 * One-to-one association
		 */
		ONE_TO_ONE,

		/**
		 * Basic attribute
		 */
		BASIC,

		/**
		 * Embeddable class attribute
		 */
		EMBEDDED,

		/**
		 * Many-to-many association
		 */
		MANY_TO_MANY,

		/**
		 * One-to-many association
		 */
		ONE_TO_MANY,

		/**
		 * Element collection
		 */
		ELEMENT_COLLECTION
	}

	protected final ManagedType<X> declaringType;
	protected final Member javaMember;
	protected final String name;
	protected final Class<Y> javaType;
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
		} else {
			javaType = (Class<Y>) ((Method) javaMember).getReturnType();
		}

		if (javaType.getAnnotation(Embeddable.class) != null) {
			persistentAttributeType = PersistentAttributeType.EMBEDDED;
		} else {
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

	public Member getJavaMember() {
		return javaMember;
	}

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return ((AccessibleObject) javaMember).getAnnotation(annotationClass);
	}
}
