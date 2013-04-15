package net.mms_projects.copyit.android.tasks;

import net.mms_projects.copyit.api.ServerApi;
import android.content.Context;

abstract public class ServerApiUiTask<Params, Progress, Result> extends
		ServerApiTask<Params, Progress, Result> {

	final protected Context context;

	public ServerApiUiTask(Context context, ServerApi api) {
		super(api);
		
		this.context = context;
	}

}
