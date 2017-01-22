package de.peterloos.beziersplines.activities;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

import de.peterloos.beziersplines.R;
import de.peterloos.beziersplines.utils.LocaleUtils;
import de.peterloos.beziersplines.utils.SharedPreferencesUtils;

public class SettingsActivity extends AppCompatActivity /** implements View.OnClickListener **/ {

    private static final int NumScaleFactors = 5;

    private RelativeLayout relativeLayoutStrokeWidth;
    private TextView textViewStrokeWidthHeader;
    private TextView textViewStrokeWidth;

    private RelativeLayout relativeLayoutLanguages;
    private TextView textViewLanguagesHeader;
    private TextView textViewLanguages;

    private String[] scalefactorsDisplayNames;
    private String[] languagesDisplayNames;

    private int indexScaleFactor;
    private int indexTmpScaleFactor;
    private int indexLanguageId;
    private int indexTmpLanguageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // prefer action bar title with two lines
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_settings);
            actionBar.setSubtitle(this.getString(R.string.app_main_title));
        }

        // retrieve control references
        this.textViewStrokeWidthHeader = (TextView) this.findViewById(R.id.textview_header_strokewidth);
        this.textViewStrokeWidth = (TextView) this.findViewById(R.id.textview_strokewidth);
        this.textViewLanguagesHeader = (TextView) this.findViewById(R.id.textview_header_languages);
        this.textViewLanguages = (TextView) this.findViewById(R.id.textview_languages);

        // setup controls
        this.textViewStrokeWidthHeader.setText(R.string.settings_stroke_widths_title);
        this.textViewLanguagesHeader.setText(R.string.settings_language_title);

        // connect 'strokewidth' dialog with a specific RelativeLayout region
        this.relativeLayoutStrokeWidth = (RelativeLayout) this.findViewById(R.id.relative_layout_strokewidth);
        this.relativeLayoutStrokeWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsActivity.this.showAlertDiologStrokeWidth();
            }
        });

        // connect 'languages' dialog with another specific RelativeLayout region
        this.relativeLayoutLanguages = (RelativeLayout) this.findViewById(R.id.relative_layout_languages);
        this.relativeLayoutLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsActivity.this.showAlertDiologLanguage();
            }
        });

        // read language-dependent names of stroke widths
        Resources res = this.getResources();
        this.scalefactorsDisplayNames = res.getStringArray(R.array.settings_stroke_widths);
        this.languagesDisplayNames = res.getStringArray(R.array.settings_languages);

        // read shared preferences (current stroke width)
        Context context = this.getApplicationContext();
        this.indexScaleFactor = SharedPreferencesUtils.readScaleFactor(context);
        String currentStrokeWidth = this.scalefactorsDisplayNames[this.indexScaleFactor];
        this.textViewStrokeWidth.setText (currentStrokeWidth);

        // read shared preferences (current app's language)
        this.indexLanguageId = -1;
        String language = SharedPreferencesUtils.readLanguage(context);
        if (language.equals(BezierGlobals.LanguageEnglish)) {
            this.indexLanguageId = 0;
        }
        else if (language.equals(BezierGlobals.LanguageGerman)) {
            this.indexLanguageId = 1;
        }
        if (this.indexLanguageId >= 0) {
            String currentLanguage = this.languagesDisplayNames[this.indexLanguageId];
            this.textViewLanguages.setText (currentLanguage);
        }





//        // retrieve current app's language setting
//        Locale locale = this.getCurrentLocale();
//        String languageDisplayName = locale.getDisplayLanguage();
//        this.textViewLanguages.setText (languageDisplayName);

//        Log.v("PeLo", "getLanguage ==> " + locale.getLanguage());
//        Log.v("PeLo", "getCountry ==> " + locale.getCountry());
//        Log.v("PeLo", "getDisplayLanguage ==> " + locale.getDisplayLanguage());
//        Log.v("PeLo", "getDisplayCountry ==> " + locale.getDisplayCountry());
    }

