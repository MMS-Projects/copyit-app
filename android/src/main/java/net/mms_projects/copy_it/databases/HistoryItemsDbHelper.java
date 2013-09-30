package net.mms_projects.copy_it.databases;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.mms_projects.copy_it.models.HistoryContract;
import net.mms_projects.copy_it.models.HistoryItem;
import net.mms_projects.copy_it.models.HistoryListFactory;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryItemsDbHelper extends SQLiteOpenHelper {

	// If you change the database schema, you must increment the database
	// version.
	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "history.db";

	private static final String TEXT_TYPE = " TEXT";
	private static final String NUMERIC_TYPE = " NUMERIC";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
			+ HistoryContract.ItemEntry.TABLE_NAME + " ("
			+ HistoryContract.ItemEntry._ID + " INTEGER PRIMARY KEY,"
			+ HistoryContract.ItemEntry.COLUMN_NAME_CONTENT + TEXT_TYPE
			+ COMMA_SEP + HistoryContract.ItemEntry.COLUMN_NAME_DATE
			+ NUMERIC_TYPE + COMMA_SEP
			+ HistoryContract.ItemEntry.COLUMN_NAME_CHANGE + TEXT_TYPE + " )";

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
			+ HistoryContract.ItemEntry.TABLE_NAME;
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public HistoryItemsDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		String[] projection = { HistoryContract.ItemEntry.COLUMN_NAME_CONTENT,
				HistoryContract.ItemEntry.COLUMN_NAME_DATE,
				HistoryContract.ItemEntry.COLUMN_NAME_CHANGE };

		Cursor cursor = database.query(HistoryContract.ItemEntry.TABLE_NAME,
				projection, null, null, null, null, null);

		List<HistoryItem> items = HistoryListFactory.buildList(cursor);

		this.clearDatabase(database);
		this.onCreate(database);

		for (HistoryItem item : items) {
			log.debug("Upgrading item: {}, " + item.date);

			ContentValues values = new ContentValues();
			values.put(HistoryContract.ItemEntry.COLUMN_NAME_CONTENT,
					item.content);
			values.put(HistoryContract.ItemEntry.COLUMN_NAME_DATE,
					item.date.getTime());
			values.put(HistoryContract.ItemEntry.COLUMN_NAME_CHANGE,
					item.change.toString());

			database.insert(HistoryContract.ItemEntry.TABLE_NAME, null, values);
		}
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
	
	public void clearDatabase(SQLiteDatabase database) {
		database.execSQL(SQL_DELETE_ENTRIES);
	}

}
