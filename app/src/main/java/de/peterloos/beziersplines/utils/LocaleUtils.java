package de.peterloos.beziersplines.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;

import java.util.Locale;

import de.peterloos.beziersplines.activities.SettingsActivity;

/**
 * Created by Peter on 15.01.2017.
 */

public class LocaleUtils {

    // TODO: sLocale umbenennen
    private static Locale sLocale;

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getLocaleOfApp(Resources res){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return res.getConfiguration().getLocales().get(0);
        } else{
            // noinspection deprecation
            return res.getConfiguration().locale;
        }
    }

    public static Locale getLocaleOfOS(){
        return Locale.getDefault();
    }


    public static void setLocale(Locale locale) {
        sLocale = locale;
        if(sLocale != null) {
            Locale.setDefault(sLocale);
        }
    }

    public static void updateConfig(ContextThemeWrapper wrapper) {
        if(sLocale != null) {
            Configuration configuration = new Configuration();
            configuration.setLocale(sLocale);
            wrapper.applyOverrideConfiguration(configuration);
        }
    }


    public static void setLocale(Activity activity, Class cls, String lang) {

        Locale myLocale = new Locale(lang);
        Configuration config = new Configuration();
        config.setLocale(myLocale);

        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        res.updateConfiguration(config, dm);


        Intent refresh = new Intent(activity, cls);

        // Intent refresh = new Intent(this.globalContext, SettingsActivity.class)

        activity.startActivity(refresh);
        activity.finish();
    }

}
