package net.mms_projects.copy_it;

import net.mms_projects.copy_it.resource_loading.ResourceLoaderInterface;
import net.mms_projects.copy_it.resource_loading.factories.BasicResourceLoaderFactory;

public class Messages {

	private static ResourceLoaderInterface resourceLoader = BasicResourceLoaderFactory
			.getResourceLoader();

	@Deprecated
	public static String getString(String key) {
		return resourceLoader.getString(key);
	}

	@Deprecated
	public static String getString(String key, Object... formatArgs) {
		return resourceLoader.getString(key, formatArgs);
	}
}
