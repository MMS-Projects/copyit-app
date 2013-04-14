package net.mms_projects.copyit.ui.android;

import java.util.UUID;

import net.mms_projects.copyit.ClipboardUtils;
import net.mms_projects.copyit.R;
import net.mms_projects.copyit.android.tasks.CopyItTask;
import net.mms_projects.copyit.android.tasks.PasteItTask;
import net.mms_projects.copyit.api.ServerApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

	static public String ACTION_COPYIT = "copyit";
	static public String ACTION_PASTEIT = "pasteit";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			Intent intentApp = new Intent(context, MainActivity.class);
			PendingIntent pendingIntentApp = PendingIntent.getActivity(context,
					0, intentApp, 0);

			Intent intentCopyIt = new Intent(context, WidgetProvider.class);
			intentCopyIt.setAction(ACTION_COPYIT);
			PendingIntent pendingIntentCopyIt = PendingIntent.getBroadcast(
					context, 0, intentCopyIt, 0);

			Intent intentPasteIt = new Intent(context, WidgetProvider.class);
			intentPasteIt.setAction(ACTION_PASTEIT);
			PendingIntent pendingIntentPasteIt = PendingIntent.getBroadcast(
					context, 0, intentPasteIt, 0);

			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget);
			views.setOnClickPendingIntent(R.id.button_app, pendingIntentApp);
			views.setOnClickPendingIntent(R.id.button_copy, pendingIntentCopyIt);
			views.setOnClickPendingIntent(R.id.button_paste,
					pendingIntentPasteIt);

			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// v1.5 fix that doesn't call onDelete Action
		final String action = intent.getAction();
		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			final int appWidgetId = intent.getExtras().getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				this.onDeleted(context, new int[] { appWidgetId });
			}
		} else {
			ClipboardUtils clipboard = new ClipboardUtils(context);
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(context);

			if (!intent.getAction().equals(ACTION_COPYIT)
					&& !intent.getAction().equals(ACTION_PASTEIT)) {
				super.onReceive(context, intent);
				return;
			}

			if (preferences.getString("device.id", null) == null) {
				Intent loginIntent = new Intent(context, LoginActivity.class);
				loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(loginIntent);
				return;
			}

			ServerApi api = new ServerApi();
			api.deviceId = UUID.fromString(preferences.getString("device.id",
					null));
			api.devicePassword = preferences.getString("device.password", null);
			api.apiUrl = preferences.getString("server.baseurl", context
					.getResources().getString(R.string.default_baseurl));

			if (intent.getAction().equals(ACTION_COPYIT)) {
				CopyItTask task = new CopyItTask(context, api);
				task.execute(clipboard.getText());
			} else if (intent.getAction().equals(ACTION_PASTEIT)) {
				PasteItTask task = new PasteItTask(context, api);
				task.execute();
			}
			super.onReceive(context, intent);
		}
	}

}