package com.sabakiengineering.syllogizmo;

/**
 
 3) Shading for empty/full parts of circle/lunes can be saved in
 preferences, along with colors for circles/bars/crosses. Even
 simpler candidate: flag controlling inclusion of default text.
 Another: output of actual strengthening premise. Another: medieval
 mnemonic for syll. val w/ strengthening premise. Another: formatting
 of Results output. Another: color of Results button.
 4) Program should output mood & figure as another option. But
 this is currently not an option, it just does it.
 6) support bar as well as cross
 7) Quine says ellipses instead of circles can b used for four terms:
 computers are good at careful drawing, so make this another option
 for later (paid?) version. Or see Wikipedia on Venn Diagrams w/
 logic refs.
 8) toasts on diagram to show full text equivalent for F, G, H,
 conclusion.
 9) singular inference needs no special code for diagram, but might for
 outputting mood/figure too.
 10) Should I use a wizard for input of subject/term etc. instead of
 separate screens for each premise? That way I could allow separate
 entry in 'canonical' form, i.e. "No F are G" etc., which would be
 much simpler.
 11) I hope one of the common applications will be verifying fallacy of
 undistributed middle, so I have to support marking out where the
 'distribution' occurs: what else should I do to support this?
 12) Ordering: rather than insist on Quine's ordering of F,G,H for
 minor, middle, major, ideally, program should figure out which is
 which and issue error message if no such assignment possible. But
 this could be an advanced feature. For now I will build the correct
 ordering into the prompts.
 13) Widgets I expect to use and where to find sample code: Spinner (Hello Views,
 API Demos Views/Spinner) for choosing quantifier, are/are not, EditText, TextView.
 14) At this point, I want to do some preliminary programs such as SyllCheck to take
 syllogism (in which representation?) and determine form, figure and validity;
 DrawOneVennDiag to draw some specific Venn Diag;
 15) #14's SyllCheck could use code from MyJava/LookupTable.java, but I could easily
 drop fullValueArray & all refs 2 it.  Dev Guide on 2D G sugg. ShapeDrawable for
 DrawOneVennDiag. Circles done w/ OvalShape. But how do I control the fill?
16) internationalization: I have been thinking of adding an array for
translating from current language to English for both Spinners: but
the Japanese example shows that for full generality, I need to
translate the PAIR (quantifier, verb). But that is my current plan:
translate from (local-quant,local-spin) -> (eng-quant,eng-spin) and
use the latter to pass to constructors in Syllogism.java. This could
be even more complicated if I allow different levels of politeness and
animate/inanimate equivalents for the verb.
17) since I have so much code duplicated between major and minor premise, and conclusion,
what kind of object does this suggest I really should have used?
A Premise object? But this would leave out Conclusion. Should I expand
on the CatStatement object? In this program at least, there is no relevant
difference between a premise and a Categorical Statement, so this suggests
building on CatStatement. But the other reason I did separate layouts for all three
is to make XML files for each view, just like Unlocking Android & EB's book
did; I dont' see a way to generalize this to CatStatement.xml, unless I
can build it partially w/ inflation and then finish off setting android:text
& the like in Java. If this is the right approach, it might have been worth
it, since it is getting to be a lot of work to continue as I have been doing.
 
 Status: Layout of Spinner was too strange for words, but is much better now
 that I added 'android:orientation="vertical"'!
 
 Also, I need to actually retrieve the values ret. by Spinner and put them somewhere.
 But where? How will I store between button-activities? For now I plan to use
 package access so all Activities share space. But see p66 of Unlocking for
 other options.
 
 More status: I just added event handling for onCreateOptionMenu: it does
 not work yet. I also added updating of both Model & View to MajorPremise.
 It is also incomplete, since untested. But it is largely working, it may
 even already be working for both Spinners and both EditTexts.
 
 I have added prefs.xml and Settings.java for reading in Preferences, of
 which there is exactly one right now: but slavishly following Burnette,
 I have added getKeepsies(...), but not yet added the call to it. I now
 also added the call to startIntent to create the Preferences Activity
 from the Settings menu, so it now displays.
 */
// TODO: in addition to above items, 'andbook' suggests I should use
// onActivityResult() and finish() to get results when sub-activities
// finished. This would let me intercept Back key? But does this name
// even still exist? There is an onBackPressed() which sounds like an even
// better match. But yes, there still is onActivityResult().

// TODO: move CatStatement initialization to when entire app is
// created instead of (as now) when MajorPremise
// etc. created. Actually, though it doesn't sound like the Android
// way, I am pretty close to that with FinalMajorPremise etc. These
// just happen to duplicate initial values in Views -- which is the
// real problem; the link must be made more explicit.

