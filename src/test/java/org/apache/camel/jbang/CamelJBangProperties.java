package org.apache.camel.jbang;

public final class CamelJBangProperties {
	public static final String DOCKER_BASE_IMAGE = System.getProperty("camel.jbang.dockerBaseImage", "eclipse-temurin:17.0.6_10-jdk");
	public static final String JBANG_VERSION = System.getProperty("camel.jbang.jbangVersion", "v0.103.1");
	public static final String CAMEL_VERSION = System.getProperty("camel.jbang.camelVersion", "3.20.2");
	public static final String CAMEL_VERSION_REF = System.getProperty("camel.jbang.camelVersionRef", "main");
	public static final String CAMEL_TESTED_VERSION = System.getProperty("camel.tested.version", "3.20.2");

	private CamelJBangProperties() {
		super();
	}
}
