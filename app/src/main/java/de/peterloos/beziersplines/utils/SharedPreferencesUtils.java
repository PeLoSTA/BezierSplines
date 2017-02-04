package de.peterloos.beziersplines.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Locale;

import de.peterloos.beziersplines.R;
import de.peterloos.beziersplines.activities.BezierGlobals;
import de.peterloos.beziersplines.views.BezierView;

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
     * language settings
     */
    public static int getPersistedScaleFactor(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String key = context.getString(R.string.shared_pref_scalefactor);
        int scaleFactor = sharedPref.getInt(key, BezierGlobals.DefaultScaleFactor);

        String msg = String.format(Locale.getDefault(), "reading scaleFactor ==> %d", scaleFactor);
        Log.v("PeLo", msg);

        return scaleFactor;
    }

    public static void persistScaleFactor(Context context, int scaleFactor) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String msg = String.format(Locale.getDefault(), "writing scaleFactor ==> %d", scaleFactor);
        Log.v("PeLo", msg);

        SharedPreferences.Editor editor = sharedPref.edit();
        String key = context.getString(R.string.shared_pref_scalefactor);
        editor.putInt(key, scaleFactor);
        editor.apply();
    }

    public static void getPersistedStrokeWidths(Context context, BezierView view1, BezierView view2) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String key = context.getString(R.string.shared_pref_strokewidth_control_points);
        float strokewidthControlPoints = sharedPref.getFloat(key, BezierGlobals.StrokeWidthControlPointsDp);
        key = context.getString(R.string.shared_pref_strokewidth_curve_line);
        float strokewidthCurveLine = sharedPref.getFloat(key, BezierGlobals.StrokeWidthCurveLineDp);
        key = context.getString(R.string.shared_pref_strokewidth_construction_lines);
        float strokewidthConstructionLines = sharedPref.getFloat(key, BezierGlobals.StrokeWidthConstructionLinesDp);

        String msg = String.format(Locale.getDefault(), "read SharedPreferences: ==> %f, %f, %f", strokewidthControlPoints, strokewidthCurveLine, strokewidthConstructionLines);
        Log.v("PeLo", msg);

        // setup Bezier view
        Resources res = context.getResources();
        view1.setStrokeWidthControlPoints(res, strokewidthControlPoints);
        view1.setStrokeWidthCurveLines(res, strokewidthCurveLine);
        view1.setStrokeWidthConstructionLines(res, strokewidthConstructionLines);

        if (view2 != null) {
            view2.setStrokeWidthControlPoints(res, strokewidthControlPoints);
            view2.setStrokeWidthCurveLines(res, strokewidthCurveLine);
            view2.setStrokeWidthConstructionLines(res, strokewidthConstructionLines);
        }
    }

    public static void persistStrokeWidths(Context context, float strokeWidthControlPoints, float strokeWidthCurveLine, float strokeWidthConstructionLines) {

        String msg = String.format(Locale.getDefault(), "write SharedPreferences: ==> %f, %f, %f", strokeWidthControlPoints, strokeWidthCurveLine, strokeWidthConstructionLines);
        Log.v("PeLo", msg);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPref.edit();

        String key = context.getString(R.string.shared_pref_strokewidth_control_points);
        editor.putFloat(key, strokeWidthControlPoints);
        key = context.getString(R.string.shared_pref_strokewidth_curve_line);
        editor.putFloat(key, strokeWidthCurveLine);
        key = context.getString(R.string.shared_pref_strokewidth_construction_lines);
        editor.putFloat(key, strokeWidthConstructionLines);

        editor.apply();
    }
}
