package com.sabakiengineering.syllogizmo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

/**
 * 
 * @author mejohnsn
 * Activity/Screen for handling input of Major Premise.
 * Displays Spinners for quantifier and verb, EditTexts
 * for subj/pred, and Done button.
 * 
 * TODO: 1) add EditText to get actual text of subject/predicate DONE
 * 2) get values returned by spinner DONE
 * 3) assemble into CatStatement. DONE
 * 4) need separate spinner for min/maj to replace simple_spinner_dropdown_item
 * and simple_spinner_item? DONE.
 * 5) Can I use notifyDataSetChanged() to force Spinner/Adapters to
 * use my data? Or can I force UI to preserve that state and reread it
 * back in with onSaveInstanceState? A: I have abandoned that approach
 * 6) I have TextWatcher copying into Major_Premise each time. Do I REALLY
 * want this? This creates a problem for Back key, since original data
 * not kept. A: problem solved by editing local copy, commit to global on Done
 * 6a) BTW: since I overwrite initial values in XML files (for major premise
 * subject etc) what DO I want to use as initial values there?
 */
public class MajorPremise extends Activity implements OnClickListener {
	private final String TAG = "MajorPremise";
	private ArrayAdapter<CharSequence> quant_adapter;
	private ArrayAdapter<CharSequence> verb_adapter;
	private Spinner quantSpinner;
	private Spinner verbSpinner;
    @SuppressWarnings("unused")          // it IS used.
	private EditText subject;
    @SuppressWarnings("unused")
	private EditText predicate;          // it IS used.
    @Override
    protected void onSaveInstanceState(Bundle icicle) {
    	super.onSaveInstanceState(icicle);
//    	Log.d(TAG, "onSaveInstanceState called");
    }

    CatStatement Major_Premise = new CatStatement(SylloGizmo.FinalMajorPremise);

    @Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.major_done_button:
			// copy all the Spinner View data to model (FinalMajorPremise)
			//  starting w/ quantifier spinner...
			//  but we now use Index, to keep correspondence btw string const & i18n
			Major_Premise.setQuantifier(quantSpinner.getSelectedItemPosition());
			quant_adapter.notifyDataSetChanged();
			// Now the same for the other (verb) spinner
			Major_Premise.setVerb(verbSpinner.getSelectedItemPosition());
			// EditText objects no longer handled here: they have
			// their own TextWatcher to update on every change
			// Finally, copy to global
			com.sabakiengineering.syllogizmo.SylloGizmo.FinalMajorPremise = Major_Premise;
			finish();
			break;
		default:
//			Log.d(TAG, "How did we get THIS 'button'?");
		}
	}
	@Override
	public void onPause() {
		super.onPause();
//		Log.d(TAG, "onPause");
	}
	@Override
	public void onResume() {
		super.onResume();
//      Log.d(TAG, "onResume");
	}
    @SuppressWarnings("unchecked")
	@Override
	    public void onCreate(Bundle savedInstanceState) {
		  Log.d(TAG, "entering onCreate");
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.major_premise);
	    	// Now set up spinners: quantifier and are/are not
	    	quantSpinner = (Spinner) findViewById(R.id.major_spinner);
	    	verbSpinner = (Spinner) findViewById(R.id.major_verb_spinner);
		    // ArrayAdapter binds string array to layout view elementwise
		    quant_adapter = ArrayAdapter.createFromResource(
		            this, R.array.quantifiers, android.R.layout.simple_spinner_item);
		    quant_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    quantSpinner.setAdapter(quant_adapter);

            // put EditText box in between the two spinners -- just like in real layout
		    EditText subject = (EditText) findViewById(R.id.major_subject);
		    EditText predicate = (EditText) findViewById(R.id.major_predicate);
            // And the final spinner
		    verb_adapter = ArrayAdapter.createFromResource(
		            this, R.array.verbs, android.R.layout.simple_spinner_item);
		    verb_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    verbSpinner.setAdapter(verb_adapter);
            // final widget on screen: the Done button
	    	View doneButton = findViewById(R.id.major_done_button); 
	    	doneButton.setOnClickListener(this);
//	    	Log.d(TAG, "onCreate set up 2 spinners, 2 EditTexts & 1 btn");
	    	
	    	// Finally, get values from Major_Premise into Views:
			// For Spinners, more precisely, translate Major_Premise values into
			// localized string placed in Adapter corresponding to View

			quantSpinner.setSelection(Major_Premise.getQuantifierIndex());
			verbSpinner.setSelection(Major_Premise.getVerbIndex());

	    	// Now the EditText objects
	    	subject.setText(Major_Premise.getSubject());
	    	predicate.setText(Major_Premise.getPredicate());
            
			subject.addTextChangedListener(new TextWatcher() {
				@SuppressWarnings("unused") // Why IS this never used?
                @Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
//					Log.d(TAG, "got to beforeTextChanged");
				}

				@Override
				/**
				 * Get each change to subject EditText, copy to Major_Premise
				 */
				public void onTextChanged(CharSequence s, int start, int before,
						int count) {
					Major_Premise.setSubject(s.toString());
//					Log.d(TAG, Major_Premise.getSubject());
//					Log.d(TAG, "  in onTextChanged(CharSeq...)");
				}

				@Override
				public void afterTextChanged(Editable s) {
//					Log.d(TAG, "got to afterTextChanged");
				}
			});
			predicate.addTextChangedListener(new TextWatcher() {
				@SuppressWarnings("unused") // again: why is this never used?
				public void onTextChanged(Editable s) {
					Major_Premise.setPredicate(s.toString());
//					Log.d(TAG, Major_Premise.getPredicate());
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
//					Log.d(TAG, "got to beforeTextChanged");
				}

				@Override
				/**
				 * Get each change to subject EditText, copy to MajorPremise
				 */
				public void onTextChanged(CharSequence s, int start, int before,
						int count) {
					Major_Premise.setPredicate(s.toString());
//					Log.d(TAG, Major_Premise.getPredicate());
//					Log.d(TAG, "  in onTextChanged(CharSeq...)");
				}

				@Override
				public void afterTextChanged(Editable s) {
//					Log.d(TAG, "got to afterTextChanged");
				}
			});

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
		@Override
		public boolean onMenuItemSelected(int featureId, MenuItem item) {
		    switch (item.getItemId()) {
		       case R.id.options_menu:
		    	   get_help();
		    	   break;
		       case R.id.more_options_menu:
		    	   get_settings();
		    	   break;
		       default:
		    	   break;
		    }
		    return super.onMenuItemSelected(featureId, item);
		}

		private void get_settings() {
//			Log.d(TAG, "get_settings");
			startActivity(new Intent(this, Settings.class));
		}
		
		private void get_help() {
//			Log.d(TAG, "get_help");
			// imitating Unlocking Androidp65:
			StringBuilder validationText = new StringBuilder("").append(getResources().getString(R.string.help_menu_text));
			// this is printed below the line
			// while help_menu_text printed above.
			StringBuilder cText = new StringBuilder("").append(getResources().getString(R.string.continue_button_label));

			new AlertDialog.Builder(this).setTitle(
					getResources().getString(R.string.help_menu_title)).setMessage(
					validationText.toString()).setPositiveButton(cText,
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int arg1) { // do nothing
						}
					}).show();
		}
}


