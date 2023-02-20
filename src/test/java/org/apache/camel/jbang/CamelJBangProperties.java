package org.apache.camel.jbang;

public final class CamelJBangProperties {
	private static String dockerBaseImage = System.getProperty("camel.jbang.dockerBaseImage", "eclipse-temurin:17.0.6_10-jdk");
	private static String jbangVersion = System.getProperty("camel.jbang.jbangVersion", "v0.103.1");
	private static String camelVersion = System.getProperty("camel.jbang.camelVersion", "3.20.2");
	private static String camelTestedVersion = System.getProperty("camel.tested.version", "3.20.2");

	private CamelJBangProperties() {
		super();
	}

	public static String getDockerBaseImage() {
		return dockerBaseImage;
	}

	public static String getJbangVersion() {
		return jbangVersion;
	}

	public static String getCamelVersion() {
		return camelVersion;
	}

	public static String getCamelTestedVersion() {
		return camelTestedVersion;
	}
}
