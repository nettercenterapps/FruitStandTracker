package netter.uni.cis350.project;

import java.util.Calendar;

import netter.uni.database.DatabaseHandler;
import netter.uni.database.FruitStand;
import netter.uni.database.StaffMember;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends Activity {

	//variables for school information
	private LinearLayout schoolLayout;
	private EditText addedSchool;
	private String school;
	
	//variables for the date
	private TextView saleDisplayDate;
	private Button changeDate;
	static final int DATE_DIALOG_ID = 999;
	private int year;
	private int month;
	private int day;
	
	//variables for adding volunteers
	private Button addVol;
	private LinearLayout volLayout;
	private int id;
	private String vol1;
	private String vol2;
	private String vol3;
	private String vol4;
	private String vol5;
	private String vol6;
	private String vol7;
	private String vol8;
	
	//variables for adding staff members
	public Button addStaff;
	private LinearLayout staffLayout;
	private int sId;
	public String staff1;
	public String staff2;
	public String staff3;
	public String staff4;
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        
        final Spinner spinner = (Spinner) findViewById(R.id.school);
        
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
             R.array.school_list, android.R.layout.simple_spinner_item);
     
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	int i = (int) id;
            	//creates a new EditText if the user would like to input their school
            	//20 is the id of the Add a School option of the School Spinner (school_list)
            	if(i == 20){
            		school = "check";//variable value that will alert an update method to act
            		Log.i("adding", "This is indeed the add a school option");
            		schoolLayout = (LinearLayout) findViewById(R.id.additional_school);
            		addedSchool = new EditText(InfoActivity.this);
            		addedSchool.setId(100);//completely arbitrary id
            		addedSchool.setWidth(LayoutParams.MATCH_PARENT);
            		addedSchool.setHeight(LayoutParams.WRAP_CONTENT);
            		addedSchool.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            		addedSchool.setHint(R.string.school_hint);
            		schoolLayout.addView(addedSchool);	
            	}
            	else{
            		school = (String)spinner.getSelectedItem();
            	}
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        //for the date picker
        setCurrentDateOnView();
		addListenerDate();
		
		//for the volunteer adder
		addListenerAddVol();
		volLayout = (LinearLayout) findViewById(R.id.volunteer_list);  
        
        //for the staff adder
        addListenerAddStaff();
        staffLayout = (LinearLayout) findViewById(R.id.staff_list);

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
     
    
    // display current date
	private void setCurrentDateOnView() {
 
		saleDisplayDate = (TextView) findViewById(R.id.saleDate);
 
	    //auto populating the date
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
 
		// set current date into textview
		saleDisplayDate.setText(
				Integer.toString(month + 1) + "-" + 
				Integer.toString(day) + "-" + 
				Integer.toString(year) + " ");
	}
 
	private void addListenerDate() {
		changeDate = (Button) findViewById(R.id.change_date);
		changeDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
 
		});
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
		   // set date picker as current date
		   return new DatePickerDialog(this, datePickerListener, 
                         year, month,day);
		}
		return null;
	}
	
 
	private DatePickerDialog.OnDateSetListener datePickerListener 
    		= new DatePickerDialog.OnDateSetListener() {

			// when dialog box is closed, below method will be called.
			public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
				year = selectedYear;
				month = selectedMonth;
				day = selectedDay;

				// set selected date into textview
				saleDisplayDate.setText(
						Integer.toString(month + 1) + "-" + 
						Integer.toString(day) + "-" + 
						Integer.toString(year) + " ");
			}
		};
		
		private void addListenerAddVol(){
			id = 4;
			addVol = (Button) findViewById(R.id.add_volunteer);
			
			addVol.setOnClickListener(new View.OnClickListener() {
				 
				@Override
				public void onClick(View v) {
					//I'm only going to allow for 8 volunteers at this time
					//four original volunteers, plus 4 volunteers that may be added
					//in addition
					if(id < 8){
						moreVolunteers();
					}
				}
	 
			});
		}
		
		//method that adds a volunteer edittext
		private void moreVolunteers(){
				
				EditText newVol = new EditText(this);
				newVol.setId(id);
				id++;
				newVol.setWidth(LayoutParams.MATCH_PARENT);
				newVol.setHeight(LayoutParams.WRAP_CONTENT);
				newVol.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
				newVol.setHint(R.string.names);
				volLayout.addView(newVol);
			
		}
	
		private void addListenerAddStaff(){
			sId = 12;
			addStaff = (Button) findViewById(R.id.add_staff);
			
			addStaff.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					//I'm only going to allow for 4 staff members at this time
					//two original staff members, plus 2 staff members that may be
					//added in addition
					if(sId < 14){
						moreStaff();
					}					
				}
			});
			
		}
		
		//method that adds a staff edittext
		private void moreStaff(){
			Log.i("more staff", "got here");
			EditText newStaff = new EditText(this);
			newStaff.setId(sId);
			sId++;
			newStaff.setWidth(LayoutParams.MATCH_PARENT);
			newStaff.setHeight(LayoutParams.WRAP_CONTENT);
			newStaff.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
			newStaff.setHint(R.string.names);
			staffLayout.addView(newStaff);	
		}
    
		//if a request has been made to add a school, change the
		//value of school to the value in the text box
		//this can be determined by the current value of school -> "check"
		//else, do nothing
		public void updateSchool(){
			if(!school.equals("check")){
				return;
			}
			if(addedSchool.getText() != null)
	    		school = addedSchool.getText().toString();
		}
		
		//fills the volunteer string variables for later usage
		private void fillVolunteers(){
			if(((EditText) findViewById(R.id.volunteer1)).getText() != null)
				vol1 = ((EditText) findViewById(R.id.volunteer1)).getText().toString();
			if(((EditText) findViewById(R.id.volunteer2)).getText() != null)
				vol2 = ((EditText) findViewById(R.id.volunteer2)).getText().toString();
			if(((EditText) findViewById(R.id.volunteer3)).getText() != null)
				vol3 = ((EditText) findViewById(R.id.volunteer3)).getText().toString();
			if(((EditText) findViewById(R.id.volunteer4)).getText() != null)
				vol4 = ((EditText) findViewById(R.id.volunteer4)).getText().toString();
			if(id >= 5 && ((EditText) findViewById(4)).getText() != null)
				vol5 = ((EditText) findViewById(4)).getText().toString();
			if(id > 6 && ((EditText) findViewById(5)).getText() != null)
				vol6 = ((EditText) findViewById(5)).getText().toString();
			if(id > 7 && ((EditText) findViewById(6)).getText() != null)
				vol7 = ((EditText) findViewById(6)).getText().toString();
			if(id >= 8 && ((EditText) findViewById(7)).getText() != null)
				vol8 = ((EditText) findViewById(7)).getText().toString();
		}
		
		//fills the staff string variables for later usage
		private void fillStaff(){
			if(((EditText) findViewById(R.id.staff1)).getText() != null)
				staff1 = ((EditText) findViewById(R.id.staff1)).getText().toString();
			if(((EditText) findViewById(R.id.staff2)).getText() != null)
				staff2 = ((EditText) findViewById(R.id.staff2)).getText().toString();
			if(sId >= 13 && ((EditText) findViewById(12)).getText() != null)
				staff3 = ((EditText) findViewById(12)).getText().toString();
			if(sId >= 14 && ((EditText) findViewById(13)).getText() != null)
				staff4 = ((EditText) findViewById(13)).getText().toString();
		}
		
		public String getYear(){
			return Integer.toString(year);
		}
		
		public String getMonth(){
			return Integer.toString(month + 1) ;
		}
		
		public String getDay(){
			return Integer.toString(day);
		}
		
		public String getSchool(){
			return school;
		}
		
		
		//method to test whether the volunteers are being input correctly. 
		//input is an array of 8 strings, which are then checked against
		//the volunteer variables of this class
		//fillVolunteers() is called in this class, so it does not need to 
		//be called in the test case
		public boolean verifyVolunteers(String[] volunteers){
			//check to see that there are 8 volunteer slots in the array
			if(volunteers.length != 8){
				return false;
			}
			fillVolunteers();
			if(vol1 == null){
				if(volunteers[0] != null){
					return false;
				}
			}
			if(vol1 != null && volunteers[0] != null && !volunteers[0].equals(vol1)){
				return false;
			}
			
			if(vol2 == null){
				if(volunteers[1] != null){
					return false;
				}
			}
			if(vol2 != null && volunteers[1] != null && !volunteers[1].equals(vol2)){
				return false;
			}
			
			if(vol3 == null){
				if(volunteers[2] != null){
					return false;	
				}
			}
			if(vol3 != null && volunteers[2] != null && !vol3.equals(volunteers[2])){
				return false;
			}
			
			if(vol4 == null){
				if(volunteers[3] != null){
					return false;
				}
			}
			if(vol4 != null && volunteers[3] != null && !vol4.equals(volunteers[3])){
				return false;
			}
			
			if(vol5 == null){
				if(volunteers[4] != null){
					return false;
				}
			}
			if(vol5 != null && volunteers[4] != null && !vol5.equals(volunteers[4])){
				return false;
			}
			
			if(vol6 == null){
				if(volunteers[5] != null){
					return false;
				}
			}
			if(vol6 != null && volunteers[5] != null && !vol6.equals(volunteers[5])){
				return false;
			}
			
			if(vol7 == null){
				if(volunteers[6] != null){
					return false;
				}
			}
			if(vol7 != null && volunteers[6] != null && !vol7.equals(volunteers[6])){ 
				return false;
			}
			
			if(vol8 == null){
				if(volunteers[7] != null){
					return false;
				}
			}
			if(vol8 != null && volunteers[7] != null && !vol8.equals(volunteers[7])){ 
				return false;
			}
			return true;
		}
		
		public boolean verifyStaff(String[] staff){
			if(staff.length != 4) return false;
			fillStaff();
			
			if(staff1 == null){
				if(staff[0] != null){
					return false;
				}
			}
			if(staff1 != null && staff[0] != null && !staff[0].equals(staff1)){
				return false;
			}
			
			if(staff2 == null){
				if(staff[1] != null){
					return false;
				}
			}
			if(staff2 != null && staff[1] != null && !staff[1].equals(staff2)){
				return false;
			}
			
			if(staff3 == null){
				if(staff[2] != null){
					return false;
				}
			}
			if(staff3 != null && staff[2] != null && !staff[2].equals(staff3)){
				return false;
			}
			
			if(staff4 == null){
				if(staff[3] != null){
					return false;
				}
			}
			if(staff4 != null && staff[3] != null && !staff[3].equals(staff4)){
				return false;
			}
			return true;
		}
	
	public int getSchoolId(){
		//here we assign an id to every school 
       
		if(school.equals("Paul Robeson HS")){
			return 001;
		}
		if(school.equals("Universal Charter AudenReid HS")){
			return 002;
		}
		if(school.equals("Alexander Wilson Middle School")){
			return 003;
		}
		if(school.equals("Academy at Palumbo")){
			return 004;
		}
	        
		if(school.equals("WorldCommunications Charter HS")){
			return 005;
		}
		if(school.equals("Gideon, K-8")){
			return 006;
		}
		if(school.equals("Comegys, K-8")){
			return 007;
		}
		if(school.equals("Bryant, K-8")){
			return 8;
		}
		if(school.equals("MYA")){
			return 9;
		}
		if(school.equals("Mastery Charter")){
			return 010;
		}
		if(school.equals("Friere Middle, 5-8")){
			return 011;
		}
		if(school.equals("Friere HS")){
			return 012;
		}
		if(school.equals("Multicultural Academy")){
			return 013;
		}
		
		if(school.equals("Huey, K-8")){
			return 014;
		}
		if(school.equals("Lea, K-8")){
			return 015;
		}
		if(school.equals("Vare, 5-8")){
			return 016;
		}
		if(school.equals("True Bright Science Academy HS")){
			return 017;
		}
		if(school.equals("Bartram HS")){
			return 18;
		}
		if(school.equals("Sayre HS")){
			return 19;
		}
		if(school.equals("Parkway West HS")){
			return 020;
		}
		if(school.equals("Mastery Shoe-Maker HS")){
			return 021;
		}
		if(school.equals("Parkway West HS")){
			return 022;
		}
		if(school.equals("School of the Future HS")){
			return 023;
		}
		if(school.equals("West Philly High HS")){
			return 024;
		}
		if(school.equals("Strawberry Mansion HS")){
			return 001;
		}
		return 99;
	}
		
    public void continueToWeather(View v){
    	Intent i = new Intent(this, WeatherActivity.class);
    	
    	//save school information for later use
    	fillVolunteers();
    	fillStaff();
    	updateSchool();
    	//only want to move on if a volunteer and a staff member have received input
    	if(vol1 != null && vol1.length() >= 1 && 
    			staff1 != null && staff1.length() >= 1
    			&& school != null && school.length() >= 1){
    		//save stand with dummy values for info not collected yet. this will be updated in cashboxandcost
    		DatabaseHandler dh = DatabaseHandler.getInstance(this);
    		FruitStand stand = new FruitStand(school, (month + "/" + day + "/" + year), -1, 
    				"",-1.0, -1.0, -1.0, -1.0);
    		dh.putFruitStand(stand);

			stand.addStaffMember(this, vol1, true);
			if (vol2 != null){
				stand.addStaffMember(this, vol2, true);
			}
			if (vol3 != null){
			stand.addStaffMember(this, vol3, true);
			}
			if (vol4 != null){
			stand.addStaffMember(this, vol4, true);
			}
    		
    		if((id >= 5) && (vol5 != null) && (!vol5.equals(""))){
    			stand.addStaffMember(this, vol5, true);
    		}
    		if(id > 6){
    			stand.addStaffMember(this, vol6, true);
    		}
    		if(id > 7){
    			stand.addStaffMember(this, vol7, true);
    		}
    		if(id >= 8){
    			stand.addStaffMember(this, vol8, true);
    		}
    		//add id here
			stand.addStaffMember(this, staff1, false);
    		if(staff2 != null && staff2 != ""){
				stand.addStaffMember(this, staff2, false);
			}
    		if(staff3 != null && staff3 != ""){
    			stand.addStaffMember(this, staff3, false);
    		}
    		if(staff4 != null && staff4 != ""){
    			stand.addStaffMember(this, staff4, false);
    		}

    		this.startActivity(i);
    		this.finish();

    	}else{
    			String toastText = "Please enter at least one staff member and one volunteer. ";
    			Toast.makeText(getApplicationContext(), toastText.toString(), Toast.LENGTH_LONG).show();
    	}
    }
}







