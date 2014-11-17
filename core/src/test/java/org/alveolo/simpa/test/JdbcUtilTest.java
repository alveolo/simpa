package org.alveolo.simpa.test;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.alveolo.simpa.PersistenceException;
import org.alveolo.simpa.jdbc.JdbcUtil;
import org.alveolo.simpa.test.beans.Simple;
import org.junit.Test;


public class JdbcUtilTest {
	String[] array;

	Collection<String> collection;
	List<String> list;
	Set<String> set;

	List<List<String>> listList;
	List<String[]> listArray;
	List<String>[] arrayList;

	Simple simple;

	private Type getType(String fieldName) throws NoSuchFieldException {
		return JdbcUtilTest.class.getDeclaredField(fieldName).getGenericType();
	}

	@Test
	public void getPgType() throws NoSuchFieldException, SecurityException {
		Assert.assertEquals("varchar", JdbcUtil.getPgType(getType("array")));
		Assert.assertEquals("varchar", JdbcUtil.getPgType(getType("collection")));
		Assert.assertEquals("varchar", JdbcUtil.getPgType(getType("list")));
		Assert.assertEquals("varchar", JdbcUtil.getPgType(getType("set")));
		Assert.assertEquals("varchar", JdbcUtil.getPgType(getType("listList")));
		Assert.assertEquals("varchar", JdbcUtil.getPgType(getType("listArray")));
		Assert.assertEquals("varchar", JdbcUtil.getPgType(getType("arrayList")));
	}

	@Test(expected=PersistenceException.class)
	public void getPgTypeFail() throws NoSuchFieldException, SecurityException {
		JdbcUtil.getPgType(JdbcUtilTest.class.getDeclaredField("simple").getGenericType());
	}

	// TODO: Tests for setPersistenceValue
}
