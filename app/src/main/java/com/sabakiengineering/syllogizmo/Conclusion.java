package com.sabakiengineering.syllogizmo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
 * Activity/Screen for handling input of Conclusion.
 * Displays Spinners for quantifier and verb, EditTexts
 * for subj/pred, and Done button.
 * 
 */
public class Conclusion extends Activity implements OnClickListener {
	private final String TAG = "Conclusion";
	private ArrayAdapter<CharSequence> quant_adapter;  // moved from onCreate
	private ArrayAdapter<CharSequence> verb_adapter;
	private Spinner quantSpinner;        // to share with onClick
	private Spinner verbSpinner;
    private EditText subject;
    private EditText predicate;
    @Override
    protected void onSaveInstanceState(Bundle icicle) {
    	super.onSaveInstanceState(icicle);
//    	Log.d(TAG, "onSaveInstanceState called");
    }
	CatStatement Conclusion = new CatStatement(SylloGizmo.FinalConclusion);
	@Override // temporary location: more readable for now.
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.conclusion_done_button:
			// copy all the view data to model (FinalSyllogism etc)
			//  starting w/ quantifier spinner...
//			Log.d(TAG, (String) quant_adapter.getItem(quantSpinner
			//		.getSelectedItemPosition()));
			// it DOES get here when Done pressed...
			// and it DOES copy the currently sel. value from the Array -- to
			// the Log
			Conclusion.setQuantifier(quantSpinner.getSelectedItemPosition());
			quant_adapter.notifyDataSetChanged();
			// Now the same for the other (verb) spinner
			Conclusion.setVerb(verbSpinner.getSelectedItemPosition());
			// EditText objects no longer handled here: they have
			//  their own TextWatcher to update on every change
			// Finally, copy to global
			com.sabakiengineering.syllogizmo.SylloGizmo.FinalConclusion= Conclusion;
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

	@Override
	    public void onCreate(Bundle savedInstanceState) {
//		  Log.d(TAG, "entering onCreate");
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.conclusion);
	    	// Now set up spinners: quantifier and are/are not
	    	quantSpinner = (Spinner) findViewById(R.id.conclusion_spinner);
	    	verbSpinner = (Spinner) findViewById(R.id.conclusion_verb_spinner);
		    // ArrayAdapter binds string array to layout view elementwise
		    quant_adapter = ArrayAdapter.createFromResource(
		            this, R.array.quantifiers, android.R.layout.simple_spinner_item);
		    quant_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    quantSpinner.setAdapter(quant_adapter);

            // put EditText box in between the two spinners -- just like in real layout
		    EditText subject = (EditText) findViewById(R.id.conclusion_subject);
		    EditText predicate = (EditText) findViewById(R.id.conclusion_predicate);
            // And the final <
		    verb_adapter = ArrayAdapter.createFromResource(
		            this, R.array.verbs, android.R.layout.simple_spinner_item);
		    verb_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    verbSpinner.setAdapter(verb_adapter);
            // final widget on screen: the Done button
	    	View doneButton = findViewById(R.id.conclusion_done_button); 
	    	doneButton.setOnClickListener(this);
//	    	Log.d(TAG, "onCreate set up 2 spinners, 2 EditTexts & 1 btn");
	    	
	    	// Finally, get values fm FinalConclusion into Views
			quantSpinner.setSelection(Conclusion.getQuantifierIndex());
			verbSpinner.setSelection(Conclusion.getVerbIndex());

	    	// Now the EditText objects
	    	subject.setText(Conclusion.getSubject());
	    	predicate.setText(Conclusion.getPredicate());
            
			subject.addTextChangedListener(new TextWatcher() {
				@SuppressWarnings("unused") // Why IS this never used?
				public void onTextChanged(Editable s) {
					Conclusion.setSubject(s.toString());
//					Log.d(TAG, Conclusion.getSubject());
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
//					Log.d(TAG, "got to beforeTextChanged");
				}

				@Override
				/**
				 * Get each change to subject EditText, copy to Conclusion
				 */
				public void onTextChanged(CharSequence s, int start, int before,
						int count) {
					Conclusion.setSubject(s.toString());
//					Log.d(TAG, Conclusion.getSubject());
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
					Conclusion.setPredicate(s.toString());
//					Log.d(TAG, Conclusion.getPredicate());
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
//					Log.d(TAG, "got to beforeTextChanged");
				}

				@Override
				/**
				 * Get each change to subject EditText, copy to Conclusion
				 */
				public void onTextChanged(CharSequence s, int start, int before,
						int count) {
					Conclusion.setPredicate(s.toString());
//					Log.d(TAG, Conclusion.getPredicate());
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

		private void get_settings() {
//			Log.d(TAG, "get_settings");
			// imitating Unlocking Androidp65 again:
			StringBuilder vText = new StringBuilder("").append(getResources()
					.getString(R.string.prefs_menu_text)); // this is printed below
														   // the line
			// while help_menu_text printed above.
			StringBuilder cText = new StringBuilder("").append(getResources().getString(R.string.continue_button_label));

			new AlertDialog.Builder(this).setTitle(
					getResources().getString(R.string.prefs_menu_title))
					.setMessage(vText.toString()).setPositiveButton(cText, // should use res	
							new android.content.DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int arg1) { // do nothing
								}
							}).show();

		}
		private void get_help() {
//  		Log.d(TAG, "get_help");
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
