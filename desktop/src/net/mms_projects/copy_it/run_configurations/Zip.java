package net.mms_projects.copy_it.run_configurations;

import net.mms_projects.copy_it.app.CopyItDesktop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Zip {

	public static void main(String[] args) {
		Logger log = LoggerFactory.getLogger(Zip.class);

		log.info("Running from zip installation...");
		log.debug("Classpath: {}",
				java.lang.System.getProperty("java.class.path"));

		CopyItDesktop.main(args);
	}

}
