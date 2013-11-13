package netter.uni.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class FruitStand {
	double defaultPrice = 0.5;

	public static final String TABLE_NAME = "FruitStand";
	// columns in FruitStand table
	public static final String COL_ID = "_id";
	public static final String COL_SCHOOL = "school";
	public static final String COL_DATE = "date";
	public static final String COL_TEMPERATURE = "temperature";
	public static final String COL_WEATHER = "weather";
	public static final String COL_INITIAL_CASH = "initial_cash";
	public static final String COL_STAND_COST = "stand_cost";
	public static final String COL_SMOOTHIE_COST = "smoothie_cost";
	public static final String COL_ADDITIONAL_COST = "additional_cost";

	// make sure order of fields in consistent
	protected static final String[] FIELDS = { COL_ID, COL_SCHOOL, COL_DATE,
			COL_TEMPERATURE, COL_WEATHER, COL_INITIAL_CASH, COL_STAND_COST,
			COL_SMOOTHIE_COST, COL_ADDITIONAL_COST };

	//SQL query to create the FruitStand table
	protected static final String CREATE_TABLE = 
			"CREATE TABLE " + TABLE_NAME + "("
		            + COL_ID + " INTEGER PRIMARY KEY,"
		            + COL_SCHOOL + " TEXT NOT NULL,"
		            + COL_DATE + " TEXT NOT NULL,"
		            + COL_TEMPERATURE + " INTEGER,"
		            + COL_WEATHER + " TEXT NOT NULL,"
		            + COL_INITIAL_CASH + " REAL,"
		            + COL_STAND_COST + " REAL,"
		            + COL_SMOOTHIE_COST + " REAL,"
		            + COL_ADDITIONAL_COST + " REAL"
		            + ")";
	
	// fields corresponding to database columns
	public long id = -1; // default is -1 to create a new auto-incremented fruit
							// stand
	public String school;
	public String date;
	public int temperature;
	public String weather;
	public double initial_cash;
	public double stand_cost;
	public double smoothie_cost;
	public double additional_cost;

	// create a FruitStand object with a specified id (for updates)
	public FruitStand(long id, String school, String date, int temperature,
			String weather, double initial_cash, double stand_cost,
			double smoothie_cost, double additional_cost) {
		this.id = id;
		this.school = school;
		this.date = date;
		this.temperature = temperature;
		this.weather = weather;
		this.initial_cash = initial_cash;
		this.stand_cost = stand_cost;
		this.smoothie_cost = smoothie_cost;
		this.additional_cost = additional_cost;
	}

	// create a FruitStand object with the default id (-1)
	public FruitStand(String school, String date, int temperature,
			String weather, double initial_cash, double stand_cost,
			double smoothie_cost, double additional_cost) {
		this.school = school;
		this.date = date;
		this.temperature = temperature;
		this.weather = weather;
		this.initial_cash = initial_cash;
		this.stand_cost = stand_cost;
		this.smoothie_cost = smoothie_cost;
		this.additional_cost = additional_cost;
	}

	// create a FruitStand object with info from the database
	protected FruitStand(final Cursor cursor) {
		// these indices must be in the same order as in FIELDS above
		this.id = cursor.getLong(0);
		this.school = cursor.getString(1);
		this.date = cursor.getString(2);
		this.temperature = cursor.getInt(3);
		this.weather = cursor.getString(4);
		this.initial_cash = cursor.getDouble(5);
		this.stand_cost = cursor.getDouble(6);
		this.smoothie_cost = cursor.getDouble(7);
		this.additional_cost = cursor.getDouble(8);
	}

	// get all the fields in a ContentValues object, to be put in the database
	protected ContentValues getContent() {
		final ContentValues values = new ContentValues();
		// ID is not included here, as it will be autoincremented
		values.put(COL_SCHOOL, school);
		values.put(COL_DATE, date);
		values.put(COL_TEMPERATURE, temperature);
		values.put(COL_WEATHER, weather);
		values.put(COL_INITIAL_CASH, initial_cash);
		values.put(COL_STAND_COST, stand_cost);
		values.put(COL_SMOOTHIE_COST, smoothie_cost);
		values.put(COL_ADDITIONAL_COST, additional_cost);

		return values;
	}

	// Use this to get the Totals information for this fruit stand
	public Totals getTotals(Context context) {
		if (id > -1) {
			return DatabaseHandler.getInstance(context).getTotalsByFruitStand(
					id);
		} else {
			return null;
		}
	}

	// Adds the totals for this fruit stand, returns true if successful
	public boolean addTotals(Context context, double cost, double revenue,
			double final_cash, int math_override, String message) {
		Totals totals = new Totals(id, cost, revenue, final_cash, math_override, message);
		return DatabaseHandler.getInstance(context).putTotals(totals);
	}

	// Use this to get the StaffMember information for this fruit stand
	public StaffMember[] getStaffMembers(Context context) {
		if (id > -1) {
			return DatabaseHandler.getInstance(context)
					.getStaffMembersByFruitStand(id);
		} else {
			return null;
		}
	}

	// Adds a StaffMember for this fruit stand, returns true if successful
	public boolean addStaffMember(Context context, String name,
			boolean is_volunteer) {
		StaffMember staffMember = new StaffMember(id, name, is_volunteer);
		return DatabaseHandler.getInstance(context).putStaffMember(staffMember);
	}

	// Use this to get the StartInventoryItem information for this fruit stand
	public StartInventoryItem[] getStartInventoryItems(Context context) {
		if (id > -1) {
			return DatabaseHandler.getInstance(context)
					.getStartInventoryItemsByFruitStand(id);
		} else {
			return null;
		}
	}

	// Adds a StartInventoryItem for this fruit stand, returns true if
	// successful
	public boolean addStartInventoryItem(Context context, String item_name,
			int count) {
		StartInventoryItem startInventoryItem = new StartInventoryItem(id,
				item_name, count);
		return DatabaseHandler.getInstance(context).putStartInventoryItem(
				startInventoryItem);
	}

	// Use this to get the ProcessedInventoryItem information for this fruit
	// stand
	public ProcessedInventoryItem[] getProcessedInventoryItems(Context context) {
		if (id > -1) {
			return DatabaseHandler.getInstance(context)
					.getProcessedInventoryItemsByFruitStand(id);
		} else {
			return null;
		}
	}

	// Adds a ProcessedInventoryItem for this fruit stand, returns true if
	// successful
	public boolean addProcessedInventoryItem(Context context, String item_name,
			int count, double price) {
		ProcessedInventoryItem processedInventoryItem = new ProcessedInventoryItem(
				id, item_name, count, price);
		return DatabaseHandler.getInstance(context).putProcessedInventoryItem(
				processedInventoryItem);
	}
	public boolean updateProcessedInventoryItem( Context context,int pItemid, String item_name,
			int count, double price) {
		ProcessedInventoryItem processedInventoryItem = new ProcessedInventoryItem(pItemid,
				id, item_name, count, price);
		return DatabaseHandler.getInstance(context).putProcessedInventoryItem(
				processedInventoryItem);
	}

	// Use this to get the EndInventoryItem information for this fruit stand
	public EndInventoryItem[] getEndInventoryItems(Context context) {
		if (id > -1) {
			return DatabaseHandler.getInstance(context)
					.getEndInventoryItemsByFruitStand(id);
		} else {
			return null;
		}
	}

	// Adds a EndInventoryItem for this fruit stand, returns true if successful
	public boolean addEndInventoryItem(Context context, String item_name,
			int count) {
		EndInventoryItem endInventoryItem = new EndInventoryItem(id, item_name,
				count);
		return DatabaseHandler.getInstance(context).putEndInventoryItem(
				endInventoryItem);
	}

	// Use this to get the Purchase information for this fruit stand
	public Purchase[] getPurchases(Context context) {
		if (id > -1) {
			return DatabaseHandler.getInstance(context)
					.getPurchasesByFruitStand(id);
		} else {
			return null;
		}
	}

	// Adds a Purchase for this fruit stand, returns true if successful
	public boolean addPurchase(Context context, String item_name, int transaction_num,
			int num_coupons, int num_tradeins, double amount_cash,
			String customer) {
		Purchase purchase = new Purchase(id, item_name, transaction_num,
				num_coupons, num_tradeins, amount_cash, customer);
		return DatabaseHandler.getInstance(context).putPurchase(purchase);
	}
}
