package de.peterloos.beziersplines.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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
        implements OnClickListener, OnSeekBarChangeListener, OnItemSelectedListener, BezierLogging {

    private static final String TAG = "PeLo";

    private static final int REQUESTCODE_SETTINGS = 1;

    // keys for generating screenshots (Google Play Store)
    public static final int SCREENSHOT_TOTALLY_RANDOM = 0;
    public static final int SCREENSHOT_SINGLE_CIRCLE = 1;
    public static final int SCREENSHOT_SINGLE_CIRCLE_OPPOSITE_CONNECTED = 2;
    public static final int SCREENSHOT_CONCENTRIC_CIRCLES = 3;
    public static final int SCREENSHOT_CASCADING_RECTANGLES = 4;
    public static final int SCREENSHOT_NICE_FIGURE = 5;
    public static final int SCREENSHOT_NICE_FIGURE_02 = 6;

    // controls
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

    private String resultGridlines;
    private String resultStrokewidth;

    private int resolution;
    private boolean gridIsVisible;
    private boolean constructionIsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Log.v(TAG, "onCreate ------------------------------------------------------------------------");

        // prefer action bar title with two lines
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_main_title);
            actionBar.setSubtitle(this.getString(R.string.app_sub_title));
        }

        // support orientation changes
        this.resolution = 50;
        this.gridIsVisible = false;
        this.constructionIsVisible = false;

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
        this.seekBarResolution.setProgress(this.resolution);
        this.seekBarT.setProgress(50);
        this.bezierViewWithoutGrid.setShowConstruction(false);
        this.bezierViewWithGrid.setShowConstruction(false);

        this.checkboxConstruction.setChecked(this.constructionIsVisible);
        this.checkboxSnaptogrid.setChecked(this.gridIsVisible);
        this.tableRowConstruction.setVisibility(View.GONE);

        // read language independent strings for settings activity result handshake
        Resources res = this.getResources();
        this.resultGridlines = res.getString(R.string.result_gridlines);
        this.resultStrokewidth = res.getString(R.string.result_strokewidth);

        // sync shared preferences settings with bezier view
        Context context = this.getApplicationContext();
        int strokewidthFactor = SharedPreferencesUtils.getPersistedStrokewidthFactor(context);
        this.bezierViewWithoutGrid.setStrokewidthFactor(strokewidthFactor);
        this.bezierViewWithGrid.setStrokewidthFactor(strokewidthFactor);

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
            this.startActivityForResult(settingsIntent, REQUESTCODE_SETTINGS);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE_SETTINGS) {
            if (resultCode == RESULT_OK) {

                int gridlinesFactor = data.getIntExtra(this.resultGridlines, -1);
                int strokewidthFactor = data.getIntExtra(this.resultStrokewidth, -1);

                if (gridlinesFactor != -1) {
                    this.bezierViewWithGrid.setDensityOfGridlines(gridlinesFactor);
                }

                if (strokewidthFactor != -1) {
                    this.bezierViewWithoutGrid.setStrokewidthFactor(strokewidthFactor);
                    this.bezierViewWithGrid.setStrokewidthFactor(strokewidthFactor);
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.v(TAG, "onActivityResult -> RESULT_CANCELED");
            }
        }
    }

    /*
     * implementing interface 'View.OnClickListener'
     */

    @Override
    public void onClick(View view) {

        if (view == this.checkboxConstruction) {
            if (this.checkboxConstruction.isChecked()) {
                this.constructionIsVisible = true;
                this.bezierViewWithoutGrid.setShowConstruction(true);
                this.bezierViewWithGrid.setShowConstruction(true);
                this.tableRowConstruction.setVisibility(View.VISIBLE);
            } else {
                this.constructionIsVisible = false;
                this.bezierViewWithoutGrid.setShowConstruction(false);
                this.bezierViewWithGrid.setShowConstruction(false);
                this.tableRowConstruction.setVisibility(View.GONE);
            }
        } else if (view == this.checkboxSnaptogrid) {
            if (this.checkboxSnaptogrid.isChecked()) {
                this.gridIsVisible = true;
                this.bezierViewWithGrid.setMode(BezierMode.Create);
                this.bezierViewWithGrid.clear();
                this.viewSwitcher.showNext();

            } else {
                this.gridIsVisible = false;
                this.bezierViewWithoutGrid.clear();
                this.bezierViewWithoutGrid.setMode(BezierMode.Create);
                this.viewSwitcher.showPrevious();
            }

            // needed for both views
            this.seekBarResolution.setProgress(this.resolution);
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

            this.resolution = i;
            String s = Integer.toString(this.resolution);
            this.textViewResolution.setText(s);
            this.bezierViewWithoutGrid.setResolution(this.resolution);
            this.bezierViewWithGrid.setResolution(this.resolution);
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
            case 0:  // create mode
                this.bezierViewWithoutGrid.setMode(BezierMode.Create);
                this.bezierViewWithGrid.setMode(BezierMode.Create);
                break;
            case 1:  // edit mode
                this.bezierViewWithoutGrid.setMode(BezierMode.Edit);
                this.bezierViewWithGrid.setMode(BezierMode.Edit);
                break;
            case 2:  // delete single mode
                this.bezierViewWithoutGrid.setMode(BezierMode.Delete);
                this.bezierViewWithGrid.setMode(BezierMode.Delete);
                break;
            case 3:  // delete all mode
                this.bezierViewWithoutGrid.clear();
                this.bezierViewWithGrid.clear();
                this.bezierViewWithoutGrid.setMode(BezierMode.Create);
                this.bezierViewWithGrid.setMode(BezierMode.Create);
                this.seekBarResolution.setProgress(50);
                this.seekBarT.setProgress(50);
                this.spinnerMode.setSelection(0);
                break;
            case 4:  // demo mode
                this.bezierViewWithoutGrid.setMode(BezierMode.Demo);
                this.bezierViewWithoutGrid.showScreenshot(SCREENSHOT_CONCENTRIC_CIRCLES);
                this.bezierViewWithGrid.setMode(BezierMode.Demo);
                this.bezierViewWithGrid.showScreenshot(SCREENSHOT_CONCENTRIC_CIRCLES);

//                this.viewSwitcher.setDisplayedChild(1);
//                this.bezierViewWithGrid.setMode(BezierMode.Demo);
//                this.bezierViewWithGrid.showScreenshot(SCREENSHOT_NICE_FIGURE);

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
