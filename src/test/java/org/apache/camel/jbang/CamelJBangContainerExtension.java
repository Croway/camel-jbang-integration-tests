package org.apache.camel.jbang;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import com.github.dockerjava.api.command.CreateContainerCmd;

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
			throw new RuntimeException(e);
		}
	}

	static {
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
								.run("cp /root/.jbang/bin/CamelJBang /bin/camel")
								.env("PATH", "${PATH}:/root/.jbang/bin")
								.entryPoint("tail", "-f", "/dev/null")
								.build()
				))
				.withFileSystemBind(CamelJBangTest.DATA_FOLDER, "/home/app", BindMode.READ_WRITE);
	}

	public String execute(String... command) {
		try {
			Container.ExecResult execResult = CONTAINER.execInContainer(command);
			if (execResult.getExitCode() != 0) {
				throw new RuntimeException(execResult.getStderr());
			}

			return execResult.getStdout();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public Future<Container.ExecResult> executeInParallel(String... command) {
		return camelJBangParallelExecutor.submit(() -> {
			return CONTAINER.execInContainer(command);
		});
	}

	public void start() {
		CONTAINER.start();
	}

	public void stop() {
		CONTAINER.stop();
	}
}
