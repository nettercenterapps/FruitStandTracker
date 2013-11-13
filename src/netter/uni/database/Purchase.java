package netter.uni.database;

import android.content.ContentValues;
import android.database.Cursor;

public class Purchase {

	public static final String TABLE_NAME = "Purchase";
	// columns in Purchase table
	public static final String COL_ID = "_id";

	public static final String COL_FRUIT_STAND_ID = "fruit_stand_id";
	public static final String COL_ITEM_NAME = "item_name";
	public static final String TRANSACTION_NUM = "transaction_num";
	public static final String COL_NUM_COUPONS = "num_coupons";
	public static final String COL_NUM_TRADEINS = "num_tradeins";
	public static final String COL_AMOUNT_CASH = "amount_cash";
	public static final String COL_CUSTOMER = "customer";

	// make sure order of fields in consistent
	protected static final String[] FIELDS = { COL_ID, COL_FRUIT_STAND_ID,
			COL_ITEM_NAME, TRANSACTION_NUM, COL_NUM_COUPONS, COL_NUM_TRADEINS,
			COL_AMOUNT_CASH, COL_CUSTOMER };

	//SQL query to create the Purchase table
	protected static final String CREATE_TABLE = 
			"CREATE TABLE " + TABLE_NAME + "("
		            + COL_ID + " INTEGER PRIMARY KEY,"
		            + COL_FRUIT_STAND_ID + " INTEGER,"
		            + COL_ITEM_NAME + " TEXT,"
		            + TRANSACTION_NUM + " INTEGER,"
		            + COL_NUM_COUPONS + " INTEGER,"
		            + COL_NUM_TRADEINS + " INTEGER,"
		            + COL_AMOUNT_CASH + " REAL,"
		            + COL_CUSTOMER + " TEXT,"
		            +"FOREIGN KEY("+ COL_FRUIT_STAND_ID + ") REFERENCES FruitStand("+COL_ID+ ") ON DELETE CASCADE"
		            + ")";

	// fields corresponding to database columns
	public long id = -1; // default is -1 to create a new auto-incremented
							// Purchase
	public long fruit_stand_id;
	public String item_name;
	public int transaction_num;
	public int num_coupons;
	public int num_tradeins;
	public double amount_cash;
	public String customer;

	// create a Purchase object with a specified id (for updates)
	protected Purchase(long id, long fruit_stand_id, String item_name,
			int transaction_num, int num_coupons, int num_tradeins, double amount_cash,
			String customer) {
		this.id = id;
		this.fruit_stand_id = fruit_stand_id;
		this.item_name = item_name;
		this.transaction_num = transaction_num;
		this.num_coupons = num_coupons;
		this.num_tradeins = num_tradeins;
		this.amount_cash = amount_cash;
		this.customer = customer;
	}

	// create a Purchase object with the default id (-1)
	protected Purchase(long fruit_stand_id, String item_name, int transaction_num,
			int num_coupons, int num_tradeins, double amount_cash,
			String customer) {
		this.fruit_stand_id = fruit_stand_id;
		this.item_name = item_name;
		this.transaction_num = transaction_num;
		this.num_coupons = num_coupons;
		this.num_tradeins = num_tradeins;
		this.amount_cash = amount_cash;
		this.customer = customer;
	}

	// create a Purchase object with info from the database
	protected Purchase(final Cursor cursor) {
		// these indices must be in the same order as in FIELDS above
		this.id = cursor.getLong(0);
		this.fruit_stand_id = cursor.getLong(1);
		this.item_name = cursor.getString(2);
		this.transaction_num = cursor.getInt(3);
		this.num_coupons = cursor.getInt(4);
		this.num_tradeins = cursor.getInt(5);
		this.amount_cash = cursor.getDouble(6);
		this.customer = cursor.getString(7);
	}

	// get all the fields in a ContentValues object, to be put in the database
	protected ContentValues getContent() {
		final ContentValues values = new ContentValues();
		// ID is not included here, as it will be autoincremented
		values.put(COL_FRUIT_STAND_ID, fruit_stand_id);
		values.put(COL_ITEM_NAME, item_name);
		values.put(TRANSACTION_NUM, transaction_num);
		values.put(COL_NUM_COUPONS, num_coupons);
		values.put(COL_NUM_TRADEINS, num_tradeins);
		values.put(COL_AMOUNT_CASH, amount_cash);
		values.put(COL_CUSTOMER, customer);

		return values;
	}
}
