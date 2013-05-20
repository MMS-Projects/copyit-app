package net.mms_projects.copyit.android.tasks;

import net.mms_projects.copy_it.R;
import net.mms_projects.copyit.api.ServerApi;
import android.os.AsyncTask;

abstract public class ServerApiTask<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result> {

	final protected ServerApi api;

	public ServerApiTask(ServerApi api) {
		this.api = api;
	}

}
