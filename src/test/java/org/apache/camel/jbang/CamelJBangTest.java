package org.apache.camel.jbang;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("jbang")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
public class CamelJBangTest {

	// TODO find some way to use @RegisterExtension with cucmber
	public static CamelJBangContainerExtension CAMEL_JBANG = new CamelJBangContainerExtension();
	public static final String DATA_FOLDER = "target/data";

	@BeforeAll
	public static void beforeAll() {
		CAMEL_JBANG.start();
	}

	@AfterAll
	public static void afterAll() {
		CAMEL_JBANG.stop();
	}
}
