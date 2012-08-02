package org.alveolo.simpa.test.beans;

import org.alveolo.simpa.Column;
import org.alveolo.simpa.Entity;
import org.alveolo.simpa.GeneratedValue;
import org.alveolo.simpa.GenerationType;
import org.alveolo.simpa.Id;
import org.alveolo.simpa.SequenceGenerator;
import org.alveolo.simpa.Table;


@Entity @Table(schema="sch_test", name="tbl_simple")
@SequenceGenerator(schema="sch_test", name="simple_sequence", sequenceName="seq_simple", allocationSize=10)
public class Simple {
	@Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="simple_sequence")
	@Column(name="simple_id")
	private long id;

	@Column(name="simple_name")
	private String name;

	public Simple() {}

	public Simple(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
