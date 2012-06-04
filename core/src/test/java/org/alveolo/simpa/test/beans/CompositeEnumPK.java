package org.alveolo.simpa.test.beans;

import static javax.persistence.EnumType.STRING;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;


@Embeddable
public class CompositeEnumPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="long_id", nullable=false, updatable=false)
	private long longId;

	@Enumerated(STRING)
	@Column(name="enum_id", nullable=false, updatable=false, columnDefinition="bpchar")
	private BasicEnum enumId;

	/** Required by SimPA/JPA */
	protected CompositeEnumPK() {}

	/* Convenience constructors */

	public CompositeEnumPK(long longId, BasicEnum enumId) {
		this.longId = longId;
		this.enumId = enumId;
	}

	/* Getters/Setters */

	public long getLongId() {
		return longId;
	}

	public BasicEnum getEnumId() {
		return enumId;
	}
}
