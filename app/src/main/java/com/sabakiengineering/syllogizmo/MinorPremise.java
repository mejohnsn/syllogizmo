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
 * Activity/Screen for handling input of Minor Premise.
 * Displays Spinners for quantifier and verb, EditTexts
 * for subj/pred, and Done button.
 * 
 * TODO: 1) add EditText to get actual text of subject/predicate DONE
 * 2) get values returned by spinner (almost done)
 * 3) assemble into CatStatement. DONE
 * 4) need separate spinner for min/maj to replace simple_spinner_dropdown_item
 * and simple_spinner_item? DONE.
 * 5) Can I use notifyDataSetChanged() to force Spinner/Adapters to
 * use my data? Or can I force UI to preserve that state and reread it
 * back in with onSaveInstanceState?
 */
public class MinorPremise extends Activity implements OnClickListener {
	private final String TAG = "MinorPremise";
	private ArrayAdapter<CharSequence> quant_adapter;  // moved from onCreate
	private ArrayAdapter<CharSequence> verb_adapter;
	private Spinner quantSpinner;        // to share with onClick
	private Spinner verbSpinner;            // which suggests I didn't need the 'min' prefix above
    private EditText subject;
    private EditText predicate;
    @Override
    protected void onSaveInstanceState(Bundle icicle) {
    	super.onSaveInstanceState(icicle);
//    	Log.d(TAG, "onSaveInstanceState called");
    }
	CatStatement Minor_Premise = new CatStatement(SylloGizmo.FinalMinorPremise);
	@Override // temporary location: more readable for now.
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.minor_done_button:
			// copy all the view data to model (FinalSyllogism etc)
			//  starting w/ quantifier spinner...
//			Log.d(TAG, (String) quant_adapter.getItem(quantSpinner.getSelectedItemPosition()));
			// it DOES get here when Done pressed...
			// and it DOES copy the currently sel. value from the Array -- to
			// the Log
			Minor_Premise.setQuantifier(quantSpinner.getSelectedItemPosition());
			quant_adapter.notifyDataSetChanged();
			// Now the same for the other (verb) spinner
			Minor_Premise.setVerb(verbSpinner.getSelectedItemPosition());

			// EditText objects no longer handled here: they have
			//  their own TextWatcher to update on every change
			// Finally, copy to global
			com.sabakiengineering.syllogizmo.SylloGizmo.FinalMinorPremise = Minor_Premise;
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
//		Log.d(TAG, "onResume");
	}
    @SuppressWarnings("unchecked")
	@Override
	    public void onCreate(Bundle savedInstanceState) {
//		  Log.d(TAG, "entering onCreate");
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.minor_premise);
	    	// Now set up spinners: quantifier and are/are not
	    	quantSpinner = (Spinner) findViewById(R.id.minor_spinner);
	    	verbSpinner = (Spinner) findViewById(R.id.minor_verb_spinner);
		    // ArrayAdapter binds string array to layout view elementwise
		    quant_adapter = ArrayAdapter.createFromResource(
		            this, R.array.quantifiers, android.R.layout.simple_spinner_item);
		    quant_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    quantSpinner.setAdapter(quant_adapter);

            // put EditText box in between the two spinners -- just like in real layout
		    EditText subject = (EditText) findViewById(R.id.minor_subject);
		    EditText predicate = (EditText) findViewById(R.id.minor_predicate);
            // And the final spinner
		    verb_adapter = ArrayAdapter.createFromResource(
		            this, R.array.verbs, android.R.layout.simple_spinner_item);
		    verb_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    verbSpinner.setAdapter(verb_adapter);
            // final widget on screen: the Done button
	    	View doneButton = findViewById(R.id.minor_done_button); 
	    	doneButton.setOnClickListener(this);
//	    	Log.d(TAG, "onCreate set up 2 spinners, 2 EditTexts & 1 btn");
	    	
	    	// Finally, get values fm Minor_Premise into Views
			quantSpinner.setSelection(Minor_Premise.getQuantifierIndex());
			verbSpinner.setSelection(Minor_Premise.getVerbIndex());

	    	// Now the EditText objects
	    	subject.setText(Minor_Premise.getSubject());
	    	predicate.setText(Minor_Premise.getPredicate());
            
			subject.addTextChangedListener(new TextWatcher() {
				@SuppressWarnings("unused") // Why IS this never used?
				public void onTextChanged(Editable s) {
					Minor_Premise.setSubject(s.toString());
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
//					Log.d(TAG, "got to beforeTextChanged");
				}

				@Override
				/**
				 * Get each change to subject EditText, copy to MinorPremise
				 */
				public void onTextChanged(CharSequence s, int start, int before,
						int count) {
					Minor_Premise.setSubject(s.toString());
//					Log.d(TAG, Minor_Premise.getSubject());
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
					Minor_Premise.setPredicate(s.toString());
//					Log.d(TAG, Minor_Premise.getPredicate());
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
//					Log.d(TAG, "got to beforeTextChanged");
				}

				@Override
				/**
				 * Get each change to subject EditText, copy to MinorPremise
				 */
				public void onTextChanged(CharSequence s, int start, int before,
						int count) {
					Minor_Premise.setPredicate(s.toString());
//					Log.d(TAG, Minor_Premise.getPredicate());
//					Log.d(TAG, "  in onTextChanged(CharSeq...)");
				}

				@Override
				public void afterTextChanged(Editable s) {
//					Log.d(TAG, "got to afterTextChanged");
				}
			});

    } // closes onCreate
	@Override
		public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// inflater used to read menu def fm XML, mk rl view.
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


	private void get_help() {
//		Log.d(TAG, "get_help");
		// imitating Unlocking Androidp65:
		StringBuilder validationText = new StringBuilder("").append(getResources().getString(R.string.help_menu_text));
		// this is printed below the line
		// while help_menu_text printed above.
		StringBuilder cText = new StringBuilder("").append(getResources().getString(R.string.continue_button_label));

		new AlertDialog.Builder(this).setTitle(
                 getResources().getString(R.string.help_menu_title))
			.setMessage( validationText.toString()).setPositiveButton(cText, // TODO: use resource
                    new android.content.DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int arg1) { // do nothing
						 }
                      }).show();
	}
	private void get_settings() {
//		Log.d(TAG, "get_settings");
		startActivity(new Intent(this, Settings.class));

		}
}
