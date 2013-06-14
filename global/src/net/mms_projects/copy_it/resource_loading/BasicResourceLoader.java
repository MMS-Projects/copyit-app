package net.mms_projects.copy_it.resource_loading;

import java.util.ArrayList;
import java.util.List;

public class BasicResourceLoader implements ResourceLoaderInterface {

	private List<ResourceLoaderInterface> resourceLoaders = new ArrayList<ResourceLoaderInterface>();

	public void addResourceLoader(ResourceLoaderInterface resourceLoader) {
		this.resourceLoaders.add(resourceLoader);
	}

	public List<ResourceLoaderInterface> getResourceLoaders() {
		return this.resourceLoaders;
	}

	@Override
	public String getString(String name) {
		for (ResourceLoaderInterface resourceLoader : getResourceLoaders()) {
			String output = resourceLoader.getString(name);
			if (output != null) {
				return output;
			}
		}
		return null;
	}

	@Override
	public String getString(String name, Object... formatArgs) {
		return String.format(getString(name), formatArgs);
	}

}
