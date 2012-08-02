package org.alveolo.simpa;


@SuppressWarnings("serial")
public class EntityNotFoundException extends PersistenceException {
	/**
	 * Constructs a new <code>EntityNotFoundException</code> exception with
	 * <code>null</code> as its detail message.
	 */
	public EntityNotFoundException() {
	}

	/**
	 * Constructs a new <code>EntityNotFoundException</code> exception with
	 * the specified detail message.
	 *
	 * @param message  the detail message.
	 */
	public EntityNotFoundException(String message) {
		super(message);
	}
}
