package netter.uni.cis350.project;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import netter.uni.database.DatabaseHandler;
import netter.uni.database.FruitStand;
import netter.uni.database.ProcessedInventoryItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PaymentActivity extends Activity {
	public int total_number_items;
	boolean bargain = false;
	boolean cashOn = false;
	public int checked_out = 0;
	public int cash_count = 0;
	public double cash_total = 0.0;
	public int coupons = 0;
	public int junk_food = 0;
	private double bargain_amt =0.0;
	private TextView bargainDisplayTxt;
	private double total_value = 0.0;
	private double total_value_backup = 0.0;
    DecimalFormat df = new DecimalFormat("#.##");

	Bundle data;
	Button submit_button;
	Button cash_button;
	Button coupon_button;
	Button junk_food_button;
	Button bargain_button;
	private FruitTuple[] purchasedItems;
	private Integer numCoupons = 0;
	private Integer numTradeIns = 0;
	private Intent intent;
	private FruitStand currStand;
	private HashMap<String,Integer> fruitSold;
	private int transactionCt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sale_payment);
		TextView items = (TextView) findViewById(R.id.items);
		fruitSold = (HashMap<String, Integer>) getIntent().getSerializableExtra("fruitSold");
		cash_button = (Button) findViewById(R.id.cash);
		coupon_button = (Button) findViewById(R.id.coupon);
		junk_food_button = (Button) findViewById(R.id.junk_food);
		submit_button = (Button) findViewById(R.id.submit);
		bargain_button = (Button) findViewById(R.id.discount_button);
		data = getIntent().getExtras();

		DatabaseHandler dht = DatabaseHandler.getInstance(this);
		currStand = dht.getCurrentFruitStand();
		
		ArrayList<FruitTuple> itemBuffer = new ArrayList<FruitTuple>();
		ProcessedInventoryItem[] possItems = currStand.getProcessedInventoryItems(this);
		
		// Impossible on an actual application instance, but needed in testing
		if (possItems != null) {
		
			for (ProcessedInventoryItem item : possItems) {
				if(fruitSold.containsKey(item.item_name)){
					for (int i = 0; i < fruitSold.get(item.item_name); i++){
					itemBuffer.add(new FruitTuple(item.item_name, item.price));
					}
					total_value += item.price*fruitSold.get(item.item_name);
					total_number_items += fruitSold.get(item.item_name);
				}
			}
			total_value_backup = total_value;

			items.setText("Total Due :\n" + total_value);
	        transactionCt = getIntent().getIntExtra("transactionCt", 0);
			// Sort list in ascending order by price; comparator enforces this
			// for custom
			// FruitTuple type
			Collections.sort(itemBuffer, new Comparator<FruitTuple>() {
				public int compare(FruitTuple a, FruitTuple b) {
					return (a.price > b.price ? 1 : a.price < b.price ? -1 : 0);
				}
			});

			purchasedItems = (FruitTuple[]) itemBuffer
					.toArray(new FruitTuple[itemBuffer.size()]);
		
		}
		int total_left = total_number_items - checked_out;
		
		//added this because now that donations are a thing the total left might start at 0
		if (total_left == 0) {
			cash_button.setEnabled(false);
			coupon_button.setEnabled(false);
			junk_food_button.setEnabled(false);
			bargain_button.setEnabled(false);

			submit_button.setEnabled(true);
			
		} else {
			cash_button.setEnabled(true);
			coupon_button.setEnabled(true);
			junk_food_button.setEnabled(true);
			bargain_button.setEnabled(true);
			submit_button.setEnabled(false);
		}
	}
	
	@Override
	public void onBackPressed(){
		//back does nothing
	}
	
	public void updateItems() {
		checked_out = cash_count + coupons + junk_food;
		int total_left = total_number_items - checked_out;
		
		TextView items = (TextView) findViewById(R.id.items);
		items.setText("Total Due:\n" + total_value);
		if (total_left == 0) {
			cash_button.setEnabled(false);
			coupon_button.setEnabled(false);
			junk_food_button.setEnabled(false);
			bargain_button.setEnabled(false);
			submit_button.setEnabled(true);
		} else {
			cash_button.setEnabled(true);
			coupon_button.setEnabled(true);
			junk_food_button.setEnabled(true);
			if(!cashOn){
				bargain_button.setEnabled(true);
			}
			submit_button.setEnabled(false);
		}
	}

	public void cash_button(View view) {
		if(total_number_items - checked_out>0){
			cash_total += total_value;
			total_value = 0.0;
	    	checked_out = total_number_items;
			cash_button.setEnabled(false);
			coupon_button.setEnabled(false);
			junk_food_button.setEnabled(false);
			submit_button.setEnabled(true);
			bargain_button.setEnabled(false);
			TextView items = (TextView) findViewById(R.id.items);
			items.setText("Total Due:\n" + total_value);
			cashOn = true;
		}
	}

	public void coupon_button(View view) {
		if(total_number_items - checked_out>0){
			total_value -= purchasedItems[coupons+junk_food].price;
			purchasedItems[coupons+junk_food].coupon = true;

			coupons += 1;
			TextView text = (TextView) findViewById(R.id.coupon_label);
			text.setText("x" + coupons);
			updateItems();
		}
	}
	
	public void mm(){
		AlertDialog.Builder editalert = new AlertDialog.Builder(this);

		editalert.setTitle("Bargain");
		editalert.setMessage("How much are you getting for these items?");


		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
		        LinearLayout.LayoutParams.MATCH_PARENT,
		        LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		editalert.setView(input);

		editalert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    	String s = input.getText().toString();
		    	if(s.equals(null)||s.equals("")||s.equals(".")){
		    		s = "0.0";
		    	}
		    	total_value = 0.0;
		    	checked_out = total_number_items;
				cash_button.setEnabled(false);
				coupon_button.setEnabled(false);
				junk_food_button.setEnabled(false);
				submit_button.setEnabled(true);
				bargain_button.setEnabled(false);
				bargain_amt = Double.parseDouble(s);
				TextView items = (TextView) findViewById(R.id.items);
				items.setText("Total Due:\n" + total_value);
				bargain = true;
		    }
		});


		editalert.show();
    }
    
    public void bargain_button(View view){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Are you sure there are no coupons or junk food?");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
		        	  mm();
	        	   
	           }
	       });
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               dialog.dismiss();
	           }
	       });
		AlertDialog dialog = builder.create();
		dialog.show();
    }

	public void junk_food_button(View view) {
		// left can't be negative
		if(total_number_items - checked_out>0){
			total_value -= purchasedItems[coupons+junk_food].price;
			purchasedItems[coupons+junk_food].junk = true;
			junk_food += 1;
			TextView text = (TextView) findViewById(R.id.junk_food_label);
			text.setText("x" + junk_food);
			updateItems();
		}
	}

	public void clear_button(View view){
		checked_out = 0;
		cash_count = 0;
		cash_total = 0.0;
		coupons = 0;
		junk_food = 0;
		bargain = false;
		cashOn = false;
		total_value = total_value_backup;
		updateItems();

		int[] buttons = { R.id.coupon_label, R.id.junk_food_label};
		for(int i: buttons){
			TextView text = (TextView)findViewById(i);
			text.setText("x0");
		}
	}
	
	public void submit_button(View view) {
		Intent transaction = new Intent(this, TransactionBaseActivity.class);
		
		EditText tr_don_textbox = (EditText) findViewById(R.id.donIn);

		String tr_don_amt = tr_don_textbox.getText().toString();
		Double donationAmtAsDoub = 0.0;
		if (tr_don_amt.equals("") || tr_don_amt.equals(null)){
			donationAmtAsDoub = 0.0;

		}else{
			donationAmtAsDoub = Double.valueOf(tr_don_amt);
		}
		
		DatabaseHandler dh = DatabaseHandler.getInstance(this);
		FruitStand currentStand = dh.getCurrentFruitStand();
		
		String grade = data.getString("grade");
		if (donationAmtAsDoub != 0.0){
			currentStand.addPurchase(this, "donation", 0, 0, 0, donationAmtAsDoub, grade);
		}
		//count is the transaction number coupon and junk are switches 0 or 1
		//parse info into purchases. each bargain info has as its price the price divided by
		//the number of items in the bargain. 
		//save the purchase to the database
			boolean firstAdjust = true;
			for(FruitTuple p :purchasedItems){
				double localPrice = p.price;
				String localName = p.name;
				if (bargain && !p.coupon && !p.junk){
					localPrice = Double.parseDouble(df.format(bargain_amt /(total_number_items-junk_food-coupons)));
					if ((localPrice*(total_number_items-junk_food-coupons)) -bargain_amt != 0 && firstAdjust ){
						localPrice -= (localPrice*(total_number_items-junk_food-coupons) -bargain_amt);
						firstAdjust = false;
					}
					localName = localName + "BARGAIN";
				}
				if (p.coupon){
					currentStand.addPurchase(this, localName, transactionCt, 1, 0, localPrice, grade);
				}else if (p.junk){
					currentStand.addPurchase(this, localName, transactionCt, 0, 1, localPrice, grade);
				}else{
					currentStand.addPurchase(this, localName, transactionCt, 0, 0, localPrice, grade);
				}

			}
		
		coupons = 0;
		junk_food = 0;
		cash_total = 0.0;
		bargain = false;
		//pass transaction count back to the main transaction screen
		transaction.putExtra("transactionCt", transactionCt);
		this.startActivity(transaction);
		this.finish();
	}
	private class FruitTuple {
		private String name;
		private double price;
		public boolean coupon = false;
		public boolean junk = false;
		
		public FruitTuple(String f, double p) {
			name = f;
			price = p;
		}
	}
}
