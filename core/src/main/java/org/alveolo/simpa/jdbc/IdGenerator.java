package org.alveolo.simpa.jdbc;

import org.alveolo.simpa.EntityStore;


public abstract class IdGenerator {
	public abstract String getName();
	public abstract Object next(EntityStore es);
}
