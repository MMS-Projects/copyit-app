package net.mms_projects.copy_it.resource_loading;


public interface ResourceLoaderInterface {

	public String getString(String name);

	public String getString(String name, Object... formatArgs);

}
