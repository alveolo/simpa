package org.alveolo.simpa.test;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import junit.framework.Assert;

import org.alveolo.simpa.SequenceGenerator;
import org.alveolo.simpa.jdbc.JdbcSequenceGenerator;
import org.alveolo.simpa.jdbc.JdbcStore;
import org.alveolo.simpa.test.beans.Simple;
import org.easymock.EasyMock;
import org.junit.Test;


public class IdGeneratorTest {
	@Test
	public void sequence() {
		SequenceGenerator annotation = Simple.class.getAnnotation(SequenceGenerator.class);

		JdbcStore store = EasyMock.createStrictMock(JdbcStore.class);

		expect(store.nextval(annotation)).andReturn(1L);
		expect(store.nextval(annotation)).andReturn(11L);
		expect(store.nextval(annotation)).andReturn(21L);

		replay(store);

		JdbcSequenceGenerator generator = new JdbcSequenceGenerator(annotation);

		for (long i = 1; i <= 30; i++) {
			Assert.assertEquals(i, generator.next(store));
		}

		verify(store);
	}
}
