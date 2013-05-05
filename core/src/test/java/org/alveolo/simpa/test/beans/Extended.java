package org.alveolo.simpa.test.beans;

import org.alveolo.simpa.Column;
import org.alveolo.simpa.Entity;
import org.alveolo.simpa.Table;


@Entity @Table(schema="sch_test", name="tbl_extended")
public class Extended extends Simple {
	@Column
	private String value;

	public Extended() {}

	public Extended(long id) {
		super(id);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
