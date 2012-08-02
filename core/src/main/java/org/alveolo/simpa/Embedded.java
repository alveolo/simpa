package org.alveolo.simpa;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Specifies a persistent field or property of an entity whose value is
 * an instance of an embeddable class. The embeddable class must be
 * annotated as {@link Embeddable}.
 *
 * <pre>
 *   Example:
 *
 *   &#064;Embedded
 *   public EmploymentPeriod getEmploymentPeriod() { ... }
 * </pre>
 *
 * @see Embeddable
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Embedded {
}
