package org.alveolo.simpa;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.alveolo.simpa.GenerationType.AUTO;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * Provides for the specification of generation strategies for the values of
 * primary keys.
 *
 * <p>The <code>GeneratedValue</code> annotation may be applied to a primary
 * key property or field of an entity or mapped superclass in conjunction
 * with the {@link Id} annotation. The use of the <code>GeneratedValue</code>
 * annotation is only required to be supported for simple primary keys. Use
 * of the <code>GeneratedValue</code> annotation is not supported for derived
 * primary keys.
 *
 * <pre>
 *     Example:
 *
 *     &#064;Id
 *     &#064;GeneratedValue(strategy=SEQUENCE, generator="CUST_SEQ")
 *     &#064;Column(name="CUST_ID")
 *     public Long getId() { return id; }
 * </pre>
 *
 * @see Id
 * @see SequenceGenerator
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface GeneratedValue {
	/**
	 * (Optional) The primary key generation strategy that the persistence
	 * provider must use to  generate the annotated entity primary key.
	 */
	GenerationType strategy() default AUTO;

	/**
	 * (Optional) The name of the primary key generator to use as specified
	 * in the {@link SequenceGenerator} or {@link TableGenerator} annotation.
	 * <p>Defaults to the id generator supplied by persistence provider.
	 */
	String generator() default "";
}
