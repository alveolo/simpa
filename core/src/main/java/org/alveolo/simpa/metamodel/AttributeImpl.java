package org.alveolo.simpa.metamodel;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import javax.persistence.Embeddable;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;


public class AttributeImpl<X, Y> implements Attribute<X, Y> {
	protected final ManagedTypeImpl<X> declaringType;
	protected final Member javaMember;
	protected final String name;
	protected final Class<Y> javaType;
	protected final PersistentAttributeType persistentAttributeType;

	@SuppressWarnings("unchecked")
	public AttributeImpl(ManagedTypeImpl<X> declaringType, Member javaMember) {
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

	protected MetamodelImpl getMetamodel() {
		return declaringType.getMetamodel();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public PersistentAttributeType getPersistentAttributeType() {
		return persistentAttributeType;
	}

	@Override
	public ManagedType<X> getDeclaringType() {
		return declaringType;
	}

	@Override
	public Class<Y> getJavaType() {
		return javaType;
	}

	@Override
	public Member getJavaMember() {
		return javaMember;
	}

	@Override
	public boolean isAssociation() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCollection() {
		// TODO Auto-generated method stub
		return false;
	}

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return ((AccessibleObject) javaMember).getAnnotation(annotationClass);
	}
}
