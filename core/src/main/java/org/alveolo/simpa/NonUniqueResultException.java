package org.alveolo.simpa;


@SuppressWarnings("serial")
public class NonUniqueResultException extends PersistenceException {
	/**
	 * Constructs a new <code>NonUniqueResultException</code> exception with
	 * <code>null</code> as its detail message.
	 */
	public NonUniqueResultException() {}

	/**
	 * Constructs a new <code>NonUniqueResultException</code> exception with
	 * the specified detail message.
	 *
	 * @param message  the detail message.
	 */
	public NonUniqueResultException(String message) {
		super(message);
	}
}
