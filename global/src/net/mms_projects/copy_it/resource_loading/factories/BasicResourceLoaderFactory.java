package net.mms_projects.copy_it.resource_loading.factories;

import net.mms_projects.copy_it.resource_loading.AndroidResourceLoader;
import net.mms_projects.copy_it.resource_loading.BasicResourceLoader;
import net.mms_projects.copy_it.resource_loading.DefaultResourceLoader;

public class BasicResourceLoaderFactory {

	public static BasicResourceLoader getResourceLoader() {
		BasicResourceLoader resourceLoader = new BasicResourceLoader();
		resourceLoader.addResourceLoader(new DefaultResourceLoader());
		resourceLoader.addResourceLoader(new AndroidResourceLoader());
		return resourceLoader;
	}
	
}
