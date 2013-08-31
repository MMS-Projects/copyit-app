package net.mms_projects.copy_it;

import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import net.mms_projects.copy_it.app.CopyItDesktop;
import net.mms_projects.utils.OSValidator;

public class JavaCommandLine {

	/**
	 * This method generates a java command line that can be used to run the
	 * application the way it was started in this runtime
	 * 
	 * @return A string with the Java command line
	 */
	static public String generateJavaCommandLine() {
		List<String> commandline = new ArrayList<String>();

		/*
		 * For some reason Windows needs to open a console when it launches Java
		 * so it needs to use a different Java binary to well not open a
		 * console.
		 */
		if (OSValidator.isWindows()) {
			commandline.add("javaw");
		} else {
			commandline.add("java");
		}
		/*
		 * This adds some the command line arguments passed to the current JVM
		 */
		commandline.addAll(ManagementFactory.getRuntimeMXBean()
				.getInputArguments());
		/*
		 * This adds the current class path
		 */
		commandline.add("-classpath \"" + getClasspath() + "\"");
		/*
		 * This adds the main class to the command line
		 */
		commandline.add(CopyItDesktop.class.getName());

		/*
		 * Converts the array of command line arguments to a long string with
		 * all the command line arguments. Delimited by a string.
		 */
		String rawCommandline = "";
		for (String part : commandline) {
			rawCommandline += part + " ";
		}

		return rawCommandline;
	}

	/**
	 * This methods returns the class path used by the current runtime
	 * 
	 * @return Returns the class path used by the current runtime
	 */
	static private String getClasspath() {
		String classpath = "";
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();

		URL[] urls = ((URLClassLoader) classLoader).getURLs();

		for (URL url : urls) {
			classpath += url.getFile();
			classpath += System.getProperty("path.separator");
		}
		return classpath;
	}

}
