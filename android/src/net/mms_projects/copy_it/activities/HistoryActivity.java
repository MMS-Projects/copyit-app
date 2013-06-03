package net.mms_projects.copy_it.activities;

import java.util.List;

import net.mms_projects.copy_it.R;
import net.mms_projects.copy_it.adapters.HistoryAdapter;
import net.mms_projects.copy_it.databases.HistoryItemsDbHelper;
import net.mms_projects.copy_it.models.HistoryContract;
import net.mms_projects.copy_it.models.HistoryItem;
import net.mms_projects.copy_it.models.HistoryListFactory;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.google.analytics.tracking.android.EasyTracker;

public class HistoryActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.AppTheme);
		setContentView(R.layout.activity_history);

		new ListLoader(this).execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		new ListLoader(this).execute();
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();

		EasyTracker.getInstance().activityStop(this);
	}

	public class ListLoader extends AsyncTask<Void, Void, Cursor> {

		private Activity activity;
		private SQLiteDatabase database;

		public ListLoader(Activity activity) {
			this.activity = activity;
			
			HistoryItemsDbHelper dbHelper = new HistoryItemsDbHelper(
					this.activity);
			this.database = dbHelper.getReadableDatabase();
		}

		@Override
		protected Cursor doInBackground(Void... params) {
			String[] projection = {
					HistoryContract.ItemEntry.COLUMN_NAME_CONTENT,
					HistoryContract.ItemEntry.COLUMN_NAME_DATE,
					HistoryContract.ItemEntry.COLUMN_NAME_CHANGE };

			String sortOrder = HistoryContract.ItemEntry.COLUMN_NAME_DATE
					+ " DESC";

			Cursor cursor = this.database.query(
					HistoryContract.ItemEntry.TABLE_NAME, projection, null,
					null, null, null, sortOrder);

			return cursor;
		}

		@Override
		protected void onPostExecute(Cursor cursor) {
			ListView list = (ListView) this.activity
					.findViewById(R.id.activity_history_list);

			List<HistoryItem> items = HistoryListFactory.buildList(cursor);
			ListAdapter adapter = new HistoryAdapter(this.activity, items);
			list.setAdapter(adapter);

			this.database.close();
		}

	}

}
