package org.alveolo.simpa.test;

import org.junit.Test;

import static org.alveolo.simpa.jdbc.NamingUtil.*;
import static org.junit.Assert.*;


public class NamingUtilTest {
	@Test
	public void testCamelCaseToUnderscore() {
		assertEquals("lowercase", camelCaseToUnderscore("lowercase"));
		assertEquals("class", camelCaseToUnderscore("Class"));
		assertEquals("my_class", camelCaseToUnderscore("MyClass"));
		assertEquals("html", camelCaseToUnderscore("HTML"));
		assertEquals("pdf_loader", camelCaseToUnderscore("PDFLoader"));
		assertEquals("a_string", camelCaseToUnderscore("AString"));
		assertEquals("simple_xml_parser", camelCaseToUnderscore("SimpleXMLParser"));
		assertEquals("gl_11_version", camelCaseToUnderscore("GL11Version"));
	}
}
