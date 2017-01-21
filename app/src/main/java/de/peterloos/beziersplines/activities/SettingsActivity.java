package de.peterloos.beziersplines.activities;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Locale;

import de.peterloos.beziersplines.R;
import de.peterloos.beziersplines.utils.LocaleUtils;
import de.peterloos.beziersplines.utils.SharedPreferencesUtils;

public class SettingsActivity extends AppCompatActivity  implements View.OnClickListener {

    private static final int NumScaleFactors = 5;

    private Button buttonEnglish;
    private Button buttonGerman;
    private Button buttonStrokeWidth;

    private EditText editTextStrokeWidth;
    private RelativeLayout relativeLayoutStrokeWidth;

    // private SharedPreferences sharedPref;

    // TODO: DIe müssen in die String.xml Dateien ...
    // private final CharSequence[] items = { "Extra Light", "Light", "Normal", "Bold", "Extra Bold" };
    // private final float[] scaleFactors = new float[] { 0.6F, 0.8F, 1.0F, 1.2F, 1.4F };

    private int scaleFactor;
    private int tmpScaleFactor;

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
        this.buttonEnglish = (Button) this.findViewById(R.id.button_englisch);
        this.buttonGerman = (Button) this.findViewById(R.id.button_german);
        this.buttonStrokeWidth = (Button) this.findViewById(R.id.button_strokewidth);
        this.editTextStrokeWidth = (EditText) this.findViewById(R.id.edittext_strokewidth);

        // setup controls
        this.editTextStrokeWidth.setEnabled(false);

        // connect with event handlers
        this.buttonEnglish.setOnClickListener(this);
        this.buttonGerman.setOnClickListener(this);
        this.buttonStrokeWidth.setOnClickListener(this);

        // connect 'Strokewidth' dialog with a specific RelativeLayout region
        this.relativeLayoutStrokeWidth = (RelativeLayout) this.findViewById(R.id.relative_layout_strokewidth);
        this.relativeLayoutStrokeWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsActivity.this.showAlertDiologStrokeWidth();
            }
        });

        // restoring shared preferences
        Context context = this.getApplicationContext();
        this.scaleFactor = SharedPreferencesUtils.readScaleFactor(context);
    }

    @Override
    public void onClick(View view) {
        if (view == this.buttonEnglish) {
            Log.v("PeLo", "english");

            this.setLocale("en");

//            Locale myLocale = new Locale("en");
//            this.updateConfig2 (myLocale);
        } else if (view == this.buttonGerman) {

            Log.v("PeLo", "german");

            this.setLocale("de");

//            Locale myLocale = new Locale("de");
//            this.updateConfig2 (myLocale);
        }
    }


    private void showAlertDiologStrokeWidth() {

        // need temporary variable in case of user cancels dialog
        this.tmpScaleFactor = this.scaleFactor;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Select Stroke Width");
        alertDialog.setSingleChoiceItems(BezierGlobals.ScaleFactorDisplayNames, this.scaleFactor, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                Log.v("PeLo", "item --> " + Integer.toString(item));

                switch(item)
                {
                    case 0:
                        SettingsActivity.this.tmpScaleFactor = 0;
                        break;
                    case 1:
                        SettingsActivity.this.tmpScaleFactor = 1;
                        break;
                    case 2:
                        SettingsActivity.this.tmpScaleFactor = 2;
                        break;
                    case 3:
                        SettingsActivity.this.tmpScaleFactor = 3;
                        break;
                    case 4:
                        SettingsActivity.this.tmpScaleFactor = 4;
                        break;
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                SettingsActivity.this.scaleFactor = SettingsActivity.this.tmpScaleFactor;

                float newStrokeWidthControlPoints = BezierGlobals.StrokeWidthControlPointsDp * BezierGlobals.ScaleFactors[SettingsActivity.this.scaleFactor];
                float newStrokeWidthCurveLine = BezierGlobals.StrokeWidthCurveLineDp * BezierGlobals.ScaleFactors[SettingsActivity.this.scaleFactor];
                float newStrokeWidthConstructionLines = BezierGlobals.StrokeWidthConstructionLinesDp * BezierGlobals.ScaleFactors[SettingsActivity.this.scaleFactor];

                Context context = SettingsActivity.this.getApplicationContext();
                SharedPreferencesUtils.writeScaleFactor(context, SettingsActivity.this.scaleFactor);
                SharedPreferencesUtils.writeSharedPreferences(context, newStrokeWidthControlPoints, newStrokeWidthCurveLine, newStrokeWidthConstructionLines);
            }
        });

        AlertDialog strokewidthDialog = alertDialog.create();
        strokewidthDialog.show();
    }

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
