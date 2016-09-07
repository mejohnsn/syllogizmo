package com.sabakiengineering.syllogizmo;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Settings extends PreferenceActivity {
	private static final String OPT_KEEPSIES="keepsies";
	private static final boolean OPT_KEEPSIES_DEF=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}
	/**
	 * 
	 * @param context
	 * @return true/false
	 * 
	 * For now I slavishly imitate sudoku
	 * TODO: figure out where to call this, how to implement turning
	 * off default Syllogism.
	 */
	public static boolean getKeepsies(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_KEEPSIES, OPT_KEEPSIES_DEF);
	}
}
