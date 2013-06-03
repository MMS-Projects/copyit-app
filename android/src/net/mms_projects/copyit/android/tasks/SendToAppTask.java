package net.mms_projects.copyit.android.tasks;

import java.util.Date;

import net.mms_projects.copy_it.R;
import net.mms_projects.copy_it.databases.HistoryItemsDbHelper;
import net.mms_projects.copy_it.models.HistoryContract;
import net.mms_projects.copy_it.models.HistoryItem.Change;
import net.mms_projects.copyit.api.ServerApi;
import net.mms_projects.copyit.api.endpoints.ClipboardContentEndpoint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class SendToAppTask extends ServerApiUiTask<Void, Void, String> {

	private SQLiteDatabase database;

	public SendToAppTask(Context context, ServerApi api) {
		super(context, api);

		this.setProgressDialigMessage(context.getResources().getString(
				R.string.text_content_pulling));
		this.setUseProgressDialog(true);
	}

	@Override
	protected String doInBackgroundWithException(Void... params)
			throws Exception {
		HistoryItemsDbHelper dbHelper = new HistoryItemsDbHelper(context);
		this.database = dbHelper.getWritableDatabase();

		return new ClipboardContentEndpoint(api).get();
	}

	@Override
	protected void onPostExecute(String content) {
		try {
			this.doExceptionCheck();

			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, content);
			sendIntent.setType("text/plain");

			this.context.startActivity(Intent.createChooser(
					sendIntent,
					this.context.getResources().getString(
							R.string.dialog_title_select_send_app)));

			ContentValues values = new ContentValues();
			values.put(HistoryContract.ItemEntry.COLUMN_NAME_CONTENT, content);
			values.put(HistoryContract.ItemEntry.COLUMN_NAME_DATE,
					new Date().getTime());
			values.put(HistoryContract.ItemEntry.COLUMN_NAME_CHANGE,
					Change.SEND_TO_APP.toString());

			SharedPreferences prefences = PreferenceManager
					.getDefaultSharedPreferences(this.context);
			if (prefences.getBoolean("history.tracking_state", true)) {
				this.database.insert(HistoryContract.ItemEntry.TABLE_NAME,
						null, values);
			}
			this.database.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			Toast.makeText(
					this.context,
					this.context.getResources().getString(
							R.string.error_general, e.getLocalizedMessage()),
					Toast.LENGTH_LONG).show();
		}

		super.onPostExecute(content);
	}
}