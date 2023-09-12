package org.apache.camel.jbang;

import org.assertj.core.api.Assertions;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Future;

public class CamelJBangSystemProvidedExtension extends CamelJBangAbstractExtension {
	@Override
	public void start() {
		// nothing to do, already started
	}

	@Override
	public void stop() {

	}

	@Override
	public Future executeInParallel(String... command) {
		// TODO with ProcessBuilder
		return camelJBangParallelExecutor.submit(() -> execute(command));
	}

	@Override
	public String execute(String... command) {
		ProcessBuilder pb = new ProcessBuilder();
		pb.command(command);
		try {
			Process process = pb.start();
			process.waitFor();

			if (process.exitValue() != 0) {
				Assertions.fail(String.format("command %s failed with error %s", command, readInputStream(process.getErrorStream())));
			}

			return readInputStream(process.getInputStream());
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getIntegrationBaseUrl() {
		return String.format("http://localhost:%d", getPort());
	}

	@Override
	public int getPort() {
		return 8080;
	}

	private String readInputStream(InputStream is) throws IOException {
		return new String(is.readAllBytes());
	}
}
