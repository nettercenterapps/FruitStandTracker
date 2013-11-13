package netter.uni.cis350.project;

import java.util.ArrayList;
import java.util.HashMap;

import netter.uni.database.DatabaseHandler;
import netter.uni.database.FruitStand;
import netter.uni.database.ProcessedInventoryItem;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PricingActivity extends Activity {
	HashMap<String,Integer> products_for_sale = new HashMap<String,Integer>();

	DatabaseHandler dh = DatabaseHandler.getInstance(this);
	FruitStand currentStand = dh.getCurrentFruitStand();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle data = getIntent().getExtras();

		setContentView(R.layout.activity_pricing);
        LinearLayout apple_button = (LinearLayout)findViewById(R.id.apples);
        LinearLayout orange_button = (LinearLayout)findViewById(R.id.oranges);
        LinearLayout pear_button = (LinearLayout)findViewById(R.id.pears);
        LinearLayout kiwi_button = (LinearLayout)findViewById(R.id.kiwis);
        LinearLayout grape_button = (LinearLayout)findViewById(R.id.grapes);
        LinearLayout banana_button = (LinearLayout)findViewById(R.id.bananas);
        
        
        LinearLayout apple_button_bags = (LinearLayout)findViewById(R.id.apples_bags);
        LinearLayout orange_button_bags  = (LinearLayout)findViewById(R.id.oranges_bags);
        LinearLayout pear_button_bags  = (LinearLayout)findViewById(R.id.pears_bags);
        LinearLayout kiwi_button_bags  = (LinearLayout)findViewById(R.id.kiwis_bags);
        LinearLayout grape_button_bags  = (LinearLayout)findViewById(R.id.grape_bags);
        LinearLayout banana_button_bags  = (LinearLayout)findViewById(R.id.bananas_bags);
        
        LinearLayout smoothie_button = (LinearLayout)findViewById(R.id.sm);
        LinearLayout mixed_bag_button = (LinearLayout)findViewById(R.id.mbb);
        LinearLayout granola_button = (LinearLayout)findViewById(R.id.gb);
    

		HashMap<String,Integer> inv = (HashMap<String, Integer>) data.get("inventoryCts");
		
		products_for_sale = inv;
    	if(inv.get("apple_bags") == 0){
    		apple_button_bags.setVisibility(8);
    	}
    	if(inv.get("orange_bags") == 0){
    		orange_button_bags.setVisibility(8);
    		//orange_text.setVisibility(8);
    	}
    	if(inv.get("pear_bags") == 0){
    		pear_button_bags.setVisibility(8);
    	//	pear_text.setVisibility(8);
    	}
    	if(inv.get("kiwi_bags") == 0){
    		kiwi_button_bags.setVisibility(8);
    	//	kiwi_text.setVisibility(8);
    	}
    	if(inv.get("grape_bags") == 0){
    		grape_button_bags.setVisibility(8);
    	//	grape_text.setVisibility(8);
    	}
    	if(inv.get("banana_bags") == 0){
    		banana_button_bags.setVisibility(8);
    	//	banana_text.setVisibility(8);
    	}
		if(inv.get("apple") == 0){
    		apple_button.setVisibility(8);
    	}
    	if(inv.get("orange") == 0){
    		orange_button.setVisibility(8);
    		//orange_text.setVisibility(8);
    	}
    	if(inv.get("pear") == 0){
    		pear_button.setVisibility(8);
    	//	pear_text.setVisibility(8);
    	}
    	if(inv.get("kiwi") == 0){
    		kiwi_button.setVisibility(8);
    	//	kiwi_text.setVisibility(8);
    	}
    	if(inv.get("grape") == 0){
    		grape_button.setVisibility(8);
    	//	grape_text.setVisibility(8);
    	}
    	if(inv.get("banana") == 0){
    		banana_button.setVisibility(8);
    	//	banana_text.setVisibility(8);
    	}
    	if(inv.get("smoothie") == 0){
    		smoothie_button.setVisibility(8);
    	//	smoothie_text.setVisibility(8);
    	}
    	if(inv.get("mixed") == 0){
    		mixed_bag_button.setVisibility(8);
    	//	mixed_bag_text.setVisibility(8);
    	}
    	if(inv.get("granola") == 0){
    		granola_button.setVisibility(8);
    	//	granola_text.setVisibility(8);
    	}
	}
	@Override
	public void onBackPressed(){
		//back does nothing
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_pricing, menu);
		return true;
	}
	
	public void highlightEditTextById(int id, boolean on) {
		EditText et = (EditText) findViewById(id);
		et.setBackgroundColor(on ? Color.argb(90, 255, 255, 0) : Color.argb(90, 255, 255, 255));
	}
	
	public double getPrice (int cid){
		EditText qtyEdit = (EditText) findViewById(cid);
		Editable qtyE = qtyEdit.getText();
		
		try {
			highlightEditTextById(cid, false);
			return Double.parseDouble(qtyE.toString());
		} catch (Exception e) {
			e.printStackTrace();
			highlightEditTextById(cid, true);
			String toastText = "Incorrectly entered prices have been adjusted to $0.50.";
			Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
			return 0.50;
		}
	}
	
	public void continueToInventory(View v) {
    	//Launch to inventory
    	Intent i = new Intent(this, PreSalesPepActivity.class);
    	//this will initialize all to 50 cents if not being sold - this will cause a problem later if no food is sold for that little
    	ArrayList<Double> prices = new ArrayList<Double>();
    	double applePrice = getPrice(R.id.applePrice);
    	double bananaPrice = getPrice(R.id.bananaPrice);
    	double grapesPrice = getPrice(R.id.grapesPrice);
    	double kiwiPrice = getPrice(R.id.kiwiPrice);
    	double orangePrice = getPrice(R.id.orangePrice);
    	double pearPrice = getPrice(R.id.pearPrice);
    	double granolaPrice = getPrice(R.id.granolaPrice);
    	double mixedPrice = getPrice(R.id.mixedPrice);
    	double smoothiePrice = getPrice(R.id.smoothiePrice);
    	
    	double applePrice_bags = getPrice(R.id.applePrice_bags);
    	double bananaPrice_bags = getPrice(R.id.bananaPrice_bags);
    	double grapesPrice_bags = getPrice(R.id.grapesPrice_bags);
    	double kiwiPrice_bags = getPrice(R.id.kiwiPrice_bags);
    	double orangePrice_bags = getPrice(R.id.orangePrice_bags);
    	double pearPrice_bags = getPrice(R.id.pearPrice_bags);
    	
		currentStand.addProcessedInventoryItem(this, "apple", products_for_sale.get("apple"), applePrice);
		currentStand.addProcessedInventoryItem(this, "pear", products_for_sale.get("pear"), pearPrice);
		currentStand.addProcessedInventoryItem(this, "orange", products_for_sale.get("orange"), orangePrice);
		currentStand.addProcessedInventoryItem(this, "banana", products_for_sale.get("banana"), bananaPrice);
		currentStand.addProcessedInventoryItem(this, "grape", products_for_sale.get("grape"), grapesPrice);
		currentStand.addProcessedInventoryItem(this, "kiwi", products_for_sale.get("kiwi"), kiwiPrice);

		currentStand.addProcessedInventoryItem(this, "apple_bags", products_for_sale.get("apple_bags"), applePrice_bags);
		currentStand.addProcessedInventoryItem(this, "pear_bags", products_for_sale.get("pear_bags"), pearPrice_bags);
		currentStand.addProcessedInventoryItem(this, "orange_bags", products_for_sale.get("orange_bags"), orangePrice_bags);
		currentStand.addProcessedInventoryItem(this, "banana_bags", products_for_sale.get("banana_bags"), bananaPrice_bags);
		currentStand.addProcessedInventoryItem(this, "grape_bags", products_for_sale.get("grape_bags"), grapesPrice_bags);
		currentStand.addProcessedInventoryItem(this, "kiwi_bags", products_for_sale.get("kiwi_bags"), kiwiPrice_bags);
		
		currentStand.addProcessedInventoryItem(this, "mixed", products_for_sale.get("mixed"), mixedPrice);
		currentStand.addProcessedInventoryItem(this, "smoothie", products_for_sale.get("smoothie"), smoothiePrice);
		currentStand.addProcessedInventoryItem(this, "granola", products_for_sale.get("granola"), granolaPrice);
		
    	this.startActivity(i);
    	this.finish();
    }

}
