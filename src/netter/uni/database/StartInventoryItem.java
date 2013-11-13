package netter.uni.database;

import android.content.ContentValues;
import android.database.Cursor;

public class StartInventoryItem {

	public static final String TABLE_NAME = "StartInventoryItem";
	// columns in StartInventoryItem table
	public static final String COL_ID = "_id";
	public static final String COL_FRUIT_STAND_ID = "fruit_stand_id";
	public static final String COL_ITEM_NAME = "item_name";
	public static final String COL_COUNT = "count";

	// make sure order of fields in consistent
	protected static final String[] FIELDS = { COL_ID, COL_FRUIT_STAND_ID,
			COL_ITEM_NAME, COL_COUNT };

	//SQL query to create the StartInventoryItem table
	protected static final String CREATE_TABLE = 
			"CREATE TABLE " + TABLE_NAME + "("
		            + COL_ID + " INTEGER PRIMARY KEY,"
		            + COL_FRUIT_STAND_ID + " INTEGER,"
		            + COL_ITEM_NAME + " TEXT,"
		            + COL_COUNT + " INTEGER,"
		            + "UNIQUE(" + COL_FRUIT_STAND_ID + "," + COL_ITEM_NAME + ")"
		            +"FOREIGN KEY("+ COL_FRUIT_STAND_ID + ") REFERENCES FruitStand("+COL_ID+ ") ON DELETE CASCADE"
		            + ")";

	// fields corresponding to database columns
	public long id = -1; // default is -1 to create a new auto-incremented
							// StartInventoryItem
	public long fruit_stand_id;
	public String item_name;
	public int count;

	// create a StartInventoryItem object with a specified id (for updates)
	protected StartInventoryItem(long id, long fruit_stand_id,
			String item_name, int count) {
		this.id = id;
		this.fruit_stand_id = fruit_stand_id;
		this.item_name = item_name;
		this.count = count;
	}

	// create a StartInventoryItem object with the default id (-1)
	protected StartInventoryItem(long fruit_stand_id, String item_name,
			int count) {
		this.fruit_stand_id = fruit_stand_id;
		this.item_name = item_name;
		this.count = count;
	}

	// create a StartInventoryItem object with info from the database
	protected StartInventoryItem(final Cursor cursor) {
		// these indices must be in the same order as in FIELDS above
		this.id = cursor.getLong(0);
		this.fruit_stand_id = cursor.getLong(1);
		this.item_name = cursor.getString(2);
		this.count = cursor.getInt(3);
	}

	// get all the fields in a ContentValues object, to be put in the database
	protected ContentValues getContent() {
		final ContentValues values = new ContentValues();
		// ID is not included here, as it will be autoincremented
		values.put(COL_FRUIT_STAND_ID, fruit_stand_id);
		values.put(COL_ITEM_NAME, item_name);
		values.put(COL_COUNT, count);

		return values;
	}
}
