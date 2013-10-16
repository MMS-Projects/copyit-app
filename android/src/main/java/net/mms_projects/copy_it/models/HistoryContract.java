package net.mms_projects.copy_it.models;

import android.provider.BaseColumns;

public class HistoryContract {
	public static abstract class ItemEntry implements BaseColumns {
		public static final String TABLE_NAME = "items";
		public static final String COLUMN_NAME_CONTENT = "content";
		public static final String COLUMN_NAME_DATE = "date";
		public static final String COLUMN_NAME_CHANGE = "change";
	}
}