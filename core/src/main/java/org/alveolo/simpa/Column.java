package org.alveolo.simpa;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Is used to specify the mapped column for a persistent property or field.
 * If no <code>Column</code> annotation is specified, the default values apply.
 *
 * <blockquote><pre>
 *    Example 1:
 *
 *    &#064;Column(name="DESC", length=512)
 *    public String getDescription() { return description; }
 *
 *    Example 2:
 *
 *    &#064;Column(name="DESC", table="EMP_DETAIL")
 *    public String getDescription() { return description; }
 *
 *    Example 3:
 *
 *    &#064;Column(name="ORDER_COST", updatable=false)
 *    public BigDecimal getCost() { return cost; }
 * </pre></blockquote>
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Column {
	/**
	 * (Optional) The name of the column. Defaults to the property or field name.
	 */
	String name() default "";

	/**
	 * (Optional) Whether the column is included in SQL INSERT
	 * statements generated by the persistence provider.
	 */
	boolean insertable() default true;

	/**
	 * (Optional) Whether the column is included in SQL UPDATE
	 * statements generated by the persistence provider.
	 */
	boolean updatable() default true;

	/**
	 * (Optional) The name of the table that contains the column.
	 * If absent the column is assumed to be in the primary table.
	 */
	String table() default "";
}