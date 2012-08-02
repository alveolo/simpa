package org.alveolo.simpa;


/**
 * Thrown by the persistence provider when a problem occurs.
 * All instances of <code>PersistenceException</code> except for instances of
 * {@link NoResultException}, {@link NonUniqueResultException},
 * {@link LockTimeoutException}, and {@link QueryTimeoutException} will cause
 * the current transaction, if one is active, to be marked for rollback.
 */
@SuppressWarnings("serial")
public class PersistenceException extends RuntimeException {
	/**
	 * Constructs a new <code>PersistenceException</code> exception
	 * with <code>null</code> as its detail message.
	 */
	public PersistenceException() {}

	/**
	 * Constructs a new <code>PersistenceException</code> exception
	 * with the specified detail message.
	 *
	 * @param message the detail message.
	 */
	public PersistenceException(String message) {
		super(message);
	}

	/**
	 * Constructs a new <code>PersistenceException</code> exception
	 * with the specified detail message and cause.
	 *
	 * @param message the detail message.
	 * @param cause the cause.
	 */
	public PersistenceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new <code>PersistenceException</code> exception
	 * with the specified cause.
	 *
	 * @param cause the cause.
	 */
	public PersistenceException(Throwable cause) {
		super(cause);
	}
}
