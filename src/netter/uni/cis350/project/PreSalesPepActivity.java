package netter.uni.cis350.project;

import netter.uni.cis350.project.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class PreSalesPepActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pre_sales_pep);
	}
	
	@Override
	public void onBackPressed(){
		//back does nothing
	}
	public void startSelling(View v) {
    	Intent i = new Intent(this, TransactionBaseActivity.class);
    	this.startActivity(i);
    	this.finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_pre_sales_pep, menu);
		return true;
	}

}
