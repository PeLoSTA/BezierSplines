package de.peterloos.beziersplines.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.peterloos.beziersplines.BezierGlobals;
import de.peterloos.beziersplines.R;
import de.peterloos.beziersplines.utils.LocaleUtils;
import de.peterloos.beziersplines.utils.SharedPreferencesUtils;

/**
 * Project: Bézier Splines Simulation
 * Copyright (c) 2017 by PeLo on 23.01.2017. All rights reserved.
 * Contact info: peterloos@gmx.de
 */

public class SettingsActivity extends AppCompatActivity {

    private RelativeLayout relativeLayoutStrokeWidth;
    private TextView textviewStrokeWidthHeader;
    private TextView textviewStrokeWidth;

    private RelativeLayout relativeLayoutGridlines;
    private TextView textviewGridlinesHeader;
    private TextView textviewGridlines;

    private RelativeLayout relativeLayoutLanguages;
    private TextView textViewLanguagesHeader;
    private TextView textViewLanguages;

    private String[] scalefactorsDisplayNames;
    private String[] gridlinesDisplayNames;
    private String[] languagesDisplayNames;

    private int indexStrokewidthFactor;
    private int indexTmpStrokewidthFactor;
    private int indexGridlines;
    private int indexTmpGridlines;
    private int indexLanguageId;
    private int indexTmpLanguageId;

    private String resultGridlines;
    private String resultStrokewidth;

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
        this.textviewStrokeWidthHeader = (TextView) this.findViewById(R.id.textview_header_strokewidth);
        this.textviewStrokeWidth = (TextView) this.findViewById(R.id.textview_strokewidth);
        this.textviewGridlinesHeader = (TextView) this.findViewById(R.id.textview_header_gridlines);
        this.textviewGridlines = (TextView) this.findViewById(R.id.textview_gridlines);
        this.textViewLanguagesHeader = (TextView) this.findViewById(R.id.textview_header_languages);
        this.textViewLanguages = (TextView) this.findViewById(R.id.textview_languages);

        // setup controls
        this.textviewStrokeWidthHeader.setText(R.string.settings_stroke_widths_title);
        this.textviewGridlinesHeader.setText(R.string.settings_gridlines_title);
        this.textViewLanguagesHeader.setText(R.string.settings_language_title);

        // read language independent strings for settings activity result handshake
        Resources res = this.getResources();
        this.resultGridlines = res.getString(R.string.result_gridlines);
        this.resultStrokewidth = res.getString(R.string.result_strokewidth);

