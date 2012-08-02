package org.alveolo.simpa;


/**
 * Defines mapping for enumerated types. The constants of this enumerated type
 * specify how a persistent property or field of an enumerated type should be
 * persisted.
 */
public enum EnumType {
	/** Persist enumerated type property or field as an integer. */
	ORDINAL,

	/** Persist enumerated type property or field as a string. */
	STRING
}
