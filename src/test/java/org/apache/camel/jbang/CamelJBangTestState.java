package org.apache.camel.jbang;

import java.net.http.HttpResponse;

/**
 * Stores test state, injected in StepDefinitions with cucumber-picocontainer
 */
public class CamelJBangTestState {
	private String stdOutput;
	private HttpResponse<String> httpResponse;

	public CamelJBangTestState() {
		super();
	}

	public String getStdOutput() {
		return stdOutput;
	}

	public void setStdOutput(String stdOutput) {
		this.stdOutput = stdOutput;
	}

	public HttpResponse<String> getHttpResponse() {
		return httpResponse;
	}

	public void setHttpResponse(HttpResponse<String> httpResponse) {
		this.httpResponse = httpResponse;
	}
}
