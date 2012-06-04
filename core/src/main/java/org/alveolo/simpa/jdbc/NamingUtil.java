package org.alveolo.simpa.jdbc;

import java.util.Locale;


public class NamingUtil {
	public static String camelCaseToUnderscore(String camelCase) {
		return camelCase
				.replaceAll("(?<=[A-Z])(?=[A-Z][a-z])|(?<=[^A-Z])(?=[A-Z])|(?<=[A-Za-z])(?=[^A-Za-z])", "_")
				.toLowerCase(Locale.ENGLISH);
	}
}
