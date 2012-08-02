package org.alveolo.simpa.test.beans;

import static org.alveolo.simpa.EnumType.ORDINAL;
import static org.alveolo.simpa.EnumType.STRING;

import org.alveolo.simpa.Column;
import org.alveolo.simpa.EmbeddedId;
import org.alveolo.simpa.Entity;
import org.alveolo.simpa.Enumerated;
import org.alveolo.simpa.Table;


@Entity @Table(schema="sch_test", name="tbl_composite")
public class CompositeEnum {
	@EmbeddedId
	private CompositeEnumPK id;

	@Column(name="enum_default")
	private BasicEnum enumDefault;

	@Enumerated(STRING)
	@Column(name="enum_string")
	private BasicEnum enumString;

	@Enumerated(ORDINAL)
	@Column(name="enum_ordinal", updatable=false)
	private BasicEnum enumOrdinal;

	/** Required by SimPA/JPA */
	protected CompositeEnum() {}

	/* Convenience constructors */

	public CompositeEnum(CompositeEnumPK id) {
		this.id = id;
	}

	public CompositeEnum(long longId, BasicEnum enumId) {
		this(new CompositeEnumPK(longId, enumId));
	}

	public CompositeEnumPK getId() {
		return id;
	}

	public BasicEnum getEnumDefault() {
		return enumDefault;
	}

	public void setEnumDefault(BasicEnum basicDefault) {
		this.enumDefault = basicDefault;
	}

	public BasicEnum getEnumString() {
		return enumString;
	}

	public void setEnumString(BasicEnum basicString) {
		this.enumString = basicString;
	}

	public BasicEnum getEnumOrdinal() {
		return enumOrdinal;
	}

	public void setEnumOrdinal(BasicEnum basicOrdinal) {
		this.enumOrdinal = basicOrdinal;
	}
}
