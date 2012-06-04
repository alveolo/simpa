package org.alveolo.simpa.test.beans;

import static javax.persistence.EnumType.ORDINAL;
import static javax.persistence.EnumType.STRING;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;


@Entity @Table(schema="sch_test", name="tbl_composite")
public class CompositeEnum {
	@EmbeddedId
	private CompositeEnumPK id;

	@Column(name="enum_default")
	private BasicEnum enumDefault;

	@Enumerated(STRING)
	@Column(name="enum_string", columnDefinition="bpchar")
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
