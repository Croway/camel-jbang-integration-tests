package org.apache.camel.jbang.steps;

import org.apache.camel.jbang.CamelJBangProperties;
import org.apache.camel.jbang.CamelJBangTest;

import org.assertj.core.api.Assertions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.cucumber.java.en.And;

public class BackgroundStepDefinitions {

	@And("camel jbang is installed")
	public void camel_jbang_is_installed() throws IOException {
		Assertions.assertThat(CamelJBangTest.CAMEL_JBANG.execute("camel", "-V"))
				.contains(CamelJBangProperties.getCamelVersion());
	}
}
