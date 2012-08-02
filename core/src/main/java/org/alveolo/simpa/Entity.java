package org.alveolo.simpa;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Specifies that the class is an entity. This annotation is applied to the
 * entity class.
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Entity {
	/**
	 * (Optional) The entity name. Defaults to the unqualified name of the
	 * entity class. This name is used to refer to the entity in queries. The
	 * name must not be a reserved literal in the Java Persistence query
	 * language.
	 */
	String name() default "";
}
