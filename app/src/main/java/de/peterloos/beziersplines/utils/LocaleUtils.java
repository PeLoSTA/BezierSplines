package de.peterloos.beziersplines.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Project: Bézier Splines Simulation
 * Copyright (c) 2017 by PeLo on 23.01.2017. All rights reserved.
 * Contact info: peterloos@gmx.de
 */

public class LocaleUtils {

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getLocaleOfApp(Resources res) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getLocale(res);
        } else {
            return getLocaleLegacy(res);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Locale getLocale(Resources res) {
        return res.getConfiguration().getLocales().get(0);
    }

    @SuppressWarnings("deprecation")
    private static Locale getLocaleLegacy(Resources res) {
        return res.getConfiguration().locale;
    }

    public static Locale getLocaleOfOS() {
        return Locale.getDefault();
    }

    public static void setLocale(Context context, Activity activity, Class cls, String language) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language);
        } else {
            updateResourcesLegacy(context, language);
        }

        Intent refresh = new Intent(context, cls);
        activity.startActivity(refresh);
        activity.finish();
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static void updateResources(Context context, String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();
        configuration.setLocale(locale);

        context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static void updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();
        configuration.locale = locale;

        DisplayMetrics dm = res.getDisplayMetrics();
        res.updateConfiguration(configuration, dm);
    }
}
