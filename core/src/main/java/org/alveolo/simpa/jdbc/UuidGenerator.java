package org.alveolo.simpa.jdbc;

import java.util.UUID;

import org.alveolo.simpa.EntityStore;


public class UuidGenerator extends IdGenerator {
	@Override
	public String getName() {
		return "system-uuid";
	}

	@Override
	public Object next(EntityStore store) {
		return UUID.randomUUID();
	}
}
