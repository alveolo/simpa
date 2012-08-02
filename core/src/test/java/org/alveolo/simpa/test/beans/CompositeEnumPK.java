package org.alveolo.simpa.test.beans;

import static org.alveolo.simpa.EnumType.STRING;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.alveolo.simpa.Column;
import org.alveolo.simpa.Embeddable;
import org.alveolo.simpa.Enumerated;


@Embeddable
public class CompositeEnumPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name="long_id", updatable=false)
	private long longId;

	@NotNull
	@Enumerated(STRING)
	@Column(name="enum_id", updatable=false)
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
