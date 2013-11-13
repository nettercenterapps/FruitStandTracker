package netter.uni.cis350.project;

import netter.uni.cis350.project.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class PreCalculationsPepActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pre_calculations_pep);
	}
	
	@Override
	public void onBackPressed(){
		//back does nothing
	}
	public void startCalculating(View v) {
    	Intent i = new Intent(this, CalculateRevenueActivity.class);
    	this.startActivity(i);
    	this.finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

}
