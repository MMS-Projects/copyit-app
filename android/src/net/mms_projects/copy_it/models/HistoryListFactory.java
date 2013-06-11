package net.mms_projects.copy_it.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.mms_projects.copy_it.models.HistoryItem.Change;
import android.database.Cursor;

public class HistoryListFactory {

	public static List<HistoryItem> buildList(Cursor cursor) {
		List<HistoryItem> items = new ArrayList<HistoryItem>();
		while (cursor.moveToNext()) {
			String content = cursor
					.getString(cursor
							.getColumnIndex(HistoryContract.ItemEntry.COLUMN_NAME_CONTENT));
			Date date = new Date(
					cursor.getLong(cursor
							.getColumnIndex(HistoryContract.ItemEntry.COLUMN_NAME_DATE)));
			Change change = Change
					.valueOf(cursor.getString(cursor
							.getColumnIndex(HistoryContract.ItemEntry.COLUMN_NAME_CHANGE)));
			items.add(new HistoryItem(content, date, change));

		}
		return items;
	}
}
