/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2017 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.uiespresso.ui.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.catrobat.catroid.R;
import org.catrobat.catroid.ui.AccessibilityPreferencesActivity;
import org.catrobat.catroid.ui.AccessibilityProfilesActivity;
import org.catrobat.catroid.ui.BaseSettingsActivity;
import org.catrobat.catroid.uiespresso.ui.activity.utils.AccessibilityActivitiesUtils;
import org.catrobat.catroid.uiespresso.util.rules.IntentsActivityInstrumentationRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_ADDITIONAL_ICONS;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_DRAGNDROP_DELAY;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_FONTFACE;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_HIGH_CONTRAST;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_HIGH_CONTRAST_ICONS;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_LARGE_ELEMENT_SPACING;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_LARGE_ICONS;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_LARGE_TEXT;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_MYPROFILE_ADDITIONAL_ICONS;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_MYPROFILE_DRAGNDROP_DELAY;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_MYPROFILE_FONTFACE;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_MYPROFILE_HIGH_CONTRAST;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_MYPROFILE_HIGH_CONTRAST_ICONS;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_MYPROFILE_LARGE_ELEMENT_SPACING;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_MYPROFILE_LARGE_ICONS;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_MYPROFILE_LARGE_TEXT;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_MYPROFILE_STARTER_BRICKS;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_PROFILE_ACTIVE;
import static org.catrobat.catroid.ui.BaseSettingsActivity.ACCESS_STARTER_BRICKS;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
public class AccessibilityProfilesActivityTest {
	private static final String TAG = AccessibilityPreferencesActivity.class.getSimpleName();

	Resources resources;

	private List<String> settings = new ArrayList<>(Arrays.asList(ACCESS_LARGE_TEXT, ACCESS_HIGH_CONTRAST,
			ACCESS_ADDITIONAL_ICONS, ACCESS_LARGE_ICONS, ACCESS_HIGH_CONTRAST_ICONS, ACCESS_LARGE_ELEMENT_SPACING,
			ACCESS_STARTER_BRICKS, ACCESS_DRAGNDROP_DELAY));
	private List<String> myProfileSettings = new ArrayList<>(Arrays.asList(ACCESS_MYPROFILE_LARGE_TEXT, ACCESS_MYPROFILE_HIGH_CONTRAST,
			ACCESS_MYPROFILE_ADDITIONAL_ICONS, ACCESS_MYPROFILE_LARGE_ICONS, ACCESS_MYPROFILE_HIGH_CONTRAST_ICONS, ACCESS_MYPROFILE_LARGE_ELEMENT_SPACING,
			ACCESS_MYPROFILE_STARTER_BRICKS, ACCESS_MYPROFILE_DRAGNDROP_DELAY));
	private Map<String, Boolean> initialSettings = new HashMap<>();
	private String initialActiveFontface;
	private String initialFontfaceSetting;
	private String initialMyProfileFontfaceSetting;

	@Rule
	public IntentsActivityInstrumentationRule<AccessibilityProfilesActivity> baseActivityTestRule = new
			IntentsActivityInstrumentationRule<>(AccessibilityProfilesActivity.class, true, false);

	@Before
	public void setUp() throws Exception {
		resources = InstrumentationRegistry.getTargetContext().getResources();
		Context context = InstrumentationRegistry.getTargetContext();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

		for (String setting : settings) {
			initialSettings.put(setting, sharedPreferences.getBoolean(setting, false));
		}
		for (String setting : myProfileSettings) {
			initialSettings.put(setting, sharedPreferences.getBoolean(setting, false));
		}
		initialActiveFontface = BaseSettingsActivity.getActiveAccessibilityProfile(context);
		initialFontfaceSetting = BaseSettingsActivity.getAccessibilityFontFace(context);
		initialMyProfileFontfaceSetting = BaseSettingsActivity.getAccessibilityMyProfileFontFace(context);

		AccessibilityActivitiesUtils.resetSettings(settings, myProfileSettings);
		baseActivityTestRule.launchActivity(null);
	}

	@After
	public void tearDown() {
		SharedPreferences.Editor sharedPreferencesEditor = PreferenceManager
				.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext()).edit();
		for (String setting : initialSettings.keySet()) {
			sharedPreferencesEditor.putBoolean(setting, initialSettings.get(setting));
		}
		sharedPreferencesEditor.putString(ACCESS_PROFILE_ACTIVE, initialActiveFontface);
		sharedPreferencesEditor.putString(ACCESS_FONTFACE, initialFontfaceSetting);
		sharedPreferencesEditor.putString(ACCESS_MYPROFILE_FONTFACE, initialMyProfileFontfaceSetting);
		sharedPreferencesEditor.commit();
		initialSettings.clear();
	}

	@Test
	public void switchProfileTest() {
		Intent intentResult = new Intent();
		Instrumentation.ActivityResult activityResult = new Instrumentation.ActivityResult(Activity.RESULT_OK,intentResult);
		intending(anyIntent()).respondWith(activityResult);

		onView(withText(R.string.preference_access_title_profile_1)).perform(click());

		intended(allOf(hasExtra(equalTo(AccessibilityProfilesActivity.PROFILE_ID), equalTo(R.id.access_profile1))));

		System.out.println("BATMAN: " + intentResult.getDataString()); //.getIntExtra(AccessibilityProfilesActivity
		// .PROFILE_ID, -1));

		/*

		onView(withId(R.id.access_active_profile_title)).check(matches(TextViewMatchers.hasValueEqualTo(
				R.string.preference_access_title_profile_1)));
		onView(withId(R.id.access_active_profile_summary)).check(matches(TextViewMatchers.hasValueEqualTo(
				R.string.preference_access_summary_profile_1)));

		onView(withId(R.id.scratch_project_apply_button)).perform(click());
		onView(withText(R.string.ok)).perform(click());

		compareValuesToXMLProfile(ACCESS_PROFILE_1);

		openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
		onView(withText(R.string.settings)).perform(click());
		onData(PreferenceMatchers.withTitle(R.string.preference_title_access))
				.perform(click());

		onView(withId(R.id.access_active_profile_title)).check(matches(TextViewMatchers.hasValueEqualTo(
				R.string.preference_access_title_profile_1)));
		onView(withId(R.id.access_active_profile_summary)).check(matches(TextViewMatchers.hasValueEqualTo(
				R.string.preference_access_summary_profile_1)));

		onView(withId(R.id.access_switch_to_predefined_profiles_button)).perform(click());
		onView(withText(R.string.preference_access_title_profile_standard)).perform(click());
		onView(withId(R.id.scratch_project_apply_button)).perform(click());
		onView(withText(R.string.ok)).perform(click());
		*/
	}
}
