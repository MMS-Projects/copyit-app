package net.mms_projects.copy_it.activities;

import java.util.List;

import net.mms_projects.copy_it.AndroidClipboardUtils;
import net.mms_projects.copy_it.ClipboardUtils;
import net.mms_projects.copy_it.R;
import net.mms_projects.copy_it.adapters.HistoryAdapter;
import net.mms_projects.copy_it.databases.HistoryItemsDbHelper;
import net.mms_projects.copy_it.models.HistoryContract;
import net.mms_projects.copy_it.models.HistoryItem;
import net.mms_projects.copy_it.models.HistoryListFactory;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;

public class HistoryActivity extends SherlockListActivity {

	protected ActionMode actionMode;
	private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

		// Called when the action mode is created; startActionMode() was called
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Inflate a menu resource providing context menu items
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.cab_history, menu);
			return true;
		}

		// Called each time the action mode is shown. Always called after
		// onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // Return false if nothing is done
		}

		// Called when the user selects a contextual menu item
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.cab_put_in_clipboard:

				ClipboardUtils clipboard = new AndroidClipboardUtils(
						HistoryActivity.this);
				clipboard.setContent(selectedText);

				Toast.makeText(
						HistoryActivity.this,
						HistoryActivity.this.getResources().getString(
								R.string.history_content_set, selectedText),
						Toast.LENGTH_LONG).show();

				mode.finish(); // Action picked, so close the CAB
				return true;
			default:
				return false;
			}
		}

		// Called when the user exits the action mode
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			actionMode = null;
		}
	};

	private String selectedText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ("net.mms_projects.copy_it.history.clear".equals(this.getIntent()
				.getAction())) {
			new ListClear(this).execute();

			finish();

			return;
		}

		setTheme(R.style.AppTheme);
		setContentView(R.layout.activity_history);

		ListView list = (ListView) this.findViewById(android.R.id.list);
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setItemsCanFocus(false);

		new ListLoader(this).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.history, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.action_history_clear:
			intent = new Intent(this, this.getClass());
			intent.setAction("net.mms_projects.copy_it.history.clear");
			this.startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		SparseBooleanArray checked = l.getCheckedItemPositions();
		boolean hasCheckedElement = false;
		for (int i = 0; i < checked.size() && !hasCheckedElement; i++) {
			hasCheckedElement = checked.valueAt(i);
		}

		if (hasCheckedElement) {
			TextView contentView = (TextView) v
					.findViewById(R.id.history_item_content);
			this.selectedText = contentView.getText().toString();

			if (actionMode == null) {
				actionMode = this.startActionMode(actionModeCallback);
				actionMode.invalidate();
			} else {
				actionMode.invalidate();
			}
		} else {
			if (actionMode != null) {
				actionMode.finish();
			}
		}
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
					.findViewById(android.R.id.list);

			List<HistoryItem> items = HistoryListFactory.buildList(cursor);
			ListAdapter adapter = new HistoryAdapter(this.activity, items);
			list.setAdapter(adapter);

			this.database.close();
		}
	}

	public class ListClear extends AsyncTask<Void, Void, Void> {
		private Activity activity;

		public ListClear(Activity activity) {
			this.activity = activity;
		}

		@Override
		protected Void doInBackground(Void... params) {
			HistoryItemsDbHelper dbHelper = new HistoryItemsDbHelper(
					this.activity);
			dbHelper.clearDatabase(dbHelper.getReadableDatabase());
			dbHelper.onCreate(dbHelper.getReadableDatabase());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			Toast.makeText(this.activity,
					this.activity.getString(R.string.text_history_cleared),
					Toast.LENGTH_LONG).show();
		}
	}

}
