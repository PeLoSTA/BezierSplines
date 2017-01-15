package de.peterloos.beziersplines;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.view.ContextThemeWrapper;

import java.util.Locale;

import de.peterloos.beziersplines.utils.LocaleUtils;

/**
 * Created by Peter on 15.01.2017.
 */

public class BezierSplinesApplication extends Application {

    // called when the application is starting, before any other application objects have been created
    @Override
    public void onCreate() {
        super.onCreate();

////        // get user preferred language set locale accordingly new locale(language,country)
//        LocaleUtils.setLocale(new Locale("en"));
//
//        Context context = this.getBaseContext();   // geht getBaseContext  ODER getApplicationContext ?!?!?!?!?!
//
//        // LocaleUtils.updateConfig(this, getBaseContext().getResources().getConfiguration());
//
//        LocaleUtils.updateConfig((ContextThemeWrapper) context);
    }

    // called by the system when the device configuration changes while your component is running
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//
//        LocaleUtils.updateConfig(newConfig);
    }
}
