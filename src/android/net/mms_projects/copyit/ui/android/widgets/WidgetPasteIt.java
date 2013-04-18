package net.mms_projects.copyit.ui.android.widgets;

import net.mms_projects.copy_it.R;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.widget.RemoteViews;

public class WidgetPasteIt extends WidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_pasteit);

			this.addPasteItAction(views, context);

			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

}
