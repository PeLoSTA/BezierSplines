package de.peterloos.beziersplines.activities;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
// import android.support.v7.view.ContextThemeWrapper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import de.peterloos.beziersplines.R;
import de.peterloos.beziersplines.utils.LocaleUtils;

public class SettingsActivity extends AppCompatActivity  implements View.OnClickListener {

    private Button buttonEnglish;
    private Button buttonGerman;
    private Button buttonStrokeWidth;

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

        // connect with event handlers
        this.buttonEnglish.setOnClickListener(this);
        this.buttonGerman.setOnClickListener(this);
        this.buttonStrokeWidth.setOnClickListener(this);
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
        else  if (view == this.buttonStrokeWidth) {
            this.showAlertDiologStrokeWidth();
        }
    }

    AlertDialog levelDialog = null;

    private void showAlertDiologStrokeWidth() {


// Strings to Show In Dialog with Radio Buttons
        final CharSequence[] items = {" Easy "," Medium "," Hard "," Very Hard "};

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select The Difficulty Level");
        builder.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:
                        // Your code when first option seletced
                        break;
                    case 1:
                        // Your code when 2nd  option seletced
                        break;
                    case 2:
                        // Your code when 3rd option seletced
                        break;
                    case 3:
                        // Your code when 4th  option seletced
                        break;

                }

                levelDialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        levelDialog = builder.create();
        levelDialog.show();
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
