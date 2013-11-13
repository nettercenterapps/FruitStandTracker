package netter.uni.cis350.project;

import netter.uni.database.DatabaseHandler;
import netter.uni.database.FruitStand;
import netter.uni.database.StaffMember;
import netter.uni.database.Totals;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;

public class CashBoxAndCostActivity extends Activity {
    private boolean first=true;
	Bundle data;
	String weather = "weather not entered";
	String temperature = "temperature not entered";
	public static Intent intent;

	public double[] fruitQtys; // post-processing changes 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set school title from prior screen
		intent = getIntent();

		if (intent != null && intent.getExtras() != null) {
			weather = (String) intent.getExtras().get("weather");
			temperature = (String) intent.getExtras().get("temperature");

		} 
		setContentView(R.layout.activity_presaleinput);
		EditText fruit_cost_textbox = (EditText) findViewById(R.id.fruitcost_starting);
		fruit_cost_textbox.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View mView, MotionEvent mMotionEvent) {
				if(first){
					first=false;
					Bundle bundle = new Bundle();
					fragmStandSizeSelect fr = new fragmStandSizeSelect();
					fr.setArguments(bundle);
					fr.show(getFragmentManager(), "search radius");	
				}
			    return false;
				
			}
			
		});
		fruit_cost_textbox.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Bundle bundle = new Bundle();
				fragmStandSizeSelect fr = new fragmStandSizeSelect();
				fr.setArguments(bundle);
				fr.show(getFragmentManager(), "search radius");	
				return false;
			}
		});
		fruitQtys = new double[4];
	}
	
	@Override
	public void onBackPressed(){
		//back does nothing
	}
	public void closeAndSetFruitVal(String s){
		EditText fruit_cost_textbox = (EditText) findViewById(R.id.fruitcost_starting);
		fruitQtys[3] = Double.parseDouble(s);

		fruit_cost_textbox.setText(s);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_presaleinput, menu);
    	
		return true;
	}


	public Double getQty (int cid){
		EditText qtyEdit = (EditText) findViewById(cid);
		Editable qtyE = qtyEdit.getText();
		
		try {
			if(qtyE.toString()==""){
				return 0.0;
			}else{
			return Double.parseDouble(qtyE.toString());
			
			}
		} catch (Exception e) {
			return 0.0;
		}
	}
	

	public void qtyClicked2(View view) {
		switch(view.getId()) {
		case R.id.otherCostPlus:
			changeQty(true, 0, R.id.other_cost_starting);
			break;
		case R.id.otherCostMinus:
			changeQty(false, 0, R.id.other_cost_starting);
			break;
		case R.id.moneyInPlus:
			changeQty(true, 1, R.id.cashbox_starting);
			break;
		case R.id.moneyInMinus:
			changeQty(false, 1, R.id.cashbox_starting);
			break;
		case R.id.smoothieCostPlus:
			changeQty(true, 2, R.id.smoothie_cost_starting);
			break;
		case R.id.smoothieCostMinus:
			changeQty(false, 2, R.id.smoothie_cost_starting);
			break;
		case R.id.fruitCostPlus:
			changeQty(true, 3, R.id.fruitcost_starting);
			break;
		case R.id.fruitCostMinus:
			changeQty(false, 3, R.id.fruitcost_starting);
			break;
		
		default:
			throw new RuntimeException("Unknown Button!");
		}
	}
	
	// method that modifies fruit quantity depending on which button was pressed
	private void changeQty(boolean pm, int fruit, int cid ) {
		double qtyTemp = getQty(cid);
		
		
		fruitQtys[fruit] = qtyTemp;
		if (pm) { // increment fruit qty if it is not more than what was put in pre-processing inventory
				qtyTemp++;
		} else { // decrement fruit qty
			if (qtyTemp > 0) qtyTemp--;
		}
		EditText qtyEdit = (EditText) findViewById(cid);
		qtyEdit.setText(""+qtyTemp);	
	}
	private void saveStandToParse() {
		DatabaseHandler dh = DatabaseHandler.getInstance(this);

		FruitStand stand = dh.getCurrentFruitStand();
		ParseObject PFruitStand = new ParseObject("FruitStand");
		PFruitStand.put("school", stand.school);
		PFruitStand.put("date", stand.date);
		PFruitStand.put("temperature", stand.temperature);
		PFruitStand.put("weather", stand.weather);
		PFruitStand.put("initial_cash", stand.initial_cash);
		PFruitStand.put("stand_cost", stand.stand_cost);
		PFruitStand.put("smoothie_cost", stand.smoothie_cost);
		PFruitStand.put("additional_cost", stand.additional_cost);
		PFruitStand.saveInBackground();

		Totals totals = stand.getTotals(this);
		if (totals != null) {
			ParseObject PTotals = new ParseObject("Totals");
			PTotals.put("parent", PFruitStand);
			PTotals.put("cost", totals.cost);
			PTotals.put("revenue", totals.revenue);
			PTotals.put("final_cash", totals.final_cash);
			PTotals.saveInBackground();
		}

		StaffMember[] staff = stand.getStaffMembers(this);
		for (StaffMember s : staff) {
			ParseObject PStaffMember = new ParseObject("StaffMember");
			PStaffMember.put("parent", PFruitStand);
			PStaffMember.put("name", s.name);
			PStaffMember.put("is_volunteer", s.is_volunteer);
			PStaffMember.saveInBackground();
		}
	}
	public void continueToPricing(View v) {
    	//Launch to inventory
    	Intent i = new Intent(this, InventoryActivity.class);
    	
    	//Cashbox amount
    	EditText cshbx = (EditText) findViewById(R.id.cashbox_starting);
    	String amt = cshbx.getText().toString();

    	//fruit cost amount
    	EditText fruit_cost_textbox = (EditText) findViewById(R.id.fruitcost_starting);
    	String fruit_cost_amt = fruit_cost_textbox.getText().toString();

    	//smoothie cost amount
    	EditText smoothie_cost_textbox = (EditText) findViewById(R.id.smoothie_cost_starting);
    	String smoothie_cost_amt = smoothie_cost_textbox.getText().toString();

    	//other cost amount
    	EditText other_cost_textbox = (EditText) findViewById(R.id.other_cost_starting);
    	String other_cost_amt = other_cost_textbox.getText().toString();
    	
    	//Save our info
    	if( amt != null && fruit_cost_amt != null 
    			&& amt.length() >= 1 && fruit_cost_amt.length() >= 1 ) {
    		
    		if( other_cost_amt == null  || other_cost_amt.length()== 0 ){
    			other_cost_amt = "0";
    		}
    		if( smoothie_cost_amt == null || smoothie_cost_amt.length() == 0 ){
    			smoothie_cost_amt = "0";
    		}
            
    		DatabaseHandler dh = DatabaseHandler.getInstance(this);
    		
    		FruitStand stand_old = dh.getCurrentFruitStand();
            FruitStand stand = new FruitStand(stand_old.id,stand_old.school, (stand_old.date), Integer.parseInt(temperature), 
    				weather,Double.parseDouble(amt), Double.parseDouble(fruit_cost_amt), Double.parseDouble(smoothie_cost_amt), Double.parseDouble(other_cost_amt));
    		dh.putFruitStand(stand);
    		
        	this.startActivity(i);
        	this.finish();
    	}else{
    		String toastText = "Please enter your financial information";
    		Toast.makeText(getApplicationContext(), toastText.toString(), Toast.LENGTH_LONG).show();
    	}
	}
}
