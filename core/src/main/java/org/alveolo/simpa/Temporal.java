package org.alveolo.simpa;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * This annotation must be specified for persistent fields or properties of
 * type <code>java.util.Date</code> and <code>java.util.Calendar</code>. It
 * may only be specified for fields or properties of these types.
 *
 * <p>The <code>Temporal</code> annotation may be used in conjunction with
 * the {@link Id} annotation.
 *
 * <pre>
 *     Example:
 *
 *     &#064;Temporal(DATE)
 *     protected java.util.Date endDate;
 * </pre>
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface Temporal {
	/**
	 * The type used in mapping <code>java.util.Date</code> or
	 * <code>java.util.Calendar</code>.
	 */
	TemporalType value();
}
