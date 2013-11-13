package netter.uni.cis350.project;

import java.util.HashMap;

import netter.uni.database.DatabaseHandler;
import netter.uni.database.FruitStand;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class InventoryMixedBags extends Activity {
	int granolaCt;
	Bundle data;
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
	 * 10 - apple bag
	 * 11 - banana bag
	 * 12 - grape bag
	 * 13 - kiwi bag
	 * 14 - orange bag
	 * 15 - pear bag
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inventory_mixed_bags);
		data = getIntent().getExtras();
		granolaCt = data.getInt("granola");
    	// calculate new inventory based on post-processing
		
		fruitQtys = new int[16];
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
		case R.id.mixedPlus:
			changeQty(true, 8, R.id.mixedQty);
			break;
		case R.id.mixedMinus:
			changeQty(false, 8, R.id.mixedQty);
			break;
		case R.id.smoothiePlus:
			changeQty(true, 9, R.id.smoothieQty);
			break;
		case R.id.smoothieMinus:
			changeQty(false, 9, R.id.smoothieQty);
			break;
		case R.id.appleBag2Plus:
			changeQty(true, 10, R.id.appleBag2Qty);
			break;
		case R.id.appleBag2Minus:
			changeQty(false, 10, R.id.appleBag2Qty);
			break;
		case R.id.bananaBag2Plus:
			changeQty(true, 11, R.id.bananaBag2Qty);
			break;
		case R.id.bananaBag2Minus:
			changeQty(false, 11, R.id.bananaBag2Qty);
			break;
		case R.id.grapesBag2Plus:
			changeQty(true, 12, R.id.grapesBag2Qty);
			break;
		case R.id.grapesBag2Minus:
			changeQty(false, 12, R.id.grapesBag2Qty);
			break;
		case R.id.kiwiBag2Plus:
			changeQty(true, 13, R.id.kiwiBag2Qty);
			break;
		case R.id.kiwiBag2Minus:
			changeQty(false, 13, R.id.kiwiBag2Qty);
			break;
		case R.id.orangeBag2Plus:
			changeQty(true, 14, R.id.orangeBag2Qty);
			break;
		case R.id.orangeBag2Minus:
			changeQty(false, 14, R.id.orangeBag2Qty);
			break;
		case R.id.pearBag2Plus:
			changeQty(true, 15, R.id.pearBag2Qty);
			break;
		case R.id.pearBag2Minus:
			changeQty(false, 15, R.id.pearBag2Qty);
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

    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Are you selling any whole fruit?");
		builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               toInventory2();
	           }
	       });
		builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               toPricing();
	           }
	       });
		AlertDialog dialog = builder.create();
		dialog.show();
    	
    }
    private void toPricing(){
    	//Launch to transaction base
    	Intent i = new Intent(this, PricingActivity.class);
    	//create hashmap of quantities to pass on
    	HashMap<String, Integer> postinv = new HashMap<String, Integer>();
    	postinv.put("granola", granolaCt);
    	postinv.put("apple", fruitQtys[0]);
    	postinv.put("banana", fruitQtys[1]);
    	postinv.put("grape", fruitQtys[2]);
    	postinv.put("kiwi", fruitQtys[3]);
    	postinv.put("orange", fruitQtys[4]);
    	postinv.put("pear", fruitQtys[5]);
    	postinv.put("mixed", fruitQtys[8]);
    	postinv.put("smoothie", fruitQtys[9]);
    	postinv.put("apple_bags", fruitQtys[10]);
    	postinv.put("banana_bags", fruitQtys[11]);
    	postinv.put("grape_bags", fruitQtys[12]);
    	postinv.put("kiwi_bags", fruitQtys[13]);
    	postinv.put("orange_bags", fruitQtys[14]);
    	postinv.put("pear_bags", fruitQtys[15]);
    	i.putExtra("inventoryCts", postinv);
    	this.startActivity(i);
    	this.finish();
    }
    private void toInventory2(){
    	//Launch to transaction base
    	Intent i = new Intent(this, Inventory2Activity.class);
    	//create hashmap of quantities to pass on
    	HashMap<String, Integer> postinv = new HashMap<String, Integer>();
    	postinv.put("granola", granolaCt);
    	postinv.put("mixed", fruitQtys[8]);
    	postinv.put("smoothie", fruitQtys[9]);
    	postinv.put("apple_bags", fruitQtys[10]);
    	postinv.put("banana_bags", fruitQtys[11]);
    	postinv.put("grape_bags", fruitQtys[12]);
    	postinv.put("kiwi_bags", fruitQtys[13]);
    	postinv.put("orange_bags", fruitQtys[14]);
    	postinv.put("pear_bags", fruitQtys[15]);
    	i.putExtra("inventoryCts", postinv);

    	this.startActivity(i);
    	this.finish();
    }
}
