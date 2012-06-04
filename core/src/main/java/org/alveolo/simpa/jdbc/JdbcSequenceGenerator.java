package org.alveolo.simpa.jdbc;

import javax.persistence.SequenceGenerator;

import org.alveolo.simpa.EntityStore;


public class JdbcSequenceGenerator extends IdGenerator {
	private final SequenceGenerator annotation;
	private long lo, hi;

	public JdbcSequenceGenerator(SequenceGenerator annotation) {
		this.annotation = annotation;
	}

	@Override
	public String getName() {
		return annotation.name();
	}

	@Override
	public Object next(EntityStore store) {
		if (lo == 0 || ++lo >= hi) {
			lo = ((JdbcStore) store).nextval(annotation);
			hi = lo + annotation.allocationSize();
		}

		return lo;
	}
}
