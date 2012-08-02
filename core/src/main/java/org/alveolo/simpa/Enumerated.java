package org.alveolo.simpa;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.alveolo.simpa.EnumType.ORDINAL;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * Specifies that a persistent property or field should be persisted as
 * an enumerated type. If the enumerated type is not specified or the
 * <code>Enumerated</code> annotation is not used, the <code>EnumType</code>
 * value is assumed to be <code>ORDINAL<code>.
 *
 * <pre>
 *   Example:
 *
 *   public enum EmployeeStatus {FULL_TIME, PART_TIME, CONTRACT}
 *
 *   public enum SalaryRate {JUNIOR, SENIOR, MANAGER, EXECUTIVE}
 *
 *   &#064;Entity public class Employee {
 *       public EmployeeStatus getStatus() {...}
 *       ...
 *       &#064;Enumerated(STRING)
 *       public SalaryRate getPayScale() {...}
 *       ...
 *   }
 * </pre>
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Enumerated {
	/** (Optional) The type used in mapping an enum type. */
	EnumType value() default ORDINAL;
}
