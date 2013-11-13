package netter.uni.cis350.project;

import java.util.HashMap;

import netter.uni.cis350.project.R;
import netter.uni.database.DatabaseHandler;
import netter.uni.database.FruitStand;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Inventory2Activity extends Activity {
	//this is the default price
	double defaultPrice = 0.5;
	Bundle data;
	HashMap<String, Integer> postinv = new HashMap<String, Integer>();

	public int[] fruitQtys; // post-processing changes 
	/* table to store quantity of each item
	 * 0 - apple
	 * 1 - banana
	 * 2 - grapes
	 * 3 - kiwi
	 * 4 - orange
	 * 5 - pear
	 * 6 - granola
	 * 7 - frozen fruit
	 * 8 - mixed bags
	 * 9 - smoothie
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inventory2);
		data = getIntent().getExtras();
		postinv = (HashMap<String, Integer>) data.get("inventoryCts");
		fruitQtys = new int[10];
	}
	
	@Override
	public void onBackPressed(){
		//back does nothing
	}
	
	public void qtyClicked2(View view) {
		switch(view.getId()) {
		case R.id.apple2Plus:
			changeQty(true, 0, R.id.apple2Qty);
			break;
		case R.id.apple2Minus:
			changeQty(false, 0, R.id.apple2Qty);
			break;
		case R.id.banana2Plus:
			changeQty(true, 1, R.id.banana2Qty);
			break;
		case R.id.banana2Minus:
			changeQty(false, 1, R.id.banana2Qty);
			break;
		case R.id.grapes2Plus:
			changeQty(true, 2, R.id.grapes2Qty);
			break;
		case R.id.grapes2Minus:
			changeQty(false, 2, R.id.grapes2Qty);
			break;
		case R.id.kiwi2Plus:
			changeQty(true, 3, R.id.kiwi2Qty);
			break;
		case R.id.kiwi2Minus:
			changeQty(false, 3, R.id.kiwi2Qty);
			break;
		case R.id.orange2Plus:
			changeQty(true, 4, R.id.orange2Qty);
			break;
		case R.id.orange2Minus:
			changeQty(false, 4, R.id.orange2Qty);
			break;
		case R.id.pear2Plus:
			changeQty(true, 5, R.id.pear2Qty);
			break;
		case R.id.pear2Minus:
			changeQty(false, 5, R.id.pear2Qty);
			break;
	
		default:
			throw new RuntimeException("Unknown Button!");
		}
	}
	
	// method that modifies fruit quantity depending on which button was pressed
	private void changeQty(boolean pm, int fruit, int cid ) {
		int qtyTemp = getQty(cid);
		if (pm) { // increment fruit qty if it is not more than what was put in pre-processing inventory
				qtyTemp++;
		} else { // decrement fruit qty
			if (qtyTemp > 0) qtyTemp--;
		}
		fruitQtys[fruit] = qtyTemp;
		EditText qtyEdit = (EditText) findViewById(cid);
		qtyEdit.setText(""+qtyTemp);	
	}
	
	public int getQty (int cid){
		EditText qtyEdit = (EditText) findViewById(cid);
		Editable qtyE = qtyEdit.getText();
		
		try {
			if(qtyE.toString()==""){
				return 0;
			}else{
			return Integer.parseInt(qtyE.toString());
			
			}
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_inventory2, menu);
		return true;
	}

    public void continueToTransactionBase(View v) {
    	//create hashmap of quantities to pass on
		postinv.put("apple", fruitQtys[0]);
    	postinv.put("banana", fruitQtys[1]);
    	postinv.put("grape", fruitQtys[2]);
    	postinv.put("kiwi", fruitQtys[3]);
    	postinv.put("orange", fruitQtys[4]);
    	postinv.put("pear", fruitQtys[5]);

    	
    	//Launch to transaction base
    	Intent i = new Intent(this, PricingActivity.class);
    	i.putExtra("inventoryCts", postinv);
    	this.startActivity(i);
    	this.finish();
    }
}
