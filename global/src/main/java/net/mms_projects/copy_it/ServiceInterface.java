package net.mms_projects.copy_it;

import java.util.concurrent.Executor;

public interface ServiceInterface {
	
	public void setExecutor(Executor executor);
	
	public Executor getExecutor();
	
	public String getServiceName();
	
}
