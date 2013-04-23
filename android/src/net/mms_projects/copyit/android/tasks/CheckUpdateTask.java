package net.mms_projects.copyit.android.tasks;

import net.mms_projects.copy_it.R;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.endpoints.GetBuildInfo;
import net.mms_projects.copyit.api.responses.JenkinsBuildResponse;
import net.mms_projects.copyit.app.CopyItAndroid;
import net.mms_projects.copyit.ui.android.UpdateActivity;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class CheckUpdateTask extends
		ServerApiUiTask<Void, Void, JenkinsBuildResponse> {

	public CheckUpdateTask(Context context, ServerApi api) {
		super(context, api);
	}

	@Override
	protected JenkinsBuildResponse doInBackgroundWithException(Void... params)
			throws Exception {
		return new GetBuildInfo(api).getLatestStableBuild();
	}

	@SuppressLint("InlinedApi")
	@Override
	protected void onPostExecute(JenkinsBuildResponse result) {
		try {
			this.doExceptionCheck();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		int currentBuildNumber = CopyItAndroid.getBuildNumber(this.context);
		int latestBuildNumber = result.number;

		if (latestBuildNumber > currentBuildNumber) {
			Intent intent = new Intent(this.context, UpdateActivity.class);
			intent.putExtra("build_latest", result.number);
			intent.putExtra("build_url", result.url);
			if (android.os.Build.VERSION.SDK_INT >= 11) {
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			} else {
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}

			NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(
					this.context)
					.setSmallIcon(R.drawable.notification_icon)
					.setContentTitle(
							this.context.getResources().getString(
									R.string.app_name))
					.setContentText("There is an update available for Copy It!");

			PendingIntent notifyIntent = PendingIntent.getActivity(
					this.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			nBuilder.setContentIntent(notifyIntent);

			NotificationManager notificationManager = (NotificationManager) this.context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			notificationManager.notify(1, nBuilder.build());
		}

		super.onPostExecute(result);
	}

}
