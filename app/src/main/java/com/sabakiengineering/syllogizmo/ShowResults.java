package com.sabakiengineering.syllogizmo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 
 * @author mejohnsn
 *Come to think of it, one TextView is not enough for displaying
 *results: I need to display 1) mood 2) form 3) validity message
 *
 * NB: this does NOT update the Model! Someone else must do that.
 * It only displays it, updating View to reflect it (i.e. all Views
 * in ShowResults)
 */
public class ShowResults extends Activity {
//	private final String TAG = "ShowResults";
	// Eventually, this will extract from other Activities
	@Override
	public void onCreate(Bundle bdl) {
//	Log.d(TAG, "entered onCreate");
        super.onCreate(bdl);
        setContentView(R.layout.show_results);
    	// Now set up TextViews
		TextView resultsViewMood =     (TextView) findViewById(R.id.results_view_mood);
		TextView resultsViewFigure =   (TextView) findViewById(R.id.results_view_figure);
		TextView resultsViewValidity = (TextView) findViewById(R.id.results_view_validity);
		TextView resultsViewMnemonic = (TextView) findViewById(R.id.results_view_mnemonic);
		// Get latest Model data from FinalMajorPremise etc., give convenience name to references
    	//  this approach treats 'FinalSyllogism' as no longer part of the Model.
    	CatStatement majP = com.sabakiengineering.syllogizmo.SylloGizmo.FinalMajorPremise;
    	CatStatement minP = com.sabakiengineering.syllogizmo.SylloGizmo.FinalMinorPremise;
    	CatStatement concl= com.sabakiengineering.syllogizmo.SylloGizmo.FinalConclusion;
   	 	Syllogism results = new Syllogism(minP, majP, concl); // which also computes figure etc. 
		// Current display order is mood, figure validity
		// Mood
		resultsViewMood.setText(getString(R.string.mood_is) + " " + results.getMood());
		// Figure
		switch(results.getFigure()) {
		case 1:
			resultsViewFigure.setText(getString(R.string.first_figure));
			break;
		case 2:
			resultsViewFigure.setText(getString(R.string.second_figure));
			break;
		case 3:
			resultsViewFigure.setText(getString(R.string.third_figure));
			break;
		case 4:
			resultsViewFigure.setText(getString(R.string.fourth_figure));
			break;
		}
		// Validity -- how was this supposed to work? I had to rewrite it once I started to use resources.
		String validity = new String("Let's allocate lot's of space to be orverwritten");
		if (results.isValid())
			validity = getString(R.string.validity_true);
		else {
			if (results.isValidWithAddedPremise())
				validity = getString(R.string.validity_with_add);
			else
				validity = getString(R.string.validity_false);
		}
		resultsViewValidity.setText(validity);     // what does setText do if given null??
		resultsViewMnemonic.setText(results.getMedMnemonic());
	}     // end of onCreate
}
