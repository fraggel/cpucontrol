package es.fraggel.cpucontrol.activity;

import es.fraggel.cpucontrol.R;
import es.fraggel.cpucontrol.extra.Constants;
import es.fraggel.cpucontrol.extra.PatternReplacerInputFilter;
import es.fraggel.cpucontrol.extra.Theme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.util.Log;
import android.widget.Toast;

public class PreferencesActivity extends PreferenceActivity implements OnPreferenceChangeListener {

	private EditTextPreference prefPattern;
	private CheckBoxPreference prefSafetyValve;
	private CheckBoxPreference prefChangePermissions;
	private ListPreference prefTheme;
	private SharedPreferences prefs;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Theme.applyTo(this);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		prefPattern = (EditTextPreference) findPreference(Constants.PREF_VIBRATE_PATTERN);
		prefPattern.setOnPreferenceChangeListener(this);
		prefPattern.setSummary(prefs.getString(Constants.PREF_VIBRATE_PATTERN, ""));
		prefPattern.getEditText().setFilters(new InputFilter[] {
			new PatternReplacerInputFilter("[^0-9,]")
		});

		prefChangePermissions = (CheckBoxPreference) findPreference(Constants.PREF_CHANGE_PERMISSIONS);
		prefChangePermissions.setOnPreferenceChangeListener(this);

		prefTheme = (ListPreference) findPreference(Constants.PREF_THEME);
		prefTheme.setSummary(prefTheme.getEntry());
		prefTheme.setOnPreferenceChangeListener(this);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			CharSequence _e[] = prefTheme.getEntries(), entries[] = new String[_e.length - 1];
			CharSequence _ev[] = prefTheme.getEntryValues(), entryValues[] = new String[_ev.length - 1];
			int j = 0;
			for (int i = 0; i < _e.length; i++) {
				if (!_ev[i].equals(Theme.THEME_LIGHT_DARK.toString())) {
					entries[j] = _e[i];
					entryValues[j] = _ev[i];
					j++;
				}
			}
			prefTheme.setEntries(entries);
			prefTheme.setEntryValues(entryValues);
		}
	}

	@Override
	public boolean onPreferenceChange(final Preference preference, final Object newValue) {
		if (preference == prefPattern) {
			preference.setSummary((String) newValue);
			return true;
		} else if (preference == prefTheme) {
			if (prefTheme.getValue() == null || !prefTheme.getValue().equals(newValue.toString())) {
				Toast.makeText(this, R.string.prefs_theme_restart_needed, Toast.LENGTH_LONG).show();
			}
			return true;
		} else if (preference == prefSafetyValve || preference == prefChangePermissions) {
			Log.d(Constants.APP_TAG, preference.getKey() + " -> " + newValue.toString());
			if (Boolean.valueOf(newValue.toString())) {
				/*
				 * Prompt an alert dialog in style of CyanogenMod's "Dragons ahead!".
				 * Return false and handle dialog.
				 */
				int title = -1;
				int message = -1;
				if (preference == prefSafetyValve) {
					title = R.string.prefs_disable_safety_valve;
					message = R.string.prefs_disable_safety_valve_dialog_message;
				} else if (preference == prefChangePermissions) {
					title = R.string.prefs_change_permissions;
					message = R.string.prefs_change_permissions_dialog_message;
				}
				AlertDialog.Builder bldr = new AlertDialog.Builder(PreferencesActivity.this);
				bldr.setIcon(android.R.drawable.ic_dialog_alert);
				bldr.setTitle(title);
				bldr.setMessage(message);
				bldr.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						prefs.edit().putBoolean(preference.getKey(), true).commit();
						((CheckBoxPreference) preference).setChecked(true);
					}
				});
				bldr.setNegativeButton(android.R.string.cancel, null);
				bldr.create().show();
				return false;
			} else {
				/* Setting to false: ok */
				return true;
			}
		}
		return false;
	}
}
