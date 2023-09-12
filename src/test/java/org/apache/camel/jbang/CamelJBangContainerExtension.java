package org.apache.camel.jbang;

import org.junit.jupiter.api.extension.ExtensionContext;

import org.assertj.core.api.Assertions;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Future;

public class CamelJBangContainerExtension extends CamelJBangAbstractExtension {

	public static GenericContainer CONTAINER;

	@Override
	public void afterAll(ExtensionContext extensionContext) throws Exception {
		// TODO find some way to use RegisterExtension with cucumber
	}

	@Override
	public void beforeAll(ExtensionContext extensionContext) throws Exception {
		// TODO find some way to use RegisterExtension with cucumber
	}

	public int getPort() {
		return CONTAINER.getMappedPort(8080);
	}

	public String getIntegrationBaseUrl() {
		return String.format("http://localhost:%d", CONTAINER.getMappedPort(8080));
	}

	public String execute(String... command) {
		try {
			Container.ExecResult execResult = CONTAINER.execInContainer(command);
			if (execResult.getExitCode() != 0) {
				Assertions.fail(String.format("command %s failed with error %s", command, execResult.getStderr()));
			}

			return execResult.getStdout();
		} catch (Exception e) {
			Assertions.fail(String.format("command %s failed", command), e);
			throw new RuntimeException(e);
		}
	}

	public Future<Container.ExecResult> executeInParallel(String... command) {
		return camelJBangParallelExecutor.submit(() -> CONTAINER.execInContainer(command));
	}

	public void start() {
		// Create target/data folder so that container user does not mess with permission withFileSystemBind
		try {
			Path dataFolder = Path.of(CamelJBangTest.DATA_FOLDER);
			if (!Files.exists(dataFolder)) {
				Files.createDirectory(dataFolder);
			}
		} catch (IOException e) {
			Assertions.fail(String.format("%s directory cannot be created, %s", CamelJBangTest.DATA_FOLDER, e.getMessage()), e);
		}

		CONTAINER = new GenericContainer(
				new ImageFromDockerfile("camel-jbang", false)
						.withFileFromClasspath("Dockerfile", "Dockerfile")
						.withBuildArg("CAMEL_REF", CamelJBangProperties.CAMEL_VERSION_REF))
				.withExposedPorts(8080)
				.withFileSystemBind(CamelJBangTest.DATA_FOLDER, "/home/app", BindMode.READ_WRITE);

		CONTAINER.setWaitStrategy(null); // 8080 Port will be exposed later on, remove default wait strategy

		CONTAINER.start();
	}

	public void stop() {
		CONTAINER.stop();
	}
}
