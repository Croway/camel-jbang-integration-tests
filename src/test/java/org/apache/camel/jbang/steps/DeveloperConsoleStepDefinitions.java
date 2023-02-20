package org.apache.camel.jbang.steps;

import org.apache.camel.jbang.CamelJBangTest;

import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class DeveloperConsoleStepDefinitions {
	private static Logger LOG = LoggerFactory.getLogger(DeveloperConsoleStepDefinitions.class);

	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().build();

	private HttpResponse<String> response;

	@And("user execute HTTP request {string}")
	public void execute_http_request(String url) throws IOException, InterruptedException {
		String endpoint = "http://localhost:" + CamelJBangTest.CAMEL_JBANG.getPort() + url;

		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create("http://localhost:" + CamelJBangTest.CAMEL_JBANG.getPort() + url))
				.build();

		HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() != 200) {
			throw new RuntimeException(
					String.format("Call to endpoint %s failed with status %s and body %s", endpoint, response.statusCode(), response.body()));
		}

		this.response = response;
	}

	@Then("HTTP response contains {string}")
	public void http_response_contains(String expected) {
		Assertions.assertThat(response.body()).contains(expected);
	}
}
