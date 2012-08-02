package org.alveolo.simpa;


/**
 * Type used to indicate a specific mapping of <code>java.util.Date</code> or
 * <code>java.util.Calendar</code>.
 */
public enum TemporalType {
	/** Map as <code>java.sql.Date</code> */
	DATE,

	/** Map as <code>java.sql.Time</code> */
	TIME,

	/** Map as <code>java.sql.Timestamp</code> */
	TIMESTAMP
}
