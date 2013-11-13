package netter.uni.cis350.project;

import java.text.DecimalFormat;

import netter.uni.database.DatabaseHandler;
import netter.uni.database.EndInventoryItem;
import netter.uni.database.FruitStand;
import netter.uni.database.ProcessedInventoryItem;
import netter.uni.database.Purchase;
import netter.uni.database.StaffMember;
import netter.uni.database.StartInventoryItem;
import netter.uni.database.Totals;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

public class CalculateProfitActivity extends FragmentActivity {
	boolean logAndEnd = false;
	double revenue;
	double fruitCosts;
	double smoothieCosts;
	double otherCosts;
	double totalComputed;
	double profitComputed;
	double endingCashbox;
	double startingCashbox;

    DecimalFormat df = new DecimalFormat("#.##");
    String messageToDataTeam = "no message added";
	double totalReal;
	double profitReal;
	int mathOverride = 0;
	public static class ErrorDialog extends DialogFragment {
		String msg;
		
		public ErrorDialog(String msg) {//
			this.msg = msg;
		}
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(msg).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
	        return builder.create();
	    }
	}
	
	
	int errors = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculate_profit);

    	DatabaseHandler dh = DatabaseHandler.getInstance(this);
		FruitStand currentStand = dh.getCurrentFruitStand();
		smoothieCosts = currentStand.smoothie_cost;
		otherCosts = currentStand.additional_cost;
		fruitCosts = currentStand.stand_cost;
		startingCashbox = currentStand.initial_cash;
		TextView revenuevalue = (TextView)findViewById(R.id.revenueValue);
	//	Double d =  db.getCashTot()+db.getDonTot()+db.getBarTot()-db.getJunkTot()-db.getCoupTot();
		revenue = getIntent().getDoubleExtra("revenue",0.0);
		revenuevalue.setText("$"+df.format(revenue));
		
		TextView originalCash = (TextView)findViewById(R.id.originalCash);
		originalCash.setText("Originally, you had $" + startingCashbox + "\n in the cash box");

		TextView fruitSpent = (TextView)findViewById(R.id.fruitStandCostHint);
		
		Double f = fruitCosts;
		String leasta = "What size fruit stand do you have? <br>";
		String leastb = " <br> Medium = $11.00 <br> Large = $14.00 <br> Jumbo = $20.00";
		String leastbold = "<b>Small = "+f+" :</b>";
		
		String meda = "What size fruit stand do you have? <br> Small = $8.00 <br>";
		String medb = "  <br> Large = $14.00 <br> Jumbo = $20.00";
		String medbold = "<b>Medium = "+f+" :</b>";
		
		String largea = "What size fruit stand do you have? <br>Small = $8.00  <br> Medium = $11.00 <br>";
		String largeb = " <br> Jumbo = $20.00";
		String largebold = "<b>Large = "+f+" :</b>";
		
		String greata = "What size fruit stand do you have? <br> Small = $8.00 <br> Medium = $11.00 <br> Large = $14.00<br> ";
		String greatb = " ";
		String greatbold = "<b>Jumbo = "+f+" :</b>";
		String newV = "What size fruit stand do you have? <br> Small = $8.00 <br> Medium = $11.00 <br> Large = $14.00<br> Jumbo = $20.00 <br> ";
		String newVplus = newV + "<b>Custom = $" +f+" :</b>";
		if(f == 8.0){
			fruitSpent.setText(Html.fromHtml(leasta+leastbold+leastb));
		}else if(f == 11.0){
			fruitSpent.setText(Html.fromHtml(meda+medbold+medb));

		}else if(f == 14.0){
			fruitSpent.setText(Html.fromHtml(largea+largebold+largeb));

		}else if(f == 20.0){
			fruitSpent.setText(Html.fromHtml(greata+greatbold+greatb));

		}else{
			fruitSpent.setText(Html.fromHtml(newVplus));
		}

		TextView smooSpent = (TextView)findViewById(R.id.smoothieCostHint);
		smooSpent.setText(getResources().getString(R.string.smoothie_hint)+" " + smoothieCosts);
		


		TextView otherSpent = (TextView)findViewById(R.id.otherCostsHint);
		otherSpent.setText(getResources().getString(R.string.other_cost_hint)+" " + otherCosts);
		
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

	private double getDoubleFromEditText(int id) {
		EditText et = (EditText) findViewById(id);
		return Double.valueOf(et.getText().toString());
	}

	public void highlightEditTextById(int id, boolean on) {
		EditText et = (EditText) findViewById(id);//
		et.setBackgroundColor(on ? Color.argb(90, 255, 255, 0): Color.argb(90, 255, 255, 255));
	}
	
	public void sendMessageDialog(){
		AlertDialog.Builder editalert = new AlertDialog.Builder(this);

		editalert.setTitle("Send Message to the Data Team");
		editalert.setMessage("Is there anything else the data team should know about?");


		final EditText input = new EditText(this);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
		        LinearLayout.LayoutParams.MATCH_PARENT,
		        LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		editalert.setView(input);

		editalert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    	String s = input.getText().toString();
		    	if(s.equals(null)||s.equals("")||s.equals(".")){
		    		messageToDataTeam = "no message added";
		    	}else{
		    		messageToDataTeam = s;
		    	}
		    	logDataAndEndFruitStand();
		    }
		});


		editalert.show();
    }
	
	public void checkCalculations(View v) {
		

		// Get all the doubles from the user input
		try {
			highlightEditTextById(R.id.totalCosts,false);
			highlightEditTextById(R.id.profit,false);
			highlightEditTextById(R.id.endingCashbox,false);
			//revenue = db.getCashTot()+db.getDonTot()+db.getBarTot();
			revenue = Double.parseDouble(df.format(revenue));
			fruitCosts = getDoubleFromEditText(R.id.fruitStandCost);
			smoothieCosts = getDoubleFromEditText(R.id.smoothieCost);
			otherCosts = getDoubleFromEditText(R.id.otherCosts);
			totalComputed = getDoubleFromEditText(R.id.totalCosts);
			profitComputed = getDoubleFromEditText(R.id.profit);
			endingCashbox = getDoubleFromEditText(R.id.endingCashbox);
	
			String[] errorMsgs = {
					"Looks like there were some differences in your numbers!\n", "", "",
					"","","","", "Try again\n" };
	
			// Only prevent them from moving on the first time.
			boolean errors = false;
			totalReal = fruitCosts + smoothieCosts + otherCosts;
			if (Math.abs(totalReal - totalComputed) > .05) {
				errorMsgs[1] = "Total costs should be equal to fruit costs + smoothie costs + other costs.\n";
				errors = true;
				highlightEditTextById(R.id.totalCosts,true);
			}
			profitReal = revenue  - totalReal;
			if (Math.abs(profitReal - profitComputed) > .05) {
				errorMsgs[2] = "Profit should equal revenue - total costs\n";
				errors = true;
				highlightEditTextById(R.id.profit,true);
			}
			if(fruitCosts != fruitCosts){
				errorMsgs[4] = "Check fruit stand cost\n";
				errors = true;
				highlightEditTextById(R.id.fruitStandCost,true);
			}
			if(smoothieCosts != smoothieCosts){
				errorMsgs[5] = "Check smoothie cost\n";
				errors = true;
				highlightEditTextById(R.id.smoothieCost,true);
			}
			if(otherCosts != otherCosts){
				errorMsgs[6] = "Check other cost\n";
				errors = true;
				highlightEditTextById(R.id.otherCosts,true);
			}
			double realEndingCash = revenue + startingCashbox;
			if (Math.abs(endingCashbox - realEndingCash) > .50) {
				errorMsgs[3] = "Ending cash should equal starting cash + revenue \n"
						+ "Are you sure you counted all of the cash?\n";
				errors = true;
				highlightEditTextById(R.id.endingCashbox,true);
			}
			if(errors && this.errors < 3) {
				this.errors++;
				if(this.errors == 3) {
					checkCalculations(null);
					return;
				}
				StringBuffer toastText = new StringBuffer();
				if(errorMsgs[4].equals("Check fruit stand cost\n")){
					toastText.append(errorMsgs[4]);
				}else if(errorMsgs[5].equals("Check smoothie cost\n")){
					toastText.append(errorMsgs[5]);
				}else if(errorMsgs[6].equals("Check other cost\n")){
					toastText.append(errorMsgs[6]);
				}else{
					for(int i=0; i< errorMsgs.length;i++) {
						toastText.append(errorMsgs[i]);
					}
				}
				ErrorDialog errs = new ErrorDialog(toastText.toString());
				errs.show(getSupportFragmentManager(), "Error");
				return;
			} else if(errors){
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("We think your math is wrong, do you want to record this as an official transaction,\n or give it another shot?");
				builder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   mathOverride +=1;
			        	   sendMessageDialog();
			           }
			       });
				builder.setNegativeButton("Give it another shot", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               dialog.dismiss();
			           }
			       });
				AlertDialog dialog = builder.create();
				dialog.show();
			}else{
				sendMessageDialog();
			}
					
			
		} catch (Exception e) {
			e.printStackTrace();
			String toastText = "Looks like you left a box blank, everything has to be filled in before submitting.";
			Toast.makeText(getApplicationContext(), toastText.toString(), Toast.LENGTH_LONG).show();
		}
	}
	private void logDataAndEndFruitStand(){
	
		Intent i = new Intent(this, FinishedPepActivity.class);
		i.putExtra("profitable", profitReal > 0);
		DatabaseHandler dh = DatabaseHandler.getInstance(this);
		FruitStand currentStand = dh.getCurrentFruitStand();
		//save totals to database
		currentStand.addTotals(this, totalReal, revenue, endingCashbox, mathOverride, messageToDataTeam);
		this.startActivity(i);
		this.finish();
	}
}
