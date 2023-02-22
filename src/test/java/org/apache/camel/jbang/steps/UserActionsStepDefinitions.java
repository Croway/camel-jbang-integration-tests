package org.apache.camel.jbang.steps;

import org.apache.camel.jbang.CamelJBangProperties;
import org.apache.camel.jbang.CamelJBangTest;
import org.apache.camel.jbang.CamelJBangTestState;

import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.testcontainers.containers.Container;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class UserActionsStepDefinitions {
	private CamelJBangTestState testState;

	public UserActionsStepDefinitions(CamelJBangTestState testState) {
		this.testState = testState;
	}

	@Given("user execute {string}")
	public void user_execute(String command) {
		testState.setStdOutput(CamelJBangTest.CAMEL_JBANG.execute(command.split(" ")));
	}

	@Given("user execute {string} in parallel")
	public void user_execute_in_parallel(String command) {
		Future<Container.ExecResult> execResultFuture = CamelJBangTest.CAMEL_JBANG.executeInParallel(command.split(" "));
	}

	@Given("file {string} is created")
	public void file_is_created(String file) {
		Assertions.assertThat(Files.exists(Path.of(CamelJBangTest.DATA_FOLDER, file)))
				.isTrue();
	}

	@Then("Integration is running")
	public void integration_is_running() {
		Assertions.assertThat(CamelJBangTest.CAMEL_JBANG.execute("camel", "ps")).contains("cheese");
	}

	@Then("{string} is logged")
	public void is_logged(String string) {
		Assertions.assertThat(testState.getStdOutput()).contains(string);
	}

	@Then("integration {string} logs {string}")
	public void integration_logs(String integration, String string) {
		integrationLogContain(integration, string);
	}

	@Then("integration {string} logs expected camel version")
	public void integration_logs_camel_version(String integration) {
		integrationLogContain(integration, CamelJBangProperties.CAMEL_TESTED_VERSION);
	}

	private void integrationLogContain(String integration, String expected) {
		Awaitility.await().atMost(40, TimeUnit.SECONDS).untilAsserted(() ->
				Assertions.assertThat(CamelJBangTest.CAMEL_JBANG.execute("camel", "log", integration, "--follow=false"))
						.contains(expected));
	}

	@Given("user create file {string} with content:")
	public void user_create_integration_with_content(String string, String content) throws IOException {
		Files.write(Path.of(CamelJBangTest.DATA_FOLDER, string), content.getBytes(StandardCharsets.UTF_8));
	}

	@Given("user update {string} content with:")
	public void user_update_content_with(String string, String content) throws IOException {
		Files.write(Path.of(CamelJBangTest.DATA_FOLDER, string), content.getBytes(StandardCharsets.UTF_8));
	}
}
