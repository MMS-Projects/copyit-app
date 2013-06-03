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
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class CopyItTask extends ServerApiUiTask<String, Void, Boolean> {
	protected Change historyChangeType = Change.PUSHED;

	private String content;
	private SQLiteDatabase database;

	public CopyItTask(Context context, ServerApi api) {
		super(context, api);

		this.setProgressDialigMessage(context.getResources().getString(
				R.string.text_content_pushing));
	}

	@Override
	protected Boolean doInBackgroundWithException(String... params)
			throws Exception {
		this.content = params[0];

		HistoryItemsDbHelper dbHelper = new HistoryItemsDbHelper(context);
		this.database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(HistoryContract.ItemEntry.COLUMN_NAME_CONTENT, this.content);
		values.put(HistoryContract.ItemEntry.COLUMN_NAME_DATE,
				new Date().getTime());
		values.put(HistoryContract.ItemEntry.COLUMN_NAME_CHANGE,
				this.historyChangeType.toString());

		this.database
				.insert(HistoryContract.ItemEntry.TABLE_NAME, null, values);
		this.database.close();

		return new ClipboardContentEndpoint(api).update(this.content);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		try {
			this.doExceptionCheck();

			if (result) {
				Toast.makeText(
						this.context,
						this.context.getResources().getString(
								R.string.text_content_pushed, this.content),
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			Toast.makeText(
					this.context,
					this.context.getResources().getString(
							R.string.error_general, e.getLocalizedMessage()),
					Toast.LENGTH_LONG).show();
		}

		super.onPostExecute(result);
	}
}