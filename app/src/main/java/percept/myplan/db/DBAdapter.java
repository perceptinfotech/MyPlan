package percept.myplan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBAdapter {

	private static final String TAG = "[DBAdapter]";

	private static final String KEY_ROWID = "rowid";
	private static final String KEY_USER_ID = "user_id";

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "MYPLAN_DB";
	private static final String ALARM_TABLE = "Alarm";

	private static final String TABLE_CREATE_ALARM = "create table "
			+ ALARM_TABLE + "( " + KEY_ROWID
			+ " integer primary key autoincrement," + KEY_USER_ID
			+ " integer not null,"
			+ " text);";

	private DatabaseHelper DBhelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		DBhelper = new DatabaseHelper(ctx);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TABLE_CREATE_ALARM);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREATE_ALARM);
			onCreate(db);
		}
	}

	public DBAdapter open() throws SQLiteException {
		db = DBhelper.getWritableDatabase();
		return this;
	}

	public boolean isCreated() {
		if (db != null) {
			return db.isOpen();
		}

		return false;
	}

	public boolean isOpen() {
		return db.isOpen();
	}

	public void close() {
		DBhelper.close();
		db.close();
	}



	public int deleteUser() {
		return db.delete(ALARM_TABLE, null, null);
	}

}