// TODO: should I disallow "all men are not mortal"? Earlier, I
// thought it was only "No men are not mortal" I should disallow. But
// this would leave only "some men are not mortal" a valid use of "are
// not". But as it is, allowing this invalidates my isValid() test. I
// would implement this by graying out 'are not' when 'no' chosen &vv.
//
// TODO: Data persistence across invocations is NOT a feature! I want it to
// go back to defaults -- or do I? What really IS the Android Way here? A:
// yes, it is a feature. It is only if the Activities are all killed that
// the next invocation is supposed to go through onCreate.
//
// TODO: need a list of steps I can follow for "release procedure",
// since I am unsure what I need to do. Right now, I can remember 1)
// remove/replace 'android:debuggable="true"' in Manifest 2)
// remove/comment out any refs to Log. 3) update versionCode
// versionName in Manifest (code still showed 1.2 though changes made
// since last upload to SlideMe). Unfortunately, currently this requires changing both versionName and about_text.
// 4) write "release notes", update in Git 5) only now sign the APK and do the upload.
//
// compare above to ~/androidReleaseProcedure.txt

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class SylloGizmo extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	private final String TAG = "SylloGizmo";
	private final String keepsiesString = "keep_default_syllogism";
	protected static CatStatement FinalMajorPremise;
	protected static CatStatement FinalMinorPremise;
	protected static CatStatement FinalConclusion;
	protected static Syllogism FinalSyllogism;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        // read preferences
		// I wanted these in constructor, but here to force reread
		SharedPreferences sp;
		sp = PreferenceManager.getDefaultSharedPreferences(this); // not sure why AinA uses getShared instead of getDefault...
		boolean keepsies;
		keepsies = sp.getBoolean(keepsiesString, false);
		if (keepsies) {
			FinalMajorPremise = new CatStatement("All", "men", "mortal");
			FinalMinorPremise = new CatStatement("All", "Greeks", "men");
			FinalConclusion   = new CatStatement("All", "Greeks", "mortal");
		} else {
			FinalMajorPremise = new CatStatement("", "", "");
			FinalMinorPremise = new CatStatement("", "", "");
			FinalConclusion   = new CatStatement("", "", "");
			FinalSyllogism    = new Syllogism(FinalMinorPremise, FinalMajorPremise, FinalConclusion);
		}		

		// Now set up button(s): instantiate and then... But why doesn't sample
		// code treat them as buttons?
    	View aboutButton = findViewById(R.id.about_button);
    	View majorPremiseButton = findViewById(R.id.major_premise_button);
    	View minorPremiseButton = findViewById(R.id.minor_premise_button);
    	View conclusionButton    = findViewById(R.id.conclusion_button);
    	View showResultsButton  = findViewById(R.id.show_results_button);
    	// set their Listeners, i.e. register SylloGizmo object as Listener for each.
    	aboutButton.setOnClickListener(this);
    	majorPremiseButton.setOnClickListener(this);
    	minorPremiseButton.setOnClickListener(this);
    	conclusionButton.setOnClickListener(this);
    	showResultsButton.setOnClickListener(this);
    	Log.d(TAG, "onCreate set up 5 Buttons");
    }
	@Override
	public void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart called" ); // interesting. Useful?
	}
	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart called" ); // interesting. Useful?
	}
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
	}
	/**
	 * In theory, the last place to persist data. But since Activity
	 * SylloGizmo still on stack, my protected vars SHOULD be safe?
	 */
	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
	}
@Override
public void onResume() {
	super.onResume();
	Log.d(TAG, "onResume");
}
	
	/**
	 * Shows two menu items, Help and Preferences
	 * as described in res/menu/menu.xml
	 */
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

	@Override
	public void onClick(View v) {
		// assume click event is one of our buttons -- of course, as long as
		// platform really does treat onMenuItemSelected as separate event
		switch (v.getId()) { //  clear queue and see which button pressed
		// and start that activity for new screen
		case R.id.about_button:
			Intent it = new Intent(this, About.class);
			startActivity(it);
//			Log.d(TAG, "case R.id.about_button");
			break;
		case R.id.minor_premise_button:
			Intent min_it = new Intent(this, MinorPremise.class);
			startActivity(min_it);
			break;
		case R.id.major_premise_button:
			Intent maj_it = new Intent(this, MajorPremise.class);
			startActivity(maj_it);
			break;
		case R.id.conclusion_button:
			Intent concl_it = new Intent(this, Conclusion.class);
			startActivity(concl_it);
			break;
		case R.id.show_results_button:
			Intent results_it = new Intent(this, ShowResults.class);
			startActivity(results_it);
			break;
		default:
//			Log.d(TAG, "onClick got bad button");
		}
	}

	private void get_help() {
//		Log.d(TAG, "get_help");
		// imitating Unlocking Androidp65:
		StringBuilder validationText = new StringBuilder("")
				.append(getResources().getString(R.string.help_menu_text)); // this is printed below the line
                // while help_menu_text printed above.
		StringBuilder cText = new StringBuilder("").append(getResources().getString(R.string.continue_button_label));

		new AlertDialog.Builder(this).setTitle(
 			    getResources().getString(R.string.help_menu_title)).setMessage(validationText.toString()).setPositiveButton(cText,
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