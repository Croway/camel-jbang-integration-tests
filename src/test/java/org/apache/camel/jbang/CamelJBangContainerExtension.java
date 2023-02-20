package org.apache.camel.jbang;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import org.assertj.core.api.Assertions;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CamelJBangContainerExtension implements BeforeAllCallback, AfterAllCallback {

	public static final GenericContainer CONTAINER;

	private final ExecutorService camelJBangParallelExecutor
			= Executors.newSingleThreadExecutor();

	@Override
	public void afterAll(ExtensionContext extensionContext) throws Exception {
		// TODO find some way to use RegisterExtension with cucumber
	}

	@Override
	public void beforeAll(ExtensionContext extensionContext) throws Exception {
		// TODO find some way to use RegisterExtension with cucumber
	}

	static {
		// Create target/data folder so that container user does not mess with permission withFileSystemBind
		try {
			Path dataFolder = Path.of(CamelJBangTest.DATA_FOLDER);
			if (!Files.exists(dataFolder)) {
				Files.createDirectory(dataFolder);
			}
		} catch (IOException e) {
			Assertions.fail(String.format("%s directory cannot be created, %s", CamelJBangTest.DATA_FOLDER, e.getMessage()), e);
		}

		String jbangDownloadUrl =
				"https://github.com/jbangdev/jbang/releases/download/" + CamelJBangProperties.getJbangVersion() + "/jbang.zip";
		String camelJBangTrustUrl = "https://raw.githubusercontent.com/apache/camel/camel-" + CamelJBangProperties.getCamelVersion()
				+ "/dsl/camel-jbang/camel-jbang-main/src/main/jbang/main/";
		String camelJBangUrl = camelJBangTrustUrl + "CamelJBang.java";

		CONTAINER = new GenericContainer(
				new ImageFromDockerfile("camel-jbang", false).withDockerfileFromBuilder(builder ->
						builder.from(CamelJBangProperties.getDockerBaseImage())
								.workDir("/home")
								.run("curl -LjO " + jbangDownloadUrl + " &&"
										+ " jar xf jbang.zip &&"
										+ " rm jbang.zip &&"
										+ " chmod +x jbang/bin/jbang")
								.env("PATH", "${PATH}:/home/jbang/bin")
								.run("jbang trust add " + camelJBangTrustUrl)
								.run("jbang app install " + camelJBangUrl)
								.run("cp /root/.jbang/bin/CamelJBang /root/.jbang/bin/camel")
								.env("PATH", "${PATH}:/root/.jbang/bin")
								.expose(8080)
								.entryPoint("tail", "-f", "/dev/null")
								.build()
				))
				.withExposedPorts(8080)
				.withFileSystemBind(CamelJBangTest.DATA_FOLDER, "/home/app", BindMode.READ_WRITE);

		CONTAINER.setWaitStrategy(null); // 8080 Port will be exposed later on, remove default wait strategy
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
		CONTAINER.start();
	}

	public void stop() {
		CONTAINER.stop();
	}
}
