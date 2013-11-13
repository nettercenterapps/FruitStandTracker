package netter.uni.database;

import android.content.ContentValues;
import android.database.Cursor;

public class School {

	public static final String TABLE_NAME = "School";
	// columns in School table
	public static final String COL_ID = "_id";
	public static final String COL_NAME = "name";

	// make sure order of fields in consistent
	protected static final String[] FIELDS = { COL_ID,
			COL_NAME };

	//SQL query to create the School table
	protected static final String CREATE_TABLE = 
			"CREATE TABLE " + TABLE_NAME + "("
		            + COL_ID + " INTEGER PRIMARY KEY,"
		            + COL_NAME + " TEXT"
		            + ")";

	// fields corresponding to database columns
	public long id = -1; // default is -1 to create a new auto-incremented staff
							// member
	public String name;

	// create a School object with a specified id (for updates)
	public School(long id, String name) {
		this.id = id;
		this.name = name;
	}

	// create a School object with the default id (-1)
	public School(String name) {
		this.name = name;
	}

	// create a School object with info from the database
	protected School(final Cursor cursor) {
		// these indices must be in the same order as in FIELDS above
		this.id = cursor.getLong(0);
		this.name = cursor.getString(1);
	}

	// get all the fields in a ContentValues object, to be put in the database
	protected ContentValues getContent() {
		final ContentValues values = new ContentValues();
		// ID is not included here, as it will be autoincremented
		values.put(COL_NAME, name);

		return values;
	}
}
