package de.peterloos.beziersplines.activities;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.ActionBar;
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

        // connect with event handlers
        this.buttonEnglish.setOnClickListener(this);
        this.buttonGerman.setOnClickListener(this);
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
