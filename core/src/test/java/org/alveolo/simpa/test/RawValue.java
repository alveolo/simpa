package org.alveolo.simpa.test;

import org.alveolo.simpa.Column;


class RawValue {
	@Column(name="simple_id")
	long id;

	@Column(name="simple_name")
	String name;

	@Column(name="cnt")
	int count;
}
