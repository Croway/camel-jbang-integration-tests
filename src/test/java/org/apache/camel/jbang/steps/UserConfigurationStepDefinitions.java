package org.apache.camel.jbang.steps;

import org.apache.camel.jbang.CamelJBangTest;
import org.apache.camel.jbang.CamelJBangTestState;

import io.cucumber.java.en.Given;

public class UserConfigurationStepDefinitions {

	private CamelJBangTestState testState;

	public UserConfigurationStepDefinitions(CamelJBangTestState testState) {
		this.testState = testState;
	}

	@Given("user configure camel property {string} with value {string}")
	public void user_execute_configuration(String property, String value) {
		String command = String.format("camel config set %s=%s", property, value);
		testState.setStdOutput(CamelJBangTest.CAMEL_JBANG.execute(command.split(" ")));
	}

	@Given("user configure camel property {string} with passed value")
	public void user_execute_configuration_with_param(String property) {
		String command_key = String.format("camel config set %s=", property);
		String value = "";
		switch (property) {
			case "camel-version" :
				value = System.getProperty("camel.tested.version");
				break;
			case "additional-properties=openshift-maven-plugin-version" :
				value = System.getProperty("openshift.maven.plugin.version");
				break;
			case "camel-spring-boot-version" :
				value = System.getProperty("camel.springboot.version");
				break;
		}
		String command = command_key.concat(value);
		testState.setStdOutput(CamelJBangTest.CAMEL_JBANG.execute(command.split(" ")));
	}
}
