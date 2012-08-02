package org.alveolo.simpa;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Specifies that the property or field is not persistent. It is used to
 * annotate a property or field of an entity class, mapped superclass, or
 * embeddable class.
 *
 * <pre>
 *    Example:
 *
 *    &#064;Entity
 *    public class Employee {
 *        &#064;Id int id;
 *        &#064;Transient User currentUser;
 *        ...
 *    }
 * </pre>
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface Transient {
}
