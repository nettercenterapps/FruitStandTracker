package netter.uni.cis350.project;

import netter.uni.cis350.project.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;


public class fragmStandSizeSelect extends DialogFragment {
	
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.fruit_cost_title);
        builder.setItems(R.array.fruitStandSize, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	Bundle bundle = getArguments();
        		
            	String directionFlag = "";
            	switch(which) {
            	case 0:
            		directionFlag = getResources().getString(R.string.eight_dollars);
            		break;
            	case 1:
            		directionFlag = getResources().getString(R.string.eleven_dollars);
            		break;
            	case 2: 
            		directionFlag = getResources().getString(R.string.fourteen_dollars);
            		break;
            	case 3:
            		directionFlag = getResources().getString(R.string.twenty_dollars);
            		break;
            	default:
            		break;
            	}
            	
            	  ((CashBoxAndCostActivity) getActivity()).closeAndSetFruitVal(directionFlag);

            }

        });
        return builder.create();
    }
}
			 

