package netter.uni.cis350.project;


import java.util.HashMap;

import netter.uni.database.DatabaseHandler;
import netter.uni.database.FruitStand;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class InventoryActivity extends Activity {

	int[] fruitQtys;
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
		setContentView(R.layout.activity_inventory);
		
		fruitQtys = new int[10];
	}
	
	@Override
	public void onBackPressed(){
		//back does nothing
	}
	
	public void qtyClicked(View view) {
		switch(view.getId()) {
		case R.id.applePlus:
			changeQty(true, 0, R.id.appleQty);
			break;
		case R.id.appleMinus:
			changeQty(false, 0, R.id.appleQty);
			break;
		case R.id.bananaPlus:
			changeQty(true, 1, R.id.bananaQty);
			break;
		case R.id.bananaMinus:
			changeQty(false, 1, R.id.bananaQty);
			break;
		case R.id.grapesPlus:
			changeQty(true, 2, R.id.grapesQty);
			break;
		case R.id.grapesMinus:
			changeQty(false, 2, R.id.grapesQty);
			break;
		case R.id.kiwiPlus:
			changeQty(true, 3, R.id.kiwiQty);
			break;
		case R.id.kiwiMinus:
			changeQty(false, 3, R.id.kiwiQty);
			break;
		case R.id.orangePlus:
			changeQty(true, 4, R.id.orangeQty);
			break;
		case R.id.orangeMinus:
			changeQty(false, 4, R.id.orangeQty);
			break;
		case R.id.pearPlus:
			changeQty(true, 5, R.id.pearQty);
			break;
		case R.id.pearMinus:
			changeQty(false, 5, R.id.pearQty);
			break;
		case R.id.granolaPlus:
			changeQty(true, 6, R.id.granolaQty);
			break;
		case R.id.granolaMinus:
			changeQty(false, 6, R.id.granolaQty);
			break;
		case R.id.frozenPlus:
			changeQty(true, 7, R.id.frozenQty);
			break;
		case R.id.frozenMinus:
			changeQty(false, 7, R.id.frozenQty);
			break;
		default:
			throw new RuntimeException("Unknown Button!");
		}
	}
	
	// method that modifies fruit quantity depending on which button was pressed
	private void changeQty(boolean increased, int fruitIndex, int widgetId ) {
		int qtyTemp = getQty(widgetId);
		if (increased) { // increment fruit qty
			if (qtyTemp < 99) qtyTemp++;
		} else { // decrement fruit qty
			if (qtyTemp > 0) qtyTemp--;
		}
		
		fruitQtys[fruitIndex] = qtyTemp;
		EditText qtyEdit = (EditText) findViewById(widgetId);
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
		getMenuInflater().inflate(R.menu.activity_inventory, menu);
		return true;
	}
	
	public void continueToInventory2(View v) {
    	//Launch to inventory2
    	Intent i = new Intent(this, InventoryMixedBags.class);
    	//Save our info
    	//store items as start inventory items in the database
    	DatabaseHandler dh = DatabaseHandler.getInstance(this);
		FruitStand currentStand = dh.getCurrentFruitStand();
		currentStand.addStartInventoryItem(this, "apple", getQty(R.id.appleQty));
		currentStand.addStartInventoryItem(this, "banana", getQty(R.id.bananaQty));
		currentStand.addStartInventoryItem(this, "grape", getQty(R.id.grapesQty));
		currentStand.addStartInventoryItem(this, "kiwi", getQty(R.id.kiwiQty));
		currentStand.addStartInventoryItem(this, "orange", getQty(R.id.orangeQty));
		currentStand.addStartInventoryItem(this, "pear", getQty(R.id.pearQty));
		currentStand.addStartInventoryItem(this, "frozen", getQty(R.id.frozenQty));
		currentStand.addStartInventoryItem(this, "granola", getQty(R.id.granolaQty));

		//granola is passed on as well
		
		i.putExtra("granola", getQty(R.id.granolaQty));
    	this.startActivity(i);
    	this.finish();
    }

}
