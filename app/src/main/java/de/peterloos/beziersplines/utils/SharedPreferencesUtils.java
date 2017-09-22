package de.peterloos.beziersplines.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Locale;

import de.peterloos.beziersplines.R;
import de.peterloos.beziersplines.BezierGlobals;

/**
 * Project: Bézier Splines Simulation
 * Copyright (c) 2017 by PeLo on 23.01.2017. All rights reserved.
 * Contact info: peterloos@gmx.de
 */

public class SharedPreferencesUtils {

    /*
     * language settings
     */
    public static boolean existsLanguagePreference(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.shared_pref_language);
        return sharedPref.contains(key);
    }

    public static String getPersistedLanguage(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String key = context.getString(R.string.shared_pref_language);
        String language = sharedPref.getString(key, BezierGlobals.DefaultLanguage);
        String msg = String.format("reading language ==> %s", language);
        Log.v("PeLo", msg);
        return language;
    }

    public static void persistLanguage(Context context, String language) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String msg = String.format("writing language ==> %s", language);
        Log.v("PeLo", msg);

        SharedPreferences.Editor editor = sharedPref.edit();
        String key = context.getString(R.string.shared_pref_language);
        editor.putString(key, language);
        editor.apply();
    }

    /*
     * gridlines settings
     */
    public static int getPersistedGridlinesFactor(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.shared_pref_gridlines);
        int gridlinesFactor = sharedPref.getInt(key, BezierGlobals.DefaultGridlineFactor);
        String msg = String.format(Locale.getDefault(), "reading gridlines factor ==> %d", gridlinesFactor);
        Log.v("PeLo", msg);

        return gridlinesFactor;
    }

    public static void persistGridlinesFactor(Context context, int gridlinesFactor) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String msg = String.format(Locale.getDefault(), "writing gridlines factor ==> %d", gridlinesFactor);
        Log.v("PeLo", msg);

        SharedPreferences.Editor editor = sharedPref.edit();
        String key = context.getString(R.string.shared_pref_gridlines);
        editor.putInt(key, gridlinesFactor);
        editor.apply();
    }

    /*
     * strokewidth settings
     */
    public static int getPersistedStrokewidthFactor(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.shared_pref_strokewidthfactor);
        int strokewidthFactor = sharedPref.getInt(key, BezierGlobals.DefaultStrokewidthFactor);
        String msg = String.format(Locale.getDefault(), "reading strokewidth factor ==> %d", strokewidthFactor);
        Log.v("PeLo", msg);

        return strokewidthFactor;
    }

    public static void persistStrokewidthFactor(Context context, int strokewidthFactor) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String msg = String.format(Locale.getDefault(), "writing strokewidth factor ==> %d", strokewidthFactor);
        Log.v("PeLo", msg);

        SharedPreferences.Editor editor = sharedPref.edit();
        String key = context.getString(R.string.shared_pref_strokewidthfactor);
        editor.putInt(key, strokewidthFactor);
        editor.apply();
    }
}