        // connect 'strokewidth' dialog with a specific RelativeLayout region
        this.relativeLayoutStrokeWidth = (RelativeLayout) this.findViewById(R.id.relative_layout_strokewidth);
        this.relativeLayoutStrokeWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsActivity.this.showAlertDialogStrokeWidth();
            }
        });

        // connect 'gridlines' dialog with a specific RelativeLayout region
        this.relativeLayoutGridlines = (RelativeLayout) this.findViewById(R.id.relative_layout_gridlines);
        this.relativeLayoutGridlines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsActivity.this.showAlertDialogGridlines();
            }
        });

        // connect 'languages' dialog with another specific RelativeLayout region
        this.relativeLayoutLanguages = (RelativeLayout) this.findViewById(R.id.relative_layout_languages);

        // because I didn't succeed to implement app local language selection settings,
        // make this setting invisible. Tests with Nougat (Android N) failed !
        this.relativeLayoutLanguages.setVisibility(View.INVISIBLE);

        this.relativeLayoutLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsActivity.this.showAlertDialogLanguage();
            }
        });

        // read language-dependent names of stroke widths
        this.scalefactorsDisplayNames = res.getStringArray(R.array.settings_stroke_widths);
        this.gridlinesDisplayNames = res.getStringArray(R.array.settings_gridlines);
        this.languagesDisplayNames = res.getStringArray(R.array.settings_languages);

        // read shared preferences (current stroke width)
        Context context = this.getApplicationContext();
        this.indexStrokewidthFactor = SharedPreferencesUtils.getPersistedStrokewidthFactor(context);
        String currentStrokeWidth = this.scalefactorsDisplayNames[this.indexStrokewidthFactor];
        this.textviewStrokeWidth.setText(currentStrokeWidth);

        // read shared preferences (gridlines factor)
        this.indexGridlines = SharedPreferencesUtils.getPersistedGridlinesFactor(context);
        String currentGridlinesFactor = this.gridlinesDisplayNames[this.indexGridlines];
        this.textviewGridlines.setText(currentGridlinesFactor);

        // read shared preferences (current app's language)
        this.indexLanguageId = 0; /* must be 0 or 1 */
        String language = SharedPreferencesUtils.getPersistedLanguage(context);
        if (language.equals(BezierGlobals.LanguageEnglish)) {
            this.indexLanguageId = 0;
        } else if (language.equals(BezierGlobals.LanguageGerman)) {
            this.indexLanguageId = 1;
        }

        String currentLanguage = this.languagesDisplayNames[this.indexLanguageId];
        this.textViewLanguages.setText(currentLanguage);
    }

    private void showAlertDialogStrokeWidth() {

        // need temporary variable in case of user cancels dialog
        this.indexTmpStrokewidthFactor = this.indexStrokewidthFactor;

        Resources res = this.getResources();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        String title = res.getString(R.string.settings_stroke_widths_title);
        alertDialog.setTitle(title);
        alertDialog.setSingleChoiceItems(this.scalefactorsDisplayNames, this.indexStrokewidthFactor, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                SettingsActivity.this.indexTmpStrokewidthFactor = item;
            }
        });
        String cancel = res.getString(R.string.settings_dialog_cancel);
        alertDialog.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
        String ok = res.getString(R.string.settings_dialog_ok);
        alertDialog.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                SettingsActivity.this.indexStrokewidthFactor =
                        SettingsActivity.this.indexTmpStrokewidthFactor;

                // persist strokewidth factor
                Context context = SettingsActivity.this.getApplicationContext();
                SharedPreferencesUtils.persistStrokewidthFactor(context, SettingsActivity.this.indexStrokewidthFactor);

                // update display
                String currentStrokeWidth = SettingsActivity.this.scalefactorsDisplayNames[SettingsActivity.this.indexStrokewidthFactor];
                SettingsActivity.this.textviewStrokeWidth.setText(currentStrokeWidth);

                // return to main activity
                Intent intent = new Intent();
                intent.putExtra(SettingsActivity.this.resultStrokewidth, SettingsActivity.this.indexStrokewidthFactor);
                SettingsActivity.this.setResult(RESULT_OK, intent);
                SettingsActivity.this.finish();
            }
        });

        AlertDialog strokewidthDialog = alertDialog.create();
        strokewidthDialog.show();
    }

    private void showAlertDialogGridlines() {

        // need temporary variable in case of user cancels dialog
        this.indexTmpGridlines = this.indexGridlines;

        Resources res = this.getResources();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        String title = res.getString(R.string.settings_gridlines_title);
        alertDialog.setTitle(title);
        alertDialog.setSingleChoiceItems(this.gridlinesDisplayNames, this.indexGridlines, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                SettingsActivity.this.indexTmpGridlines = item;
            }
        });
        String cancel = res.getString(R.string.settings_dialog_cancel);
        alertDialog.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
        String ok = res.getString(R.string.settings_dialog_ok);
        alertDialog.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                SettingsActivity.this.indexGridlines = SettingsActivity.this.indexTmpGridlines;

                // persist gridlines factor
                Context context = SettingsActivity.this.getApplicationContext();
                SharedPreferencesUtils.persistGridlinesFactor(context, SettingsActivity.this.indexGridlines);

                // update display
                String currentGridlinesFactor = SettingsActivity.this.gridlinesDisplayNames[SettingsActivity.this.indexGridlines];
                SettingsActivity.this.textviewGridlines.setText(currentGridlinesFactor);

                // return to main activity
                Intent intent = new Intent();
                intent.putExtra(SettingsActivity.this.resultGridlines, SettingsActivity.this.indexGridlines);
                SettingsActivity.this.setResult(RESULT_OK, intent);
                SettingsActivity.this.finish();
            }
        });

        AlertDialog gridlinesDialog = alertDialog.create();
        gridlinesDialog.show();
    }

    private void showAlertDialogLanguage() {

        Resources res = this.getResources();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        String title = res.getString(R.string.settings_language_title);

        // need temporary variable in case of user cancels dialog
        SettingsActivity.this.indexTmpLanguageId = SettingsActivity.this.indexLanguageId;

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

                // update shared preferences
                Context context = SettingsActivity.this.getApplicationContext();
                if (SettingsActivity.this.indexLanguageId == 0) {
                    SharedPreferencesUtils.persistLanguage(context, BezierGlobals.LanguageEnglish);
                } else if (SettingsActivity.this.indexLanguageId == 1) {
                    SharedPreferencesUtils.persistLanguage(context, BezierGlobals.LanguageGerman);
                }

                // update view
                String currentLanguage = SettingsActivity.this.languagesDisplayNames[SettingsActivity.this.indexLanguageId];
                SettingsActivity.this.textViewLanguages.setText(currentLanguage);

                // switch language within app
                String lang = (SettingsActivity.this.indexLanguageId == 0) ? "en" : "de";
                Context currentContext = SettingsActivity.this.getApplicationContext();
                LocaleUtils.setLocale(currentContext, SettingsActivity.this, SettingsActivity.class, lang);
            }
        });

        AlertDialog languageDialog = alertDialog.create();
        languageDialog.show();
    }
}
