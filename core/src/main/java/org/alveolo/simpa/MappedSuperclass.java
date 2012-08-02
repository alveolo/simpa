package org.alveolo.simpa;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Designates a class whose mapping information is applied to the entities
 * that inherit from it. A mapped superclass has no separate table defined
 * for it.
 *
 * <p>A class designated with the <code>MappedSuperclass</code> annotation
 * can be mapped in the same way as an entity except that the mappings will
 * apply only to its subclasses since no table exists for the mapped
 * superclass itself. When applied to the subclasses the inherited mappings
 * will apply in the context of the subclass tables.
 *
 * <pre>
 *    Example: Concrete class as a mapped superclass
 *
 *    &#064;MappedSuperclass
 *    public class Employee {
 *        &#064;Id protected Integer empId;
 *        &#064;Version protected Integer version;
 *
 *        public Integer getEmpId() { ... }
 *        public void setEmpId(Integer id) { ... }
 *    }
 *
 *    // Default table is FTEMPLOYEE table
 *    &#064;Entity
 *    public class FTEmployee extends Employee {
 *        // Inherited empId field mapped to FTEMPLOYEE.EMPID
 *        // Inherited version field mapped to FTEMPLOYEE.VERSION
 *
 *        // Defaults to FTEMPLOYEE.SALARY
 *        protected Integer salary;
 *
 *        public Integer getSalary() { ... }
 *        public void setSalary(Integer salary) { ... }
 *    }
 *
 *    &#064;Entity &#064;Table(name="PT_EMP")
 *    public class PartTimeEmployee extends Employee {
 *        // Inherited empId field mapped to PT_EMP.EMPID
 *        // Inherited version field mapped to PT_EMP.VERSION
 *        &#064;Column(name="WAGE")
 *        protected Float hourlyWage;
 *
 *        public Float getHourlyWage() { ... }
 *        public void setHourlyWage(Float wage) { ... }
 *    }
 * </pre>
 */
@Documented
@Target({TYPE})
@Retention(RUNTIME)
public @interface MappedSuperclass {
}
