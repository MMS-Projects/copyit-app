package net.mms_projects.copyit.android.tasks;

import net.mms_projects.copy_it.R;
import net.mms_projects.copyit.api.ServerApi;
import android.app.ProgressDialog;
import android.content.Context;

abstract public class ServerApiUiTask<Params, Progress, Result> extends
		ServerApiTask<Params, Progress, Result> {

	final protected Context context;

	protected ProgressDialog progress;
	protected boolean useProgressDialog = false;
	protected String progressDialogTitle;
	protected String progressDialogMessage;

	protected Exception exception;

	public ServerApiUiTask(Context context, ServerApi api) {
		super(api);

		this.context = context;
		this.progressDialogTitle = this.context.getResources().getString(
				R.string.dialog_title_busy);
		this.progressDialogMessage = this.context.getResources().getString(
				R.string.dialog_title_busy);
	}

	protected Result doInBackground(Params... params) {
		try {
			return this.doInBackgroundWithException(params);
		} catch (Exception e) {
			this.throwException(e);
		}
		return null;
	}

	abstract protected Result doInBackgroundWithException(Params... params) throws Exception;

	@Override
	protected void onPreExecute() {
		if (this.useProgressDialog) {
			this.progress = ProgressDialog.show(this.context,
					this.progressDialogTitle, this.progressDialogMessage, true);
		}

		super.onPreExecute();
	}

	public void setUseProgressDialog(boolean useProgressBar) {
		this.useProgressDialog = useProgressBar;
	}

	public void setProgressDialigTitle(String progressDialogTitle) {
		this.progressDialogTitle = progressDialogTitle;
	}

	public void setProgressDialigMessage(String progressDialogMessage) {
		this.progressDialogMessage = progressDialogMessage;
	}

	/**
	 * Because you can't throw a exception in async task this will simulate one.
	 * You can use doExceptionCheck to check if a exception was thrown.
	 * 
	 * @param exception
	 *            The exception you want to throw
	 */
	protected void throwException(Exception exception) {
		this.exception = exception;
	}

	public void doExceptionCheck() throws Exception {
		if (this.exception != null) {
			throw this.exception;
		}
	}

	@Override
	protected void onPostExecute(Result result) {
		if (this.useProgressDialog) {
			this.progress.dismiss();
		}

		super.onPostExecute(result);
	}

}
