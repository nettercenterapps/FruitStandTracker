package netter.uni.cis350.project;

import java.text.DecimalFormat;

import netter.uni.database.DatabaseHandler;
import netter.uni.database.FruitStand;
import netter.uni.database.Purchase;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class TransactionBaseActivity extends Activity {
	private int transactionCt;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String _wholefruit = "0", _smoothies = "0", _bags = "0", _transactions = "0", _totalsales = "0", _granolabars = "0", _bargain = "0", _dona = "0", _total_sales_plus_bar = "0";

    	DatabaseHandler dh = DatabaseHandler.getInstance(this);
		FruitStand currentStand = dh.getCurrentFruitStand();
		int smoothieTotal = 0;
		int granolaTotal = 0;
		int bagsTotal = 0;
		int wholeTotal = 0;
		double bargainTotal = 0.0;
		double standardPriceSales = 0.0;
		double donationTotal = 0.0;
	    DecimalFormat df = new DecimalFormat("#.##");

        Purchase[] purchasesSoFar = currentStand.getPurchases(this);
        for (Purchase p : purchasesSoFar){
        	Log.w("name", p.item_name);
        	if(p.num_coupons == 0 && p.num_tradeins == 0){
        		if (p.item_name.equalsIgnoreCase("donation")){
        			donationTotal+= p.amount_cash;
        		}else if (p.item_name.equalsIgnoreCase("granola")){
        			granolaTotal+= 1;
        		}else if (p.item_name.equalsIgnoreCase("smoothie")){
        			smoothieTotal+= 1;
        		}
        		if(p.item_name.contains("BARGAIN")){
        			bargainTotal += p.amount_cash;
        		}else{
        			if(!p.item_name.contains("donation")){
	        			standardPriceSales += p.amount_cash;
	        			if(p.item_name.contains("bags")){
	        				bagsTotal += 1;
	        			}else{
	        				wholeTotal +=1;
	        			}
        			}
        		}
        	}
        }
        transactionCt = getIntent().getIntExtra("transactionCt", 0);
        
        _transactions = String.valueOf(transactionCt);
        _granolabars = String.valueOf(granolaTotal);
        _smoothies = String.valueOf(smoothieTotal);
        _bags = String.valueOf(bagsTotal);
        _dona = df.format(donationTotal);
        _totalsales = df.format(standardPriceSales);
        _wholefruit = String.valueOf(wholeTotal);
        _bargain = df.format(bargainTotal);
        _total_sales_plus_bar= df.format(bargainTotal+standardPriceSales);
        setContentView(R.layout.transaction_base_activity);
        
        TextView wholefruit = (TextView)findViewById(R.id.wholefruit);
        TextView smoothies = (TextView)findViewById(R.id.smoothies);
        TextView bags = (TextView)findViewById(R.id.mixedbags);
        TextView transactions = (TextView)findViewById(R.id.transactions);
        TextView granolabars = (TextView)findViewById(R.id.granolabars);
        TextView barginTotal = (TextView)findViewById(R.id.barginTotal);
        TextView donaTotal = (TextView)findViewById(R.id.donaTotal);
        TextView total_standard_price_salessales = (TextView)findViewById(R.id.total_standard_sales);

        TextView totalsales = (TextView)findViewById(R.id.totalsales);
        
        wholefruit.setText(_wholefruit);
        smoothies.setText(_smoothies);
        bags.setText(_bags);
        totalsales.setText(_total_sales_plus_bar);
        total_standard_price_salessales.setText(_totalsales);

        transactions.setText(_transactions); 
        granolabars.setText(_granolabars);
        barginTotal.setText(_bargain);
        donaTotal.setText(_dona);
    }
	 
	 @Override
		public void onBackPressed(){
			//back does nothing
		}

	
	//
	private void toPreCalcPep(){
		Intent i = new Intent(this,PreCalculationsPepActivity.class);
		
		this.startActivity(i);
		this.finish();
	}
	 


	public void newTransaction(View view){
		Intent i = new Intent(this, SaleActivity.class);
		i.putExtra("transactionCt", transactionCt+1);
		this.startActivity(i);
		this.finish();
	}
	public void finishSession(View view){
		//Continue on to calculations
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			//if no sales made, check if the fruit stand should really be ended
			builder.setTitle("Are you sure sales are over for today?");
			builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               toPreCalcPep();
		           }
		       });
			builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               dialog.dismiss();
		           }
		       });
		AlertDialog dialog = builder.create();
		dialog.show();
		
	}
}
