package org.apache.camel.jbang;

import java.io.IOException;

public class CamelJBangExtensionFactory {

	public static CamelJBangAbstractExtension get() {
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			// camel jbang should be installed
			return new CamelJBangSystemProvidedExtension();
		} else {
			// use docker image, linux comptabile
			return new CamelJBangSystemProvidedExtension();
		}
	}
}
