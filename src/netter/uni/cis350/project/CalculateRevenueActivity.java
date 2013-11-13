package netter.uni.cis350.project;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import netter.uni.cis350.project.CalculateProfitActivity.ErrorDialog;
import netter.uni.database.DatabaseHandler;
import netter.uni.database.FruitStand;
import netter.uni.database.Purchase;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CalculateRevenueActivity extends FragmentActivity {
	
	//Bundle data;
    DecimalFormat df = new DecimalFormat("#.##");
    
	int bagsTotal = 0;
	int wholeTotal = 0;
	double bargainTotal = 0.0;
	double bagsTotalValue = 0.0;
	double wholeTotalValue = 0.0;

	double standardPriceSales = 0.0;
	int tries;
	double donationTotal=0;
	double couponTotal = 0;
	double junkTotal = 0;
	private HashMap<String,Double> prices = new HashMap<String,Double>();
	private HashMap<String,Integer> saleTotals = new HashMap<String,Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculate_revenue);
		

    	DatabaseHandler dh = DatabaseHandler.getInstance(this);
		FruitStand currentStand = dh.getCurrentFruitStand();
		ArrayList<String> allItems =new ArrayList(Arrays.asList(getResources().getStringArray(R.array.productList)));
		ArrayList<String> wholeFruit =new ArrayList(Arrays.asList(getResources().getStringArray(R.array.wholeFruit)));
		ArrayList<String> bagFruit =new ArrayList(Arrays.asList(getResources().getStringArray(R.array.bagFruit)));

        for(String s : allItems){
        	saleTotals.put(s, 0);
        	saleTotals.put(s+"BARGAIN", 0);

        	prices.put(s, 0.5);
        	prices.put(s+"BARGAIN", 0.0);

        }
        //aggregate info from purchases
		Purchase[] purchasesSoFar = currentStand.getPurchases(this);
        for (Purchase p : purchasesSoFar){
        	if(p.item_name.contains("BARGAIN")){
    			bargainTotal += p.amount_cash;
    		}else{
    			if(!p.item_name.contains("donation")){
	    			prices.put(p.item_name, p.amount_cash);
	    			if(p.item_name.contains("bags")){
	    				bagsTotal += 1;
	    				bagsTotalValue+=p.amount_cash;
	    			}else{
	    				wholeTotal +=1;
	    				wholeTotalValue+=p.amount_cash;
	
	    			}
    			}
    		}
        	if (p.item_name.equalsIgnoreCase("donation")){
    			donationTotal+= p.amount_cash;
    		}else{
    			int ct = 0;
    			if (saleTotals.containsKey(p.item_name)){
    				ct = saleTotals.get(p.item_name);
    			}
    			saleTotals.put(p.item_name,ct+1);
    		}
        	if(p.num_coupons == 0 && p.num_tradeins == 0 && !p.item_name.contains("BARGAIN") && !p.item_name.contains("donation")){
    			standardPriceSales += p.amount_cash;

        		
        	}else if (p.num_coupons == 1){
        		couponTotal += p.amount_cash;
        	}else if (p.num_tradeins == 1){
        		junkTotal += p.amount_cash;
        	}
        }

		TextView wholefruitlabel = (TextView)findViewById(R.id.wholeFruitTotalLabel);
		TextView smoothielabel = (TextView)findViewById(R.id.smoothieTotalLabel);
		TextView mixedbaglabel = (TextView)findViewById(R.id.mixedBagTotalLabel);
		TextView granolalabel = (TextView)findViewById(R.id.granolaTotalLabel);
		TextView couponlabel = (TextView)findViewById(R.id.couponLabel);
		TextView barginLabel = (TextView)findViewById(R.id.barginLabel);

		TextView junkfoodlabel = (TextView)findViewById(R.id.junkfoodLabel);
		TextView donlabel = (TextView)findViewById(R.id.donLable);		
		
		String whole_fruit_equation = "Whole Fruit: \n";
		String tab = "     ";
		int extraLineCt = 0;
		//only display items that were for sale
		for (String item : wholeFruit){
			if (saleTotals.containsKey(item)){
				if (saleTotals.get(item) > 0){
					whole_fruit_equation =  whole_fruit_equation + tab + item + " "  + saleTotals.get(item) + " x" + prices.get(item)+"\n";
				}else{
					extraLineCt += 1;
				}
			}
		}
		//if no items were from sale, hide the category entirely
		if (extraLineCt == wholeFruit.size()){
			TextView wholeRevenEdit = (TextView)findViewById(R.id.wholeFruitRevenue);
			wholeRevenEdit.setVisibility(8);
			TextView wholeDollar = (TextView)findViewById(R.id.dollarSign9);
			wholeDollar.setVisibility(8);
			wholefruitlabel.setVisibility(8);
		}else{
			for (int i = 0; i < extraLineCt; i++){
				whole_fruit_equation = whole_fruit_equation + "\n";
	
			}
			wholefruitlabel.setText(whole_fruit_equation);
		}
		smoothielabel.setText("Smoothies: "+saleTotals.get("smoothie") + "x"+ prices.get("smoothie")+" =");
		String bag_fruit_equation = "Bagged Fruit :\n";
		int extraLineCtBags = 0;
		//only display items that were for sale
		for (String item : bagFruit){
			if (saleTotals.containsKey(item)){
				if (saleTotals.get(item) > 0){
					bag_fruit_equation =  bag_fruit_equation + tab + item +" "+ saleTotals.get(item) + " x" + prices.get(item)+"\n";
				}else{
					extraLineCtBags += 1;
				}
			}
		}
		//if no items for sale, don't display the box
		if (extraLineCtBags == bagFruit.size()){
			TextView bagRevenEdit = (TextView)findViewById(R.id.mixedBagRevenue);
			bagRevenEdit.setVisibility(8);
			TextView bagDollar = (TextView)findViewById(R.id.dollarSign8);
			bagDollar.setVisibility(8);
			mixedbaglabel.setVisibility(8);
		}else{
			for (int i = 0; i < extraLineCtBags; i++){
				bag_fruit_equation = bag_fruit_equation + "\n";
	
			}
			mixedbaglabel.setText(bag_fruit_equation);
		}

		granolalabel.setText("Granola Bars: "+saleTotals.get("granola") + "x"+ prices.get("granola")+" =");
		couponlabel.setText("Coupon Total Value =           $"+ df.format(couponTotal));
		barginLabel.setText("Bargain Total Value =           $"+ df.format(bargainTotal));

		junkfoodlabel.setText("Junk Food Total Value =        $"+ df.format(junkTotal));
		donlabel.setText("Donation Total Value =        $"+ df.format(donationTotal));

	}
	@Override
	public void onBackPressed(){
		//back does nothing
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_calculate_profit, menu);
		return true;
	}
	
	public void submit_calculations(View view){
		TextView wholefruitlabel = (TextView)findViewById(R.id.wholeFruitTotalLabel);
		TextView smoothielabel = (TextView)findViewById(R.id.smoothieTotalLabel);
		TextView mixedbaglabel = (TextView)findViewById(R.id.mixedBagTotalLabel);
		TextView granolalabel = (TextView)findViewById(R.id.granolaTotalLabel);
		
		EditText wholefruit = (EditText)findViewById(R.id.wholeFruitRevenue);
		EditText smoothie = (EditText)findViewById(R.id.smoothieRevenue);
		EditText mixedbag = (EditText)findViewById(R.id.mixedBagRevenue);
		EditText granola = (EditText)findViewById(R.id.granolaRevenue);
		EditText total = (EditText)findViewById(R.id.totalRevenue);
		double bags_input = 0.0;
		double gran_input = 0.0;
		double smoothie_input = 0.0;
		double fruit_input = 0.0;
		try{
			if(smoothielabel.getVisibility()!=8){
				smoothie_input = Double.parseDouble(smoothie.getText().toString());
			}
			if (mixedbaglabel.getVisibility()!=8){
				bags_input = Double.parseDouble(mixedbag.getText().toString());
			}
			if(wholefruitlabel.getVisibility()!=8){
				fruit_input = Double.parseDouble(wholefruit.getText().toString());
			}
			if(granolalabel.getVisibility()!=8){

				gran_input = Double.parseDouble(granola.getText().toString());
			}
		}catch(Exception e){
			String toastText = "Looks like you left the total blank...the total has to be filled in before submitting.";
			Toast.makeText(getApplicationContext(), toastText.toString(), Toast.LENGTH_LONG).show();
		}
		
		try{
			double usertotal = Double.parseDouble(total.getText().toString());
			//double realtotal = db.getCashTot()+donationTotal+db.getBarTot()-db.getJunkTot()-db.getCoupTot();
			double realtotal = Double.parseDouble(df.format(bargainTotal+standardPriceSales+donationTotal));
			if(realtotal == usertotal || tries >2){//////should there be an override for total revenue here? should the math walkthrough get more intricate?
				Intent i = new Intent(this, CalculateProfitActivity.class);
				i.putExtra("revenue",bargainTotal+standardPriceSales+donationTotal );
				this.startActivity(i);
				this.finish();
			} else {
				double real_smoothie =saleTotals.get("smoothie") * prices.get("smoothie");
				double real_bags = bagsTotalValue;
				double real_fruit =wholeTotalValue;
				double real_gran = saleTotals.get("granola") * prices.get("granola");
				total.setBackgroundColor(Color.argb(90, 255, 255, 0));
				boolean smoothie_wrong = Math.abs(real_smoothie - smoothie_input ) > .01;
				boolean bags_wrong = Math.abs(real_bags - bags_input) > .01;
				boolean gran_wrong = Math.abs(real_gran) - gran_input > .01;
				boolean fruit_wrong = Math.abs(real_fruit) - fruit_input > .01;

				wholefruit.setBackgroundColor(fruit_wrong ? Color.argb(90, 255, 255, 0) : Color.argb(90, 255, 255, 255));

				smoothie.setBackgroundColor(smoothie_wrong ? Color.argb(90, 255, 255, 0) : Color.argb(90, 255, 255, 255));
				mixedbag.setBackgroundColor(bags_wrong ? Color.argb(90, 255, 255, 0) : Color.argb(90, 255, 255, 255));
				granola.setBackgroundColor(gran_wrong ? Color.argb(90, 255, 255, 0): Color.argb(90, 255, 255, 255));

				
				String toastText = "Looks like you made a mistake in your math...\n check that the total is equal to \n" +
						"Whole fruit sold + Bagged Fruit Sold + Smoothies + donations + bargain total - coupons - junk food ";
				//Toast.makeText(getApplicationContext(), toastText.toString(), Toast.LENGTH_LONG).show();
				ErrorDialog errs = new ErrorDialog(toastText.toString());
				errs.show(getSupportFragmentManager(), "Error");
				tries += 1;
				return;
			}
		}
		catch(Exception e){
			String toastText = "Looks like you left the total blank...the total has to be filled in before submitting.";
			Toast.makeText(getApplicationContext(), toastText.toString(), Toast.LENGTH_LONG).show();
		}
	}

}
