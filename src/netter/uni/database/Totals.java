package netter.uni.database;

import android.content.ContentValues;
import android.database.Cursor;

public class Totals {

	public static final String TABLE_NAME = "Totals";
	// columns in Totals table
	public static final String COL_ID = "_id";

	public static final String COL_FRUIT_STAND_ID = "fruit_stand_id";
	public static final String COL_COST = "cost";
	public static final String COL_REVENUE = "revenue";
	public static final String COL_FINAL_CASH = "final_cash";
	public static final String MATH_OVERRIDE = "math_override";
	public static final String MESSAGE = "message";


	// make sure order of fields in consistent
	protected static final String[] FIELDS = { COL_ID, COL_FRUIT_STAND_ID,
			COL_COST, COL_REVENUE, COL_FINAL_CASH, MATH_OVERRIDE, MESSAGE };

	//SQL query to create the Totals table
	protected static final String CREATE_TABLE = 
			"CREATE TABLE " + TABLE_NAME + "("
		            + COL_ID + " INTEGER PRIMARY KEY,"
		            + COL_FRUIT_STAND_ID + " INTEGER UNIQUE,"
		            + COL_COST + " REAL,"
		            + COL_REVENUE + " REAL,"
		            + COL_FINAL_CASH + " REAL,"
		            + MATH_OVERRIDE + " INTEGER,"
		            + MESSAGE + " TEXT,"
		            +"FOREIGN KEY("+ COL_FRUIT_STAND_ID + ") REFERENCES FruitStand("+COL_ID+ ") ON DELETE CASCADE"
		            + ")";

	// fields corresponding to database columns
	public long id = -1; // default is -1 to create a new auto-incremented
							// totals
	public long fruit_stand_id;
	public double cost;
	public double revenue;
	public double final_cash;
	public int math_override;
	public String message;

	// create a Totals object with a specified id (for updates)
	protected Totals(long id, long fruit_stand_id, double cost, double revenue,
			double final_cash, int math_override, String message) {
		this.id = id;
		this.fruit_stand_id = fruit_stand_id;
		this.cost = cost;
		this.revenue = revenue;
		this.final_cash = final_cash;
		this.math_override = math_override;
		this.message = message;
	}

	// create a Totals object with the default id (-1)
	protected Totals(long fruit_stand_id, double cost, double revenue,
			double final_cash, int math_override, String message) {
		this.fruit_stand_id = fruit_stand_id;
		this.cost = cost;
		this.revenue = revenue;
		this.final_cash = final_cash;
		this.math_override = math_override;
		this.message = message;
	}

	// create a Totals object with info from the database
	protected Totals(final Cursor cursor) {
		// these indices must be in the same order as in FIELDS above
		this.id = cursor.getLong(0);
		this.fruit_stand_id = cursor.getLong(1);
		this.cost = cursor.getDouble(2);
		this.revenue = cursor.getDouble(3);
		this.final_cash = cursor.getDouble(4);
		this.math_override = cursor.getInt(5);
		this.message = cursor.getString(6);
	}

	// get all the fields in a ContentValues object, to be put in the database
	protected ContentValues getContent() {
		final ContentValues values = new ContentValues();
		// ID is not included here, as it will be autoincremented
		values.put(COL_FRUIT_STAND_ID, fruit_stand_id);
		values.put(COL_COST, cost);
		values.put(COL_REVENUE, revenue);
		values.put(COL_FINAL_CASH, final_cash);
		values.put(MATH_OVERRIDE, math_override);
		values.put(MESSAGE, message);
		return values;
	}
}
