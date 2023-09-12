package org.apache.camel.jbang;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class CamelJBangAbstractExtension implements BeforeAllCallback, AfterAllCallback {

	protected final ExecutorService camelJBangParallelExecutor
			= Executors.newSingleThreadExecutor();

	@Override
	public void afterAll(ExtensionContext extensionContext) throws Exception {
		// TODO find some way to use RegisterExtension with cucumber
	}

	@Override
	public void beforeAll(ExtensionContext extensionContext) throws Exception {
		// TODO find some way to use RegisterExtension with cucumber
	}

	public abstract void start();

	public abstract void stop();

	public abstract Future executeInParallel(String... command);

	public abstract String execute(String... command);

	public abstract String getIntegrationBaseUrl();

	public abstract int getPort();
}
