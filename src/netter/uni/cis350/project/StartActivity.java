package netter.uni.cis350.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import netter.uni.database.DatabaseHandler;
import netter.uni.database.FruitStand;
import netter.uni.database.ProcessedInventoryItem;
import netter.uni.database.Purchase;
import netter.uni.database.StaffMember;
import netter.uni.database.StartInventoryItem;
import netter.uni.database.Totals;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class StartActivity extends Activity {
	
	Bundle data;
	Context thisActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getIntent().getExtras();
        setContentView(R.layout.activity_start);
        thisActivity = this;
        //for Parse
        Parse.initialize(this, 
        		getString(R.string.parseAPIkey), getString(R.string.parsesecretkey));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_start, menu);
        return true;
    }
    @Override
	public void onBackPressed(){
		//back does nothing
	}
    @Override
  	public boolean onOptionsItemSelected(MenuItem item) {
  	    // Handle item selection
  	    switch (item.getItemId()){
  	    	case R.id.clear_data:
  	    		reallyClear();
  	    		return true;
  	    	case R.id.log_data:
  	    		saveStandToParse();
  	        	return true;
  	        default:
  	            return super.onOptionsItemSelected(item);
  	    }
  	}
    public void start_button(View view){
    	Intent i = new Intent(this, InfoActivity.class);
    	this.startActivity(i);
    	this.finish();
    }
    private void clearLocalData(){
    	DatabaseHandler dh = DatabaseHandler.getInstance(this);

		FruitStand[] allstands = dh.getAllFruitStands();
		for (FruitStand stand: allstands){
			dh.removeFruitStand(stand);
		}
    }
    private void saveStandToParse() {
    	//save the data both in multiple tables as it exists in sqlite, but also in one large table
    	//because we will be frequently pulling all the data to write it to a spreadsheet.
    	//This will lower the number of queries required. 
		DatabaseHandler dh = DatabaseHandler.getInstance(this);

		FruitStand[] allstands = dh.getAllFruitStands();
		for (FruitStand stand: allstands){
			ParseObject PFruitStandALL = new ParseObject("FruitStandALL");

			ParseObject PFruitStand = new ParseObject("FruitStand");
			PFruitStand.put("school", stand.school);
			PFruitStand.put("date", stand.date);
			PFruitStand.put("temperature", stand.temperature);
			PFruitStand.put("weather", stand.weather);
			PFruitStand.put("cashbox_starting_amount", stand.initial_cash);
			PFruitStand.put("fruit_cost", stand.stand_cost);
			PFruitStand.put("smoothie_cost", stand.smoothie_cost);
			PFruitStand.put("granola_cost", stand.additional_cost);

			PFruitStandALL.put("Fruit_Stand_id",stand.school+stand.date);
			PFruitStandALL.put("school", stand.school);
			PFruitStandALL.put("date", stand.date);
			PFruitStandALL.put("temperature", stand.temperature);
			PFruitStandALL.put("weather", stand.weather);
			PFruitStandALL.put("cashbox_starting_amount", stand.initial_cash);
			PFruitStandALL.put("fruit_cost", stand.stand_cost);
			PFruitStandALL.put("smoothie_cost", stand.smoothie_cost);
			PFruitStandALL.put("granola_cost", stand.additional_cost);
			
			PFruitStand.saveInBackground();
	
			
			Totals totals = stand.getTotals(this);
			if (totals != null) {
				ParseObject PTotals = new ParseObject("Totals");
				PTotals.put("parent", PFruitStand);
				PTotals.put("total_cost", totals.cost);
				PTotals.put("revenue", totals.revenue);
				PTotals.put("ending_cashbox_amount", totals.final_cash);
				PTotals.put("math_override", totals.math_override);
				PTotals.put("message", totals.message);
				
				
				PFruitStandALL.put("profit", totals.revenue -totals.cost);                   
				PFruitStandALL.put("total_cost", totals.cost);                   
				PFruitStandALL.put("revenue", totals.revenue);             
				PFruitStandALL.put("ending_cashbox_amount", totals.final_cash);       
				PFruitStandALL.put("math_override", totals.math_override); 
				PFruitStandALL.put("message", totals.message);             
				
				PTotals.saveInBackground();
			}
			ArrayList<String> volunteers_names = new ArrayList<String>();
			ArrayList<String> staff_names = new ArrayList<String>();

			StaffMember[] staff = stand.getStaffMembers(this);
			for (StaffMember s : staff) {
				//don't log staff with blank names
				if(s.name != null && s.name.length() > 0){
					ParseObject PStaffMember = new ParseObject("StaffMember");
					PStaffMember.put("parent", PFruitStand);
					PStaffMember.put("name", s.name);
					PStaffMember.put("is_volunteer", s.is_volunteer);
					PStaffMember.saveInBackground();
					if(s.is_volunteer){
						volunteers_names.add(s.name);
					}else{
						staff_names.add(s.name);
					}
				}
			}
			PFruitStandALL.add("volunteers", volunteers_names);
			PFruitStandALL.add("staff", staff_names);

			HashMap<String,Integer> startInventoryCounts = new HashMap<String,Integer>();
			
			StartInventoryItem[] startInventory = stand
					.getStartInventoryItems(this);
			for (StartInventoryItem i : startInventory) {
				ParseObject PItem = new ParseObject("StartInventoryItem");
				PItem.put("parent", PFruitStand);
				PItem.put("item_name", i.item_name);
				PItem.put("count", i.count);
				PItem.saveInBackground();
				startInventoryCounts.put(i.item_name, i.count);
			}
			PFruitStandALL.add("startInventory", startInventoryCounts);
			
			HashMap<String,Integer> processedInventoryCounts = new HashMap<String,Integer>();
			HashMap<String,Double> pricesInventoryCounts = new HashMap<String,Double>();

			ProcessedInventoryItem[] processedInventory = stand
					.getProcessedInventoryItems(this);
			for (ProcessedInventoryItem i : processedInventory) {
				ParseObject PItem = new ParseObject("ProcessedInventoryItem");
				PItem.put("parent", PFruitStand);
				PItem.put("item_name", i.item_name);
				PItem.put("count", i.count);
				PItem.put("price", i.price);
				PItem.saveInBackground();
				processedInventoryCounts.put(i.item_name, i.count);
				pricesInventoryCounts.put(i.item_name, i.price);

			}
			PFruitStandALL.add("processedInventory", processedInventoryCounts);
			PFruitStandALL.add("pricesInventory", pricesInventoryCounts);
			ArrayList<String> allItems =new ArrayList(Arrays.asList(getResources().getStringArray(R.array.productList)));

			ArrayList<String> wholeFruit =new ArrayList(Arrays.asList(getResources().getStringArray(R.array.wholeFruit)));
			ArrayList<String> bagFruit =new ArrayList(Arrays.asList(getResources().getStringArray(R.array.bagFruit)));
			HashMap<String,Integer> regularPriceSoldCounts = new HashMap<String,Integer>();
			HashMap<String,Integer> bargainPriceSoldCounts = new HashMap<String,Integer>();
			for (String i : allItems){
				regularPriceSoldCounts.put(i, 0);
				bargainPriceSoldCounts.put(i, 0);
			}
			double donationTotal = 0.0;
			int couponCt =0;
			int junkCt = 0;
			int wholeCtBar = 0;
			int bagCtBar = 0;

			int wholeCtReg = 0;
			int bagCtReg = 0;
			int transCt = 0;
			double barginTotal = 0.0;
			double cashTotal = 0.0;
			Purchase[] purchases = stand.getPurchases(this);
			for (Purchase p : purchases) {
				ParseObject PPurchase = new ParseObject("Purchase");
				PPurchase.put("parent", PFruitStand);
				PPurchase.put("item_name", p.item_name);
				PPurchase.put("transaction_num", p.transaction_num);
				PPurchase.put("num_coupons", p.num_coupons);
				PPurchase.put("num_tradeins", p.num_tradeins);
				PPurchase.put("amount_cash", p.amount_cash);
				PPurchase.put("customer", p.customer);
				PPurchase.saveInBackground();
				if(p.transaction_num > transCt){
					transCt = p.transaction_num;
				}
				if(p.item_name.equals("donation")){
					donationTotal += p.amount_cash;
				}else{
					
					if(p.num_coupons > 0){
						couponCt +=1;
					}
					if(p.num_tradeins > 0){
						junkCt +=1;
					}
					
					if(p.item_name.contains("BARGAIN")){
						if(wholeFruit.contains(p.item_name.subSequence(0, p.item_name.length()-7))){
							wholeCtBar +=1;
						}
						if(bagFruit.contains(p.item_name.subSequence(0, p.item_name.length()-7))){
							bagCtBar +=1;
						}
						barginTotal += p.amount_cash;
						if(bargainPriceSoldCounts.containsKey(p.item_name)){
							bargainPriceSoldCounts.put(p.item_name, bargainPriceSoldCounts.get(p.item_name)+1);
						}
					}else{
						if(wholeFruit.contains(p.item_name)){
							wholeCtReg +=1;
						}
						if(bagFruit.contains(p.item_name)){
							bagCtReg +=1;
						}
						cashTotal += p.amount_cash;
						if(regularPriceSoldCounts.containsKey(p.item_name)){
							regularPriceSoldCounts.put(p.item_name, regularPriceSoldCounts.get(p.item_name)+1);
						}
					}
				}
			}
			PFruitStandALL.add("donation_total", donationTotal);
			PFruitStandALL.add("transaction_count", transCt);
			PFruitStandALL.add("junk_food", junkCt);
			PFruitStandALL.add("coupons", couponCt);
			PFruitStandALL.add("barginTotal", barginTotal);
			PFruitStandALL.add("cashTotal", cashTotal);

			PFruitStandALL.add("wholeCtReg", wholeCtReg);
			PFruitStandALL.add("wholeCtBar", wholeCtBar);
			PFruitStandALL.add("bagCtReg", bagCtReg);
			PFruitStandALL.add("bagCtBar", bagCtBar);


			PFruitStandALL.add("bargainPriceSoldCounts", bargainPriceSoldCounts);

			PFruitStandALL.add("regularPriceSoldCounts", regularPriceSoldCounts);

			PFruitStandALL.saveInBackground(new SaveCallback() {
				 
				@Override
				public void done(com.parse.ParseException e) {
					if (e == null) {
			    		Toast mToast = Toast.makeText( thisActivity  , "data saved successfully" , Toast.LENGTH_SHORT );
			    		mToast.show();
					     } else {
					    		Toast mToast = Toast.makeText( thisActivity  , "data NOT successfully"+e.getMessage() , Toast.LENGTH_SHORT );
					    		mToast.show();

					     }					
				}
			});
		}
	}
    public void reallyClear(){
    	AlertDialog.Builder editalert = new AlertDialog.Builder(this);

		editalert.setTitle("Really Clear Local Data?");
		editalert.setMessage("Please enter the password");


		final EditText input = new EditText(this);
		
		editalert.setView(input);

		editalert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    	String s = input.getText().toString();
		    	if(s.equals("iaasoncr")){
		    		clearLocalData();
		    		Toast mToast = Toast.makeText( thisActivity  , "data cleared successfully" , Toast.LENGTH_SHORT );
		    	    mToast.show();
		    	}else{
		    	    Toast mToast = Toast.makeText( thisActivity  , "password incorrect" , Toast.LENGTH_SHORT );
		    	    mToast.show();
		    	}
		    }
		});


		editalert.show();
    }
    
}
