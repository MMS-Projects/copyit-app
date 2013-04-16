package net.mms_projects.copyit.ui.android.widgets;

import net.mms_projects.copy_it.R;
import net.mms_projects.copyit.ui.android.MainActivity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetGeneral extends WidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_general);

			Intent intentApp = new Intent(context, MainActivity.class);
			PendingIntent pendingIntentApp = PendingIntent.getActivity(context,
					0, intentApp, 0);

			views.setOnClickPendingIntent(R.id.button_app, pendingIntentApp);

			this.addCopyItAction(views, context);
			this.addPasteItAction(views, context);

			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
}
