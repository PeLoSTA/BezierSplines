package de.peterloos.beziersplines.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ViewSwitcher;

import java.util.Locale;

import de.peterloos.beziersplines.utils.BezierMode;
import de.peterloos.beziersplines.utils.LocaleUtils;
import de.peterloos.beziersplines.utils.SharedPreferencesUtils;
import de.peterloos.beziersplines.views.BezierGridView;
import de.peterloos.beziersplines.views.BezierLogging;
import de.peterloos.beziersplines.views.BezierView;
import de.peterloos.beziersplines.R;

/**
 * Project: Bézier Splines Simulation
 * Copyright (c) 2017 by PeLo on 23.01.2017. All rights reserved.
 * Contact info: peterloos@gmx.de
 */

public class MainActivity
    extends AppCompatActivity
    implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener, BezierLogging {

    private static final String TAG = "PeLo";

    private ViewSwitcher viewSwitcher;
    private BezierView bezierViewWithoutGrid;
    private BezierGridView bezierViewWithGrid;
    private CheckBox checkboxConstruction;
    private CheckBox checkboxSnaptogrid;
    private TextView textViewInfo;
    private SeekBar seekBarResolution;
    private SeekBar seekBarT;
    private TextView textViewResolution;
    private TextView textViewT;
    private Spinner spinnerMode;
    private TableRow tableRowConstruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v(TAG, "==============================================================================");
        Log.v(TAG, "onCreate");

        this.setContentView(R.layout.activity_main);

        // prefer action bar title with two lines
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_main_title);
            actionBar.setSubtitle(this.getString(R.string.app_sub_title));
        }

        // retrieve control references
        this.viewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher);
        this.bezierViewWithoutGrid = (BezierView) this.findViewById(R.id.bezier_view_withoutgrid);
        this.bezierViewWithGrid = (BezierGridView) this.findViewById(R.id.bezier_view_withgrid);
        this.checkboxConstruction = (CheckBox) this.findViewById(R.id.checkbox_show_construction);
        this.checkboxSnaptogrid = (CheckBox) this.findViewById(R.id.checkbox_show_snaptogrid);
        this.textViewInfo = (TextView) this.findViewById(R.id.textview_info);
        this.seekBarResolution = (SeekBar) this.findViewById(R.id.seekbar_resolution);
        this.seekBarT = (SeekBar) this.findViewById(R.id.seekbar_t);
        this.textViewResolution = (TextView) this.findViewById(R.id.textview_resolution);
        this.textViewT = (TextView) this.findViewById(R.id.textview_t);
        this.spinnerMode = (Spinner) this.findViewById(R.id.spinner_editormode);
        this.tableRowConstruction = (TableRow) this.findViewById(R.id.t_seekbar);

        // connect with event handlers
        this.checkboxConstruction.setOnClickListener(this);
        this.checkboxSnaptogrid.setOnClickListener(this);
        this.seekBarResolution.setOnSeekBarChangeListener(this);
        this.seekBarT.setOnSeekBarChangeListener(this);
        this.spinnerMode.setOnItemSelectedListener(this);

        // initialize controls
        this.seekBarResolution.setProgress(50);
        this.seekBarT.setProgress(50);
        this.bezierViewWithoutGrid.setShowConstruction(false);
        this.bezierViewWithGrid.setShowConstruction(false);

        this.checkboxConstruction.setChecked(false);
        this.checkboxSnaptogrid.setChecked(false);
        this.tableRowConstruction.setVisibility(View.GONE);

        // sync shared preferences settings with bezier view
        Context context = this.getApplicationContext();
        SharedPreferencesUtils.getPersistedStrokeWidths(context, this.bezierViewWithoutGrid, this.bezierViewWithGrid);

        // read shared preferences (gridlines factor)
        int gridlinesFactor = SharedPreferencesUtils.getPersistedGridlinesFactor(context);
        this.bezierViewWithGrid.setDensityOfGridlines(gridlinesFactor);

        // connect event sink with clients
        this.bezierViewWithoutGrid.registerListener(this);
        this.bezierViewWithGrid.registerListener(this);

        // sync shared preferences settings with language:
        // implemented - but didn't work with Android 'Nougat'
        // this.syncSharedPrefsWithLanguage(context);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the values you need from your textview into "outState"-object
        Log.v(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        this.checkboxConstruction.setChecked(false);
        this.checkboxSnaptogrid.setChecked(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_action_settings) {
            Context currentContext = this.getApplicationContext();
            Intent settingsIntent = new Intent(currentContext, SettingsActivity.class);
            this.startActivity(settingsIntent);
        } else if (id == R.id.menu_action_demo) {
            Context currentContext = this.getApplicationContext();
            Intent demoIntent = new Intent(currentContext, DemonstrationActivity.class);
            this.startActivity(demoIntent);
        } else if (id == R.id.menu_action_about) {
            Context currentContext = this.getApplicationContext();
            Intent demoIntent = new Intent(currentContext, AboutActivity.class);
            this.startActivity(demoIntent);
        } else if (id == R.id.menu_action_docs) {
            Context currentContext = this.getApplicationContext();
            Intent demoIntent = new Intent(currentContext, DocumentationActivity.class);
            this.startActivity(demoIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * implementing interface 'View.OnClickListener'
     */

    @Override
    public void onClick(View view) {

        if (view == this.checkboxConstruction) {
            if (this.checkboxConstruction.isChecked()) {
                this.bezierViewWithoutGrid.setShowConstruction(true);
                this.bezierViewWithGrid.setShowConstruction(true);
                this.tableRowConstruction.setVisibility(View.VISIBLE);
            } else {
                this.bezierViewWithoutGrid.setShowConstruction(false);
                this.bezierViewWithGrid.setShowConstruction(false);
                this.tableRowConstruction.setVisibility(View.GONE);
            }
        } else if (view == this.checkboxSnaptogrid) {
            if (this.checkboxSnaptogrid.isChecked()) {
                Log.v(TAG, "Switch Normal View to Grid View");
                this.bezierViewWithGrid.setMode(BezierMode.Create);
                this.bezierViewWithGrid.clear();
                this.viewSwitcher.showNext();

            } else {
                Log.v(TAG, "Switch Grid View to Normal View");
                this.bezierViewWithoutGrid.clear();
                this.bezierViewWithoutGrid.setMode(BezierMode.Create);
                this.viewSwitcher.showPrevious();
            }

            // needed for both views
            this.seekBarResolution.setProgress(50);
            this.seekBarT.setProgress(50);
            this.spinnerMode.setSelection(0);
        }
    }

    /*
     * implementing interface 'SeekBar.OnSeekBarChangeListener'
     */

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        if (seekBar == this.seekBarResolution) {

            String s = Integer.toString(i);
            this.textViewResolution.setText(s);
            this.bezierViewWithoutGrid.setResolution(i);
            this.bezierViewWithGrid.setResolution(i);
        } else if (seekBar == this.seekBarT) {

            float constructionPosition = (float) 0.01 * i;
            String pos = String.format(Locale.getDefault(), "%.2f", constructionPosition);
            this.textViewT.setText(pos);
            this.bezierViewWithoutGrid.setT(constructionPosition);
            this.bezierViewWithGrid.setT(constructionPosition);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    /*
     * implementing interface 'AdapterView.OnItemSelectedListener'
     */

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.v("PeLo", "onItemSelected: " + Integer.toString(i) + ", " + Long.toString(l));

        switch (i) {
            case 0:
                this.bezierViewWithoutGrid.setMode(BezierMode.Create);
                this.bezierViewWithGrid.setMode(BezierMode.Create);
                break;
            case 1:
                this.bezierViewWithoutGrid.setMode(BezierMode.Edit);
                this.bezierViewWithGrid.setMode(BezierMode.Edit);
                break;
            case 2:
                this.bezierViewWithoutGrid.setMode(BezierMode.Delete);
                this.bezierViewWithGrid.setMode(BezierMode.Delete);
                break;
            case 3:
                this.bezierViewWithoutGrid.clear();
                this.bezierViewWithGrid.clear();
                this.bezierViewWithoutGrid.setMode(BezierMode.Create);
                this.bezierViewWithGrid.setMode(BezierMode.Create);
                this.seekBarResolution.setProgress(50);
                this.seekBarT.setProgress(50);
                this.spinnerMode.setSelection(0);

                // JUST FOR MAKING SCREENSHOTS: Begin
                // this.bezierView.showScreenshot();
                // JUST FOR MAKING SCREENSHOTS: End
                break;
            case 4:
                this.bezierViewWithoutGrid.setMode(BezierMode.Demo);
                this.bezierViewWithGrid.setMode(BezierMode.Demo);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.v("PeLo", "onNothingSelected");
    }

    /*
     * implementing interface 'BezierLogging'
     */

    @Override
    public void setCurrentInfo(String info) {
        this.textViewInfo.setText(info);
    }

    // private helper methods
    @SuppressWarnings("unused")
    private void syncSharedPrefsWithLanguage(Context context) {
        if (!SharedPreferencesUtils.existsLanguagePreference(context)) {

            // no preferences available, create language preferences conform to language of this device
            Locale localeOfDevice = LocaleUtils.getLocaleOfOS();
            if (localeOfDevice.getLanguage().equals("de")) {
                SharedPreferencesUtils.persistLanguage(context, BezierGlobals.LanguageGerman);
            } else {
                SharedPreferencesUtils.persistLanguage(context, BezierGlobals.LanguageEnglish);
            }
        } else {

            // preferences available, sync language preference with language of this app
            Resources res = this.getResources();
            Locale localeOfApp = LocaleUtils.getLocaleOfApp(res);
            String prefLanguage = SharedPreferencesUtils.getPersistedLanguage(this);

            if (prefLanguage.equals(BezierGlobals.LanguageEnglish) && !localeOfApp.getLanguage().equals("en")) {
                LocaleUtils.setLocale(context, MainActivity.this, MainActivity.class, "en");
            } else if (prefLanguage.equals(BezierGlobals.LanguageGerman) && !localeOfApp.getLanguage().equals("de")) {
                LocaleUtils.setLocale(context, MainActivity.this, MainActivity.class, "de");
            }
        }
    }
}
