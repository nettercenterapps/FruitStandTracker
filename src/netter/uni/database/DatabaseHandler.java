package netter.uni.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static DatabaseHandler singleton;

	public static DatabaseHandler getInstance(final Context context) {
		if (singleton == null) {
			singleton = new DatabaseHandler(context);
		}
		return singleton;
	}

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Fruity";

	private DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creates all the necessary tables, only done once
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(FruitStand.CREATE_TABLE);
		db.execSQL(Totals.CREATE_TABLE);
		db.execSQL(StaffMember.CREATE_TABLE);
		db.execSQL(StartInventoryItem.CREATE_TABLE);
		db.execSQL(ProcessedInventoryItem.CREATE_TABLE);
		db.execSQL(EndInventoryItem.CREATE_TABLE);
		db.execSQL(Purchase.CREATE_TABLE);
		db.execSQL(School.CREATE_TABLE);
		db.execSQL("PRAGMA foreign_keys = ON;");
		prepopulateDatabase(db);
	}

	private void prepopulateDatabase(SQLiteDatabase db) {

		// prepopulate schools - not used as of 11/2013
		String[] schools = new String[] { "Gideon Elementary",
				"Locke Elementary", "Bryant Elementary", "Lea Elementary",
				"Alexander Wilson Elementary", "Huey Elementary",
				"Comegys Elementary", "Shaw Middle School",
				"Hardy Williams Middle School", "Pepper Middle School",
				"Freire Charter School", "Comm Tech HS",
				"High School of the Future", "Auden Reid HS",
				"Strawberry Mansion HS", "West Philadelphia HS", "Sayre HS",
				"University City HS", "Bartram HS", "Robeson HS" };

		for (String schoolName : schools) {
			School school = new School(schoolName);
			db.insert(School.TABLE_NAME, null, school.getContent());
		}
	}

	// does nothing for now
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	@Override
	public void onOpen(SQLiteDatabase db) {
		db.execSQL("PRAGMA foreign_keys = ON;");
		super.onOpen(db);
	}
	/*
	 * Methods for FruitStand
	 */

	// returns the last fruit stand in the table, which is the current one
	public synchronized FruitStand getCurrentFruitStand() {
		final SQLiteDatabase db = this.getReadableDatabase();
		final Cursor cursor = db.query(FruitStand.TABLE_NAME,
				FruitStand.FIELDS, null, null, null, null, null, null);
		if (cursor == null || cursor.isAfterLast()) {
			return null;
		}

		FruitStand item = null;
		if (cursor.moveToLast()) {
			item = new FruitStand(cursor);
		}
		cursor.close();

		return item;
	}

	// returns an array of all the FruitStands
	public synchronized FruitStand[] getAllFruitStands() {
		final SQLiteDatabase db = this.getReadableDatabase();
		final Cursor cursor = db.query(FruitStand.TABLE_NAME,
				FruitStand.FIELDS, null, null, null, null, null, null);
		if (cursor == null || cursor.isAfterLast()) {
			FruitStand[] temp = new FruitStand[0];
			return temp;
		}

		FruitStand[] items;
		if (cursor.moveToFirst()) {
			items = new FruitStand[cursor.getCount()];
			for (int i = 0; i < cursor.getCount(); i++) {
				FruitStand item = new FruitStand(cursor);
				items[i] = item;
				cursor.moveToNext();
			}
		} else {
			items = new FruitStand[0];
		}
		cursor.close();

		return items;

	}

	/*
	 * returns a FruitStand object with the data for the given id from the fruit
	 * stand table
	 */
	public synchronized FruitStand getFruitStand(final long id) {
		final SQLiteDatabase db = this.getReadableDatabase();
		final Cursor cursor = db.query(FruitStand.TABLE_NAME,
				FruitStand.FIELDS, FruitStand.COL_ID + " IS ?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor == null || cursor.isAfterLast()) {
			return null;
		}

		FruitStand item = null;
		if (cursor.moveToFirst()) {
			item = new FruitStand(cursor);
		}
		cursor.close();

		return item;
	}

	// Inserts or updates a FruitStand in the database
	public synchronized boolean putFruitStand(final FruitStand fruitStand) {
		boolean success = false;
		int result = 0;
		final SQLiteDatabase db = this.getWritableDatabase();
		// Update a FruitStand
		if (fruitStand.id > -1) {
			result = db.update(FruitStand.TABLE_NAME, fruitStand.getContent(),
					FruitStand.COL_ID + " IS ?",
					new String[] { String.valueOf(fruitStand.id) });
		}

		// If we were able to update
		if (result > 0) {
			success = true;
		} else {
			// Unable to update, insert instead
			final long id = db.insert(FruitStand.TABLE_NAME, null,
					fruitStand.getContent());

			if (id > -1) {
				// update the FruitStand object with its now valid ID
				fruitStand.id = id;
				success = true;
			}
		}

		return success;
	}

	/*
	 * deletes a FruitStand from the database returns the number of rows that
	 * were deleted (0 or 1)
	 */
	public synchronized int removeFruitStand(final FruitStand fruitStand) {
		final SQLiteDatabase db = this.getWritableDatabase();
		final int result = db.delete(FruitStand.TABLE_NAME, FruitStand.COL_ID
				+ " IS ?", new String[] { Long.toString(fruitStand.id) });

		return result;
	}

	/*
	 * Methods for School
	 */

	// returns an array of all the School names (with "Other" at the end)
	public synchronized String[] getSchoolsWithOther() {
		final SQLiteDatabase db = this.getReadableDatabase();
		final Cursor cursor = db.query(School.TABLE_NAME, School.FIELDS, null,
				null, null, null, null, null);
		if (cursor == null || cursor.isAfterLast()) {
			String[] temp = new String[1];
			temp[0] = "Other";
			return temp;
		}

		String[] items;
		if (cursor.moveToFirst()) {
			items = new String[cursor.getCount() + 1];
			for (int i = 0; i < cursor.getCount(); i++) {
				School item = new School(cursor);
				items[i] = item.name;
				cursor.moveToNext();
			}
			items[cursor.getCount()] = "Other";
		} else {
			items = new String[0];
		}
		cursor.close();

		return items;
	}

	// Inserts or updates a School in the database
	public synchronized boolean putSchool(final School school) {
		boolean success = false;
		int result = 0;
		final SQLiteDatabase db = this.getWritableDatabase();
		// Update a School
		if (school.id > -1) {
			result = db.update(School.TABLE_NAME, school.getContent(),
					School.COL_ID + " IS ?",
					new String[] { String.valueOf(school.id) });
		}

		// If we were able to update
		if (result > 0) {
			success = true;
		} else {
			// Unable to update, insert instead
			final long id = db.insert(School.TABLE_NAME, null,
					school.getContent());

			if (id > -1) {
				// update the School object with its now valid ID
				school.id = id;
				success = true;
			}
		}

		return success;
	}

	/*
	 * Methods for Totals
	 */

	/*
	 * returns a Totals object with the data for the given fruit_stand_id from
	 * the totals table
	 */
	protected synchronized Totals getTotalsByFruitStand(
			final long fruit_stand_id) {
		final SQLiteDatabase db = this.getReadableDatabase();
		final Cursor cursor = db.query(Totals.TABLE_NAME, Totals.FIELDS,
				Totals.COL_FRUIT_STAND_ID + " IS ?",
				new String[] { String.valueOf(fruit_stand_id) }, null, null,
				null, null);
		if (cursor == null || cursor.isAfterLast()) {
			return null;
		}

		Totals item = null;
		if (cursor.moveToFirst()) {
			item = new Totals(cursor);
		}
		cursor.close();

		return item;
	}

	// Inserts or updates a Totals in the database
	protected synchronized boolean putTotals(final Totals totals) {
		boolean success = false;
		int result = 0;
		final SQLiteDatabase db = this.getWritableDatabase();

		// Update a Totals
		if (totals.id > -1) {
			result = db.update(Totals.TABLE_NAME, totals.getContent(),
					Totals.COL_ID + " IS ?",
					new String[] { String.valueOf(totals.id) });
		}

		// If we were able to update
		if (result > 0) {
			success = true;
		} else {
			// Unable to update, insert instead
			final long id = db.insert(Totals.TABLE_NAME, null,
					totals.getContent());

			if (id > -1) {
				// update the Totals object with its now valid ID
				totals.id = id;
				success = true;
			}
		}

		return success;
	}

	/*
	 * Methods for StaffMember
	 */

	/*
	 * returns an array of StaffMember objects with the data for the given
	 * fruit_stand_id from the StaffMember table
	 */
	protected synchronized StaffMember[] getStaffMembersByFruitStand(
			final long fruit_stand_id) {
		final SQLiteDatabase db = this.getReadableDatabase();
		final Cursor cursor = db.query(StaffMember.TABLE_NAME,
				StaffMember.FIELDS, StaffMember.COL_FRUIT_STAND_ID + " IS ?",
				new String[] { String.valueOf(fruit_stand_id) }, null, null,
				null, null);
		if (cursor == null || cursor.isAfterLast()) {
			StaffMember[] temp = new StaffMember[0];
			return temp;
		}

		StaffMember[] items;
		if (cursor.moveToFirst()) {
			items = new StaffMember[cursor.getCount()];
			for (int i = 0; i < cursor.getCount(); i++) {
				StaffMember item = new StaffMember(cursor);
				items[i] = item;
				cursor.moveToNext();
			}
		} else {
			items = new StaffMember[0];
		}

		cursor.close();

		return items;
	}

	// Inserts or updates a StaffMember in the database
	protected synchronized boolean putStaffMember(final StaffMember staffMember) {
		boolean success = false;
		int result = 0;
		final SQLiteDatabase db = this.getWritableDatabase();

		// Update a StaffMember
		if (staffMember.id > -1) {
			result = db.update(StaffMember.TABLE_NAME,
					staffMember.getContent(), StaffMember.COL_ID + " IS ?",
					new String[] { String.valueOf(staffMember.id) });
		}

		// If we were able to update
		if (result > 0) {
			success = true;
		} else {
			// Unable to update, insert instead
			final long id = db.insert(StaffMember.TABLE_NAME, null,
					staffMember.getContent());

			if (id > -1) {
				// update the StaffMember object with its now valid ID
				staffMember.id = id;
				success = true;
			}
		}

		return success;
	}

	/*
	 * Methods for StartInventoryItem
	 */

	/*
	 * returns an array of StartInventoryItem objects with the data for the
	 * given fruit_stand_id from the StartInventoryItem table
	 */
	protected synchronized StartInventoryItem[] getStartInventoryItemsByFruitStand(
			final long fruit_stand_id) {
		final SQLiteDatabase db = this.getReadableDatabase();
		final Cursor cursor = db.query(StartInventoryItem.TABLE_NAME,
				StartInventoryItem.FIELDS,
				StartInventoryItem.COL_FRUIT_STAND_ID + " IS ?",
				new String[] { String.valueOf(fruit_stand_id) }, null, null,
				null, null);
		if (cursor == null || cursor.isAfterLast()) {
			StartInventoryItem[] temp = new StartInventoryItem[0];
			return temp;
		}

		StartInventoryItem[] items;
		if (cursor.moveToFirst()) {
			items = new StartInventoryItem[cursor.getCount()];
			for (int i = 0; i < cursor.getCount(); i++) {
				StartInventoryItem item = new StartInventoryItem(cursor);
				items[i] = item;
				cursor.moveToNext();
			}
		} else {
			items = new StartInventoryItem[0];
		}

		cursor.close();

		return items;
	}

	// Inserts or updates a StartInventoryItem in the database
	protected synchronized boolean putStartInventoryItem(
			final StartInventoryItem startInventoryItem) {
		boolean success = false;
		int result = 0;
		final SQLiteDatabase db = this.getWritableDatabase();

		// Update a StartInventoryItem
		if (startInventoryItem.id > -1) {
			result = db.update(StartInventoryItem.TABLE_NAME,
					startInventoryItem.getContent(), StartInventoryItem.COL_ID
							+ " IS ?",
					new String[] { String.valueOf(startInventoryItem.id) });
		}

		// If we were able to update
		if (result > 0) {
			success = true;
		} else {
			// Unable to update, insert instead
			final long id = db.insert(StartInventoryItem.TABLE_NAME, null,
					startInventoryItem.getContent());

			if (id > -1) {
				// update the StartInventoryItem object with its now valid ID
				startInventoryItem.id = id;
				success = true;
			}
		}

		return success;
	}

	/*
	 * Methods for ProcessedInventoryItem
	 */

	/*
	 * returns an array of ProcessedInventoryItem objects with the data for the
	 * given fruit_stand_id from the ProcessedInventoryItem table
	 */
	protected synchronized ProcessedInventoryItem[] getProcessedInventoryItemsByFruitStand(
			final long fruit_stand_id) {
		final SQLiteDatabase db = this.getReadableDatabase();
		final Cursor cursor = db.query(ProcessedInventoryItem.TABLE_NAME,
				ProcessedInventoryItem.FIELDS,
				ProcessedInventoryItem.COL_FRUIT_STAND_ID + " IS ?",
				new String[] { String.valueOf(fruit_stand_id) }, null, null,
				null, null);
		if (cursor == null || cursor.isAfterLast()) {
			ProcessedInventoryItem[] temp = new ProcessedInventoryItem[0];
			return temp;
		}

		ProcessedInventoryItem[] items;
		if (cursor.moveToFirst()) {
			items = new ProcessedInventoryItem[cursor.getCount()];
			for (int i = 0; i < cursor.getCount(); i++) {
				ProcessedInventoryItem item = new ProcessedInventoryItem(cursor);
				items[i] = item;
				cursor.moveToNext();
			}
		} else {
			items = new ProcessedInventoryItem[0];
		}

		cursor.close();

		return items;
	}

	// Inserts or updates a ProcessedInventoryItem in the database
	protected synchronized boolean putProcessedInventoryItem(
			final ProcessedInventoryItem processedInventoryItem) {
		boolean success = false;
		int result = 0;
		final SQLiteDatabase db = this.getWritableDatabase();

		// Update a ProcessedInventoryItem
		if (processedInventoryItem.id > -1) {
			result = db.update(ProcessedInventoryItem.TABLE_NAME,
					processedInventoryItem.getContent(),
					ProcessedInventoryItem.COL_ID + " IS ?",
					new String[] { String.valueOf(processedInventoryItem.id) });
		}

		// If we were able to update
		if (result > 0) {
			success = true;
		} else {
			// Unable to update, insert instead
			final long id = db.insert(ProcessedInventoryItem.TABLE_NAME, null,
					processedInventoryItem.getContent());

			if (id > -1) {
				// update the ProcessedInventoryItem object with its now valid
				// ID
				processedInventoryItem.id = id;
				success = true;
			}
		}

		return success;
	}

	/*
	 * Methods for EndInventoryItem
	 */

	/*
	 * returns an array of EndInventoryItem objects with the data for the given
	 * fruit_stand_id from the EndInventoryItem table
	 */
	protected synchronized EndInventoryItem[] getEndInventoryItemsByFruitStand(
			final long fruit_stand_id) {
		final SQLiteDatabase db = this.getReadableDatabase();
		final Cursor cursor = db.query(EndInventoryItem.TABLE_NAME,
				EndInventoryItem.FIELDS, EndInventoryItem.COL_FRUIT_STAND_ID
						+ " IS ?",
				new String[] { String.valueOf(fruit_stand_id) }, null, null,
				null, null);
		if (cursor == null || cursor.isAfterLast()) {
			EndInventoryItem[] temp = new EndInventoryItem[0];
			return temp;
		}

		EndInventoryItem[] items;
		if (cursor.moveToFirst()) {
			items = new EndInventoryItem[cursor.getCount()];
			for (int i = 0; i < cursor.getCount(); i++) {
				EndInventoryItem item = new EndInventoryItem(cursor);
				items[i] = item;
				cursor.moveToNext();
			}
		} else {
			items = new EndInventoryItem[0];
		}

		cursor.close();

		return items;
	}

	// Inserts or updates a EndInventoryItem in the database -- not used as of 11/2013
	protected synchronized boolean putEndInventoryItem(
			final EndInventoryItem endInventoryItem) {
		boolean success = false;
		int result = 0;
		final SQLiteDatabase db = this.getWritableDatabase();

		// Update a EndInventoryItem
		if (endInventoryItem.id > -1) {
			result = db.update(EndInventoryItem.TABLE_NAME,
					endInventoryItem.getContent(), EndInventoryItem.COL_ID
							+ " IS ?",
					new String[] { String.valueOf(endInventoryItem.id) });
		}

		// If we were able to update
		if (result > 0) {
			success = true;
		} else {
			// Unable to update, insert instead
			final long id = db.insert(EndInventoryItem.TABLE_NAME, null,
					endInventoryItem.getContent());

			if (id > -1) {
				// update the EndInventoryItem object with its now valid ID
				endInventoryItem.id = id;
				success = true;
			}
		}

		return success;
	}

	/*
	 * Methods for Purchase
	 */

	/*
	 * returns an array of Purchase objects with the data for the given
	 * fruit_stand_id from the Purchase table
	 */
	protected synchronized Purchase[] getPurchasesByFruitStand(
			final long fruit_stand_id) {
		final SQLiteDatabase db = this.getReadableDatabase();
		final Cursor cursor = db.query(Purchase.TABLE_NAME, Purchase.FIELDS,
				Purchase.COL_FRUIT_STAND_ID + " IS ?",
				new String[] { String.valueOf(fruit_stand_id) }, null, null,
				null, null);
		if (cursor == null || cursor.isAfterLast()) {
			Purchase[] temp = new Purchase[0];
			return temp;
		}

		Purchase[] items;
		if (cursor.moveToFirst()) {
			items = new Purchase[cursor.getCount()];
			for (int i = 0; i < cursor.getCount(); i++) {
				Purchase item = new Purchase(cursor);
				items[i] = item;
				cursor.moveToNext();
			}
		} else {
			items = new Purchase[0];
		}

		cursor.close();

		return items;
	}

	// Inserts or updates a Purchase in the database
	protected synchronized boolean putPurchase(final Purchase purchase) {
		boolean success = false;
		int result = 0;
		final SQLiteDatabase db = this.getWritableDatabase();
		// Update a Purchase
		if (purchase.id > -1) {
			result = db.update(Purchase.TABLE_NAME, purchase.getContent(),
					Purchase.COL_ID + " IS ?",
					new String[] { String.valueOf(purchase.id) });
		}

		// If we were able to update
		if (result > 0) {
			success = true;
		} else {
			// Unable to update, insert instead
			final long id = db.insert(Purchase.TABLE_NAME, null,
					purchase.getContent());

			if (id > -1) {
				// update the Purchase object with its now valid ID
				purchase.id = id;
				success = true;
			}
		}

		return success;
	}
}
