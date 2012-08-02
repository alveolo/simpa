package org.alveolo.simpa;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Applied to a persistent field or property of an entity class or mapped
 * superclass to denote a composite primary key that is an embeddable class.
 * The embeddable class must be annotated as {@link Embeddable}.
 *
 * <p>There must be only one <code>EmbeddedId</code> annotation and no
 * <code>Id</code> annotation when the <code>EmbeddedId</code> annotation
 * is used.
 *
 * <p>The {@link AttributeOverride} annotation may be used to override
 * the column mappings declared within the embeddable class.
 *
 * <p>Relationship mappings defined within an embedded id class are not
 * supported.
 *
 * <pre>
 *    Example 1:
 *
 *    &#064;EmbeddedId
 *    protected EmployeePK empPK;
 *
 *
 *    Example 2:
 *
 *    &#064;Embeddable
 *    public class DependentId {
 *       String name;
 *       EmployeeId empPK; // corresponds to primary key type of Employee
 *    }
 *
 *    &#064;Entity
 *    public class Dependent {
 *       // default column name for "name" attribute is overridden
 *       &#064;EmbeddedId DependentId id;
 *       ...
 *    }
 * </pre>
 *
 * @see Embeddable
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)

public @interface EmbeddedId {
}
