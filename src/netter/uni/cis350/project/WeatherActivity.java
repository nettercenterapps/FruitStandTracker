package netter.uni.cis350.project;

import netter.uni.cis350.project.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class WeatherActivity extends Activity {
	private SeekBar seekBar;
	private String w = "sunny";
	private String tempText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		
		seekBar = (SeekBar) findViewById(R.id.temp_bar);
		seekBar.setProgress(50);
		seekBar.incrementProgressBy(1);
		seekBar.setMax(100);
		final TextView seekBarValue = (TextView) findViewById(R.id.temp_reading);
		tempText = "Temperature: 50F";
		seekBarValue.setText(tempText);

		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

		    @Override
		    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		    	tempText = "Temperature: " + String.valueOf(progress) + "F";
		        seekBarValue.setText(tempText);
		    }

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// Don't do anything
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// Don't do anything
				
			}
		});

	}
	
	@Override
	public void onBackPressed(){
		//back does nothing
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_weather, menu);
		return true;
	}
	
	public String getTemp(){
		return tempText;
	}
	
	public void setWeather(View v) {
		switch(v.getId()) {
		case R.id.sunny:
			w = "sunny";
			break;
		case R.id.rainy:
			w = "rainy";
			break;
		case R.id.partly_cloudy:
			w = "partly cloudy";
			break;
		case R.id.snowy:
			w = "snowy";
			break;
		}
	}
	
	public String getWeather(){
		return w;
	}

	public void continueToPricing(View v) {
    	//Launch to inventory
    	Intent i = new Intent(this, CashBoxAndCostActivity.class);
    	
    	
    	//Save our info
    	if(w != null ) {
            i.putExtra("weather", w);
            i.putExtra("temperature", String.valueOf(seekBar.getProgress()));
        	this.startActivity(i);
        	this.finish();
    	}
	}
}
