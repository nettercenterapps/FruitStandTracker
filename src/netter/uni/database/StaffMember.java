package netter.uni.database;

import android.content.ContentValues;
import android.database.Cursor;

public class StaffMember {

	public static final String TABLE_NAME = "StaffMember";
	// columns in StaffMember table
	public static final String COL_ID = "_id";
	public static final String COL_FRUIT_STAND_ID = "fruit_stand_id";
	public static final String COL_NAME = "name";
	public static final String COL_IS_VOLUNTEER = "is_volunteer";

	// make sure order of fields in consistent
	protected static final String[] FIELDS = { COL_ID, COL_FRUIT_STAND_ID,
			COL_NAME, COL_IS_VOLUNTEER };

	//SQL query to create the StaffMember table
	protected static final String CREATE_TABLE = 
			"CREATE TABLE " + TABLE_NAME + "("
		            + COL_ID + " INTEGER PRIMARY KEY,"
		            + COL_FRUIT_STAND_ID + " INTEGER,"
		            + COL_NAME + " TEXT,"
		            + COL_IS_VOLUNTEER + " INTEGER,"
		            +"FOREIGN KEY("+ COL_FRUIT_STAND_ID + ") REFERENCES FruitStand("+COL_ID+ ") ON DELETE CASCADE"
		            + ")";

	// fields corresponding to database columns
	public long id = -1; // default is -1 to create a new auto-incremented staff
							// member
	public long fruit_stand_id;
	public String name;
	public boolean is_volunteer;

	// create a StaffMember object with a specified id (for updates)
	protected StaffMember(long id, long fruit_stand_id, String name,
			boolean is_volunteer) {
		this.id = id;
		this.fruit_stand_id = fruit_stand_id;
		this.name = name;
		this.is_volunteer = is_volunteer;
	}

	// create a StaffMember object with the default id (-1)
	protected StaffMember(long fruit_stand_id, String name, boolean is_volunteer) {
		this.fruit_stand_id = fruit_stand_id;
		this.name = name;
		this.is_volunteer = is_volunteer;
	}

	// create a StaffMember object with info from the database
	protected StaffMember(final Cursor cursor) {
		// these indices must be in the same order as in FIELDS above
		this.id = cursor.getLong(0);
		this.fruit_stand_id = cursor.getLong(1);
		this.name = cursor.getString(2);
		switch (cursor.getInt(3)) {
		case 0:
			this.is_volunteer = false;
			break;
		case 1:
			this.is_volunteer = true;
			break;
		default:
			this.is_volunteer = false; // should never happen
			break;
		}
	}

	// get all the fields in a ContentValues object, to be put in the database
	protected ContentValues getContent() {
		final ContentValues values = new ContentValues();
		// ID is not included here, as it will be autoincremented
		values.put(COL_FRUIT_STAND_ID, fruit_stand_id);
		values.put(COL_NAME, name);

		if (is_volunteer) {
			values.put(COL_IS_VOLUNTEER, 1);
		} else {
			values.put(COL_IS_VOLUNTEER, 0);
		}

		return values;
	}
}
