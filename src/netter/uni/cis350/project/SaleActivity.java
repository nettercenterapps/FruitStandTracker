package netter.uni.cis350.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import netter.uni.database.DatabaseHandler;
import netter.uni.database.FruitStand;
import netter.uni.database.ProcessedInventoryItem;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class SaleActivity extends Activity {

	private int temp_whole_fruit = 0;
	private int temp_bags_fruit = 0;
	private int transactionCt;
	private HashMap<String,Integer> fruitSold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        transactionCt = getIntent().getIntExtra("transactionCt", 0);
        setContentView(R.layout.activity_sale_person);
        //text views and buttons to display items for sale
        TextView wholefruittext = (TextView)findViewById(R.id.whole_fruit_label);
        TextView bagsfruittext = (TextView)findViewById(R.id.bags_fruit_label);

        TextView smoothietext = (TextView)findViewById(R.id.smoothie_label);
        TextView bagtext = (TextView)findViewById(R.id.mixed_bag_label);
        TextView granolatext = (TextView)findViewById(R.id.granola_label);
        Button whole_fruit_button = (Button)findViewById(R.id.whole_fruit_button);
        Button bags_fruit_button = (Button)findViewById(R.id.bags_fruit_button);


        Button apple_button = (Button)findViewById(R.id.apples);
        Button orange_button = (Button)findViewById(R.id.oranges);
        Button pear_button = (Button)findViewById(R.id.pears);
        Button kiwi_button = (Button)findViewById(R.id.kiwis);
        Button grape_button = (Button)findViewById(R.id.grapes);
        Button banana_button = (Button)findViewById(R.id.bananas);
        
        Button apple_button_bags = (Button)findViewById(R.id.apples_bags);
        Button orange_button_bags = (Button)findViewById(R.id.oranges_bags);
        Button pear_button_bags = (Button)findViewById(R.id.pears_bags);
        Button kiwi_button_bags = (Button)findViewById(R.id.kiwis_bags);
        Button grape_button_bags = (Button)findViewById(R.id.grapes_bags);
        Button banana_button_bags = (Button)findViewById(R.id.bananas_bags);
        
        Button smoothie_button = (Button)findViewById(R.id.smoothie_button);
        Button mixed_bag_button = (Button)findViewById(R.id.mixed_bag_button);
        Button granola_button = (Button)findViewById(R.id.granola_button);
    	TextView apple_text = (TextView)findViewById(R.id.apple_label);
    	TextView orange_text = (TextView)findViewById(R.id.orange_label);
    	TextView pear_text = (TextView)findViewById(R.id.pear_label);
    	TextView kiwi_text = (TextView)findViewById(R.id.kiwi_label);
    	TextView grape_text = (TextView)findViewById(R.id.grape_label);
    	TextView banana_text = (TextView)findViewById(R.id.banana_label);
    	
    	TextView apple_text_bags = (TextView)findViewById(R.id.apple_label_bags);
    	TextView orange_text_bags = (TextView)findViewById(R.id.orange_label_bags);
    	TextView pear_text_bags = (TextView)findViewById(R.id.pear_label_bags);
    	TextView kiwi_text_bags = (TextView)findViewById(R.id.kiwi_label_bags);
    	TextView grape_text_bags = (TextView)findViewById(R.id.grape_label_bags);
    	TextView banana_text_bags = (TextView)findViewById(R.id.banana_label_bags);
    	
    	TextView smoothie_text = (TextView)findViewById(R.id.smoothie_label);
    	TextView mixed_bag_text = (TextView)findViewById(R.id.mixed_bag_label);
    	TextView granola_text = (TextView)findViewById(R.id.granola_label);
    	
    	//get processed inventory to determine which items to display for sale
    	DatabaseHandler dh = DatabaseHandler.getInstance(this);
		FruitStand currentStand = dh.getCurrentFruitStand();
		ProcessedInventoryItem[] PI = currentStand.getProcessedInventoryItems(this);
		HashMap<String,Integer> inv = new HashMap<String,Integer>();
		fruitSold = new HashMap<String,Integer>();

		for (int i = 0; i < PI.length; i++){
			inv.put(PI[i].item_name, PI[i].count);
		}
    	boolean anyWholeFruit = false;
    	boolean anyBagsFruit = false;
    	//set the items invisible if they are not being sold today
    	if(inv.get("apple") == 0){
    		apple_button.setVisibility(8);
    		apple_text.setVisibility(8);
    	}else{
    		anyWholeFruit = true;
    	}
    	if(inv.get("orange") == 0){
    		orange_button.setVisibility(8);
    		orange_text.setVisibility(8);
    	}else{
    		anyWholeFruit = true;
    	}
    	if(inv.get("pear") == 0){
    		pear_button.setVisibility(8);
    		pear_text.setVisibility(8);
    	}else{
    		anyWholeFruit = true;
    	}
    	if(inv.get("kiwi") == 0){
    		kiwi_button.setVisibility(8);
    		kiwi_text.setVisibility(8);
    	}else{
    		anyWholeFruit = true;
    	}
    	if(inv.get("grape") == 0){
    		grape_button.setVisibility(8);
    		grape_text.setVisibility(8);
    	}
    	
    	else{
    		anyWholeFruit = true;
    	}
    	if(inv.get("banana") == 0){
    		banana_button.setVisibility(8);
    		banana_text.setVisibility(8);
    	}else{
    		anyWholeFruit = true;
    	}
    	if(!anyWholeFruit){
    		whole_fruit_button.setVisibility(8);
    		wholefruittext.setVisibility(8);
    	}
    	if(inv.get("apple_bags") == 0){
    		apple_button_bags.setVisibility(8);
    		apple_text_bags.setVisibility(8);
    	}else{
    		anyBagsFruit = true;
    	}
    	if(inv.get("orange_bags") == 0){
    		orange_button_bags.setVisibility(8);
    		orange_text_bags.setVisibility(8);
    	}else{
    		anyBagsFruit = true;
    	}
    	if(inv.get("pear_bags") == 0){
    		pear_button_bags.setVisibility(8);
    		pear_text_bags.setVisibility(8);
    	}else{
    		anyBagsFruit = true;
    	}
    	if(inv.get("kiwi_bags") == 0){
    		kiwi_button_bags.setVisibility(8);
    		kiwi_text_bags.setVisibility(8);
    	}else{
    		anyBagsFruit = true;
    	}
    	if(inv.get("grape_bags") == 0){
    		grape_button_bags.setVisibility(8);
    		grape_text_bags.setVisibility(8);
    	}
    	
    	else{
    		anyBagsFruit = true;
    	}
    	if(inv.get("banana_bags") == 0){
    		banana_button_bags.setVisibility(8);
    		banana_text_bags.setVisibility(8);
    	}else{
    		anyBagsFruit = true;
    	}
    	if(inv.get("mixed") == 0){
    		mixed_bag_button.setVisibility(8);
    		mixed_bag_text.setVisibility(8);
    	}else{
    		anyBagsFruit = true;
    	}
    	if(!anyBagsFruit){
    		bags_fruit_button.setVisibility(8);
    		bagsfruittext.setVisibility(8);
    	}
    	if(inv.get("smoothie") == 0){
    		smoothie_button.setVisibility(8);
    		smoothie_text.setVisibility(8);
    	}
    	
    	if(inv.get("granola") == 0){
    		granola_button.setVisibility(8);
    		granola_text.setVisibility(8);
    	}
        bagsfruittext.setText("x" + 0);

        wholefruittext.setText("x" + 0);
        smoothietext.setText("x" + 0);
        bagtext.setText("x" + 0);
        granolatext.setText("x" + 0);
        
    }
    
    @Override
	public void onBackPressed(){
		//back does nothing
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_start, menu);
        return true;
    }
    
    //method to expand button menu of the app -> show the fruit
    public void whole_fruit_clicked(View view){
    	LinearLayout fruitview = (LinearLayout)findViewById(R.id.fruitchoice);
    	if(fruitview.getVisibility() == View.VISIBLE){
    		fruitview.setVisibility(View.GONE);
    	} else { fruitview.setVisibility(View.VISIBLE); }
    }
    public void bags_fruit_clicked(View view){
    	LinearLayout fruitview = (LinearLayout)findViewById(R.id.fruitchoice_bags);
    	if(fruitview.getVisibility() == View.VISIBLE){
    		fruitview.setVisibility(View.GONE);
    	} else { fruitview.setVisibility(View.VISIBLE); }
    }
    
    //called by other methods in this class
    private void updateWholeFruit(){
    	TextView text = (TextView)findViewById(R.id.whole_fruit_label);
    	temp_whole_fruit +=1;
    	text.setText("x" + temp_whole_fruit);
        
    }
    private void updateBagsFruit(){
    	TextView text = (TextView)findViewById(R.id.bags_fruit_label);
    	temp_bags_fruit +=1;
    	text.setText("x" + temp_bags_fruit);
    }
    
    public String itemClicked(String itemName){
    	int count = 0;
    	if (fruitSold.containsKey(itemName)){
    		count = fruitSold.get(itemName);
    	}
    	count +=1;
    	fruitSold.put(itemName, count);
    	return ""+count;
    }
    
    public void smoothie_clicked(View view){
    	TextView text = (TextView)findViewById(R.id.smoothie_label);
    	String numSold = itemClicked("smoothie");
    	text.setText("x" + numSold);
    }
    
    public void bags_clicked(View view){
    	LinearLayout fruitview = (LinearLayout)findViewById(R.id.fruitchoice_bags);
    	if(fruitview.getVisibility() == View.VISIBLE){
    		fruitview.setVisibility(View.GONE);
    	} else { fruitview.setVisibility(View.VISIBLE); }
    }
    
    public void mixed_bag_clicked(View view){
    	TextView text = (TextView)findViewById(R.id.mixed_bag_label);
    	String numSold = itemClicked("mixed");
    	text.setText("x" + numSold);
    	updateBagsFruit();

    }

    public void granola_clicked(View view){
    	TextView text = (TextView)findViewById(R.id.granola_label);
    	String numSold = itemClicked("granola");
    	text.setText("x" + numSold);
    }
    
    public void apples_button(View view){
    	TextView text = (TextView)findViewById(R.id.apple_label);
    	String numSold = itemClicked("apple");
    	text.setText("x" + numSold);
    	updateWholeFruit();
    }
    public void oranges_button(View view){
    	TextView text = (TextView)findViewById(R.id.orange_label);
    	String numSold = itemClicked("orange");
    	text.setText("x" + numSold);
    	updateWholeFruit();
    }
    
    public void bananas_button(View view){
    	TextView text = (TextView)findViewById(R.id.banana_label);
    	String numSold = itemClicked("banana");
    	text.setText("x" + numSold);
    	updateWholeFruit();
    }
    
    public void grapes_button(View view){
    	TextView text = (TextView)findViewById(R.id.grape_label);
    	String numSold = itemClicked("grape");
    	text.setText("x" + numSold);
    	updateWholeFruit();
    }
    
    public void kiwis_button(View view){
    	TextView text = (TextView)findViewById(R.id.kiwi_label);
    	String numSold = itemClicked("kiwi");
    	text.setText("x" + numSold);
    	updateWholeFruit();
    }
    
    
    
    public void pears_button(View view){
    	TextView text = (TextView)findViewById(R.id.pear_label);
    	String numSold = itemClicked("pear");
    	text.setText("x" + numSold);
    	updateWholeFruit();
    }
    
    public void apples_bags_button(View view){
    	TextView text = (TextView)findViewById(R.id.apple_label_bags);
    	String numSold = itemClicked("apple_bags");
    	text.setText("x" + numSold);
    	updateBagsFruit();
    }
    public void oranges_bags_button(View view){
    	TextView text = (TextView)findViewById(R.id.orange_label_bags);
    	String numSold = itemClicked("orange_bags");
    	text.setText("x" + numSold);
    	updateBagsFruit();
    }
    
    public void bananas_bags_button(View view){
    	TextView text = (TextView)findViewById(R.id.banana_label_bags);
    	String numSold = itemClicked("banana_bags");
    	text.setText("x" + numSold);
    	updateBagsFruit();
    }
    
    public void grapes_bags_button(View view){
    	TextView text = (TextView)findViewById(R.id.grape_label_bags);
    	String numSold = itemClicked("grape_bags");
    	text.setText("x" + numSold);
    	updateBagsFruit();
    }
    
    public void kiwis_bags_button(View view){
    	TextView text = (TextView)findViewById(R.id.kiwi_label_bags);
    	String numSold = itemClicked("kiwi_bags");
    	text.setText("x" + numSold);
    	updateBagsFruit();
    }
    
    
    
    public void pears_bags_button(View view){
    	TextView text = (TextView)findViewById(R.id.pear_label_bags);
    	String numSold = itemClicked("pear_bags");
    	text.setText("x" + numSold);
    	updateBagsFruit();
    }
    
    
    public void clear_entries(View view){
    	//clear the fruit sold, bags and whole totals, and reset the visible counters to x0
    	fruitSold.clear();
    	temp_bags_fruit = 0;
    	temp_whole_fruit = 0;
    	int[] texts = {R.id.whole_fruit_label, R.id.apple_label,
    			R.id.orange_label, R.id.pear_label, R.id.kiwi_label, R.id.grape_label,
    			R.id.banana_label, R.id.smoothie_label, R.id.mixed_bag_label,R.id.granola_label,
    			R.id.bags_fruit_label, R.id.apple_label_bags,
    			R.id.orange_label_bags, R.id.pear_label_bags, R.id.kiwi_label_bags,
    			R.id.banana_label_bags, R.id.grape_label_bags,
    	};
    	for(int i: texts){
    		TextView text = (TextView)findViewById(i);
    		text.setText("x0");
    	}
    }
    
    public void go_to_checkout(View view){
    	Intent i = new Intent(this, PaymentActivity.class);	
    	Spinner age = (Spinner)findViewById(R.id.choosePerson);
    	String grade = (String)age.getSelectedItem();
    	//pass on fruit sold, the age of the person buying, and the number of the transaction
    	i.putExtra("fruitSold", fruitSold);
    	i.putExtra("grade", grade);
		i.putExtra("transactionCt", transactionCt);

    	this.startActivity(i);
    	this.finish();
    }
}