//    @Override
//    public void onClick(View view) {
//        if (view == this.buttonEnglish) {
//            Log.v("PeLo", "english");
//
//            this.setLocale("en_US");
//
////            Locale myLocale = new Locale("en");
////            this.updateConfig2 (myLocale);
//        } else if (view == this.buttonGerman) {
//
//            Log.v("PeLo", "german");
//
//            this.setLocale("de_DE");
//
////            Locale myLocale = new Locale("de");
////            this.updateConfig2 (myLocale);
//        }
//    }

    private void showAlertDiologStrokeWidth() {

        // need temporary variable in case of user cancels dialog
        this.indexTmpScaleFactor = this.indexScaleFactor;

        Resources res = this.getResources();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        String title = res.getString(R.string.settings_stroke_widths_title);
        alertDialog.setTitle(title);
        alertDialog.setSingleChoiceItems(this.scalefactorsDisplayNames, this.indexScaleFactor, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                SettingsActivity.this.indexTmpScaleFactor = item;
            }
        });
        String cancel = res.getString(R.string.settings_dialog_cancel);
        alertDialog.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        String ok = res.getString(R.string.settings_dialog_ok);
        alertDialog.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                SettingsActivity.this.indexScaleFactor = SettingsActivity.this.indexTmpScaleFactor;

                float newStrokeWidthControlPoints = BezierGlobals.StrokeWidthControlPointsDp * BezierGlobals.ScaleFactors[SettingsActivity.this.indexScaleFactor];
                float newStrokeWidthCurveLine = BezierGlobals.StrokeWidthCurveLineDp * BezierGlobals.ScaleFactors[SettingsActivity.this.indexScaleFactor];
                float newStrokeWidthConstructionLines = BezierGlobals.StrokeWidthConstructionLinesDp * BezierGlobals.ScaleFactors[SettingsActivity.this.indexScaleFactor];

                Context context = SettingsActivity.this.getApplicationContext();
                SharedPreferencesUtils.writeScaleFactor(context, SettingsActivity.this.indexScaleFactor);
                SharedPreferencesUtils.writeSharedPreferences(context, newStrokeWidthControlPoints, newStrokeWidthCurveLine, newStrokeWidthConstructionLines);

                String currentStrokeWidth = SettingsActivity.this.scalefactorsDisplayNames[SettingsActivity.this.indexScaleFactor];
                SettingsActivity.this.textViewStrokeWidth.setText (currentStrokeWidth);
            }
        });

        AlertDialog strokewidthDialog = alertDialog.create();
        strokewidthDialog.show();
    }

    private void showAlertDiologLanguage() {

        Resources res = this.getResources();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        String title = res.getString(R.string.settings_language_title);

        // retrieve current locale
        Locale current = this.getCurrentLocale();
        SettingsActivity.this.indexTmpLanguageId = -1;
        if (current.getLanguage().equals("en")) {
            SettingsActivity.this.indexTmpLanguageId = 0;
        }
        else if (current.getLanguage().equals("de")) {
            SettingsActivity.this.indexTmpLanguageId = 1;
        }

        alertDialog.setTitle(title);
        alertDialog.setSingleChoiceItems(this.languagesDisplayNames, SettingsActivity.this.indexTmpLanguageId, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                SettingsActivity.this.indexTmpLanguageId = item;
            }
        });
        String cancel = res.getString(R.string.settings_dialog_cancel);
        alertDialog.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        String ok = res.getString(R.string.settings_dialog_ok);
        alertDialog.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                SettingsActivity.this.indexLanguageId = SettingsActivity.this.indexTmpLanguageId;

                Context context = SettingsActivity.this.getApplicationContext();
                if (SettingsActivity.this.indexLanguageId == 0) {
                    SharedPreferencesUtils.writeLanguage(context, BezierGlobals.LanguageEnglish);
                }
                else if (SettingsActivity.this.indexLanguageId == 1) {
                    SharedPreferencesUtils.writeLanguage(context, BezierGlobals.LanguageGerman);
                }

                String currentStrokeWidth = SettingsActivity.this.scalefactorsDisplayNames[SettingsActivity.this.indexScaleFactor];
                SettingsActivity.this.textViewStrokeWidth.setText (currentStrokeWidth);

                String currentLanguage = SettingsActivity.this.languagesDisplayNames[SettingsActivity.this.indexLanguageId];
                SettingsActivity.this.textViewLanguages.setText (currentLanguage);

                // switch language
                SettingsActivity.this.setLocale ((SettingsActivity.this.indexLanguageId == 0) ? "en_US" : "de_DE" );
            }
        });

        AlertDialog strokewidthDialog = alertDialog.create();
        strokewidthDialog.show();
    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return getResources().getConfiguration().getLocales().get(0);
        } else{
            // noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }

    // ALTES ZEUGS ....


    // private helper methods
    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);

//        Resources res = getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();

//        Configuration conf = res.getConfiguration();
//        conf.locale = myLocale;

        Configuration config = new Configuration();
        config.setLocale(myLocale);

        // res.updateConfiguration(conf, dm);

        Application app = this.getApplication();

        Resources res = app.getBaseContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        res.updateConfiguration(config, dm);

     //   res.

        Intent refresh = new Intent(this, SettingsActivity.class);
        startActivity(refresh);
        finish();
    }

    public void updateConfig2(Locale locale) {
        LocaleUtils.setLocale(locale);

        Intent refresh = new Intent(this, SettingsActivity.class);
        startActivity(refresh);
        finish();
    }

    public void updateConfig(Locale locale, ContextThemeWrapper wrapper) {

        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        try {
            wrapper.applyOverrideConfiguration(configuration);
        }
        catch (Exception ex) {
            Log.v("PeLo", ex.getMessage());
        }
    }
}
