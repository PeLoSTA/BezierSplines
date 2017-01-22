package de.peterloos.beziersplines.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import de.peterloos.beziersplines.R;
import de.peterloos.beziersplines.activities.BezierGlobals;
import de.peterloos.beziersplines.views.BezierView;

/**
 * Created by Peter on 20.01.2017.
 */

public class SharedPreferencesUtils {

    //     <string name="shared_pref_language">Language</string>
//    public static final String LanguageGerman = "German";
//    public static final String LanguageEnglish = "English";
//    public static final String DefaultLanguage = LanguageEnglish;

    public static String readLanguage(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String key = context.getString(R.string.shared_pref_language);
        String language = sharedPref.getString(key, BezierGlobals.DefaultLanguage);

        String msg = String.format("reading language ==> %s", language);
        Log.v("PeLo", msg);

        return language;
    }

    public static void writeLanguage(Context context, String language) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String msg = String.format("writing language ==> %s", language);
        Log.v("PeLo", msg);

        SharedPreferences.Editor editor = sharedPref.edit();
        String key = context.getString(R.string.shared_pref_language);
        editor.putString(key, language);
        editor.commit();
    }

    public static int readScaleFactor(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String key = context.getString(R.string.shared_pref_scalefactor);
        int scaleFactor = sharedPref.getInt(key, BezierGlobals.DefaultScaleFactor);

        String msg = String.format("reading scaleFactor ==> %d", scaleFactor);
        Log.v("PeLo", msg);

        return scaleFactor;
    }

    public static void writeScaleFactor(Context context, int scaleFactor) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String msg = String.format("writing scaleFactor ==> %d", scaleFactor);
        Log.v("PeLo", msg);

        SharedPreferences.Editor editor = sharedPref.edit();
        String key = context.getString(R.string.shared_pref_scalefactor);
        editor.putInt(key, scaleFactor);
        editor.commit();
    }

    public static void readSharedPreferences(Context context, BezierView view) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String key = context.getString(R.string.shared_pref_strokewidth_control_points);
        float strokewidthControlPoints = sharedPref.getFloat(key, BezierGlobals.StrokeWidthControlPointsDp);
        key = context.getString(R.string.shared_pref_strokewidth_curve_line);
        float strokewidthCurveLine = sharedPref.getFloat(key, BezierGlobals.StrokeWidthCurveLineDp);
        key = context.getString(R.string.shared_pref_strokewidth_construction_lines);
        float strokewidthConstructionLines = sharedPref.getFloat(key, BezierGlobals.StrokeWidthConstructionLinesDp);

        String msg = String.format("read SharedPreferences: ==> %f, %f, %f", strokewidthControlPoints, strokewidthCurveLine, strokewidthConstructionLines);
        Log.v("PeLo", msg);

        // setup Bezier view
        view.setStrokeWidthControlPoints (strokewidthControlPoints);
        view.setStrokeWidthCurveLines (strokewidthCurveLine);
        view.setStrokeWidthConstructionLines (strokewidthConstructionLines);
    }

    public static void writeSharedPreferences(Context context, float strokeWidthControlPoints, float strokeWidthCurveLine, float strokeWidthConstructionLines) {

        String msg = String.format("write SharedPreferences: ==> %f, %f, %f", strokeWidthControlPoints, strokeWidthCurveLine, strokeWidthConstructionLines);
        Log.v("PeLo", msg);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPref.edit();

        String key = context.getString(R.string.shared_pref_strokewidth_control_points);
        editor.putFloat(key, strokeWidthControlPoints);
        key = context.getString(R.string.shared_pref_strokewidth_curve_line);
        editor.putFloat(key, strokeWidthCurveLine);
        key = context.getString(R.string.shared_pref_strokewidth_construction_lines);
        editor.putFloat(key, strokeWidthConstructionLines);

        editor.commit();
    }
}
