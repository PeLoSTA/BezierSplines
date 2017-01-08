package de.peterloos.beziersplines.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Locale;

import de.peterloos.beziersplines.utils.BezierMode;
import de.peterloos.beziersplines.views.BezierView;
import de.peterloos.beziersplines.R;

// TODO: Die Seekbars sind recht ungenau ... für schrittweise könnte man auch eine Stepwise Control benötigen#

// TODO: Starte jetzt mit Material Layout
// TODO: Die AsyncTask sollte als dritten Paramer Void bekommen ...
// TODO: Die AsyncTask sollte als ersten Paramer Void bekommen ...

/**
 * main activity of bézier splines app
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener {

    private BezierView bezierView;
    private CheckBox checkboxResolution;
    private SeekBar seekBarResolution;
    private SeekBar seekBarT;
    private TextView textViewResolution;
    private TextView textViewT;
    private Spinner spinnerMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        // prefer Action Bar Title with two lines
//        ActionBar actionBar = this.getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle(R.string.main_title);
//            actionBar.setSubtitle(this.getString(R.string.sub_title));
//        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle(R.string.main_title);
        myToolbar.setSubtitle(this.getString(R.string.sub_title));


        // retrieve control references
        this.bezierView = (BezierView) this.findViewById(R.id.bezier_view);
        this.checkboxResolution = (CheckBox) this.findViewById(R.id.checkbox_show_resolution);
        this.seekBarResolution = (SeekBar) this.findViewById(R.id.seekbar_resolution);
        this.seekBarT = (SeekBar) this.findViewById(R.id.seekbar_t);
        this.textViewResolution = (TextView) this.findViewById(R.id.textview_resolution);
        this.textViewT = (TextView) this.findViewById(R.id.textview_t);
        this.spinnerMode = (Spinner) this.findViewById(R.id.spinner_editormode);

        // connect with event handlers
        this.checkboxResolution.setOnClickListener(this);
        this.seekBarResolution.setOnSeekBarChangeListener(this);
        this.seekBarT.setOnSeekBarChangeListener(this);
        this.spinnerMode.setOnItemSelectedListener(this);

        // initialize controls
        this.checkboxResolution.setChecked(false);
        this.seekBarResolution.setProgress(50);
        this.seekBarT.setProgress(50);
        this.bezierView.setShowConstruction(false);
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

        if (id == R.id.menu_action_demo) {
            Context currentContext = this.getApplicationContext();
            Intent demoIntent = new Intent(currentContext, DemoActivity.class);
            this.startActivity(demoIntent);
        } else if (id == R.id.menu_action_about) {
            Context currentContext = this.getApplicationContext();
            Intent demoIntent = new Intent(currentContext, AboutActivity.class);
            this.startActivity(demoIntent);
        }

        return super.onOptionsItemSelected(item);
    }

//    public boolean onOptionsItemSelected_Alt(MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.menu_action_settings) {
//            Toast.makeText(this, "Pressed Action Settings", Toast.LENGTH_LONG).show();
//        }
//        else if (id == R.id.menu_action_demo) {
//            Context currentContext = this.getApplicationContext();
//            Intent demoIntent = new Intent(currentContext, DemoActivity.class);
//            this.startActivity(demoIntent);
//        }
//        else if (id == R.id.menu_action_about) {
//            Context currentContext = this.getApplicationContext();
//            Intent demoIntent = new Intent(currentContext, AboutActivity.class);
//            this.startActivity(demoIntent);
//        }
//        else if (id == R.id.menu_action_docs) {
//            Context currentContext = this.getApplicationContext();
//            Intent demoIntent = new Intent(currentContext, DocumentationActivity.class);
//            this.startActivity(demoIntent);
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /*
     * implementing interface 'View.OnClickListener'
     */

    @Override
    public void onClick(View view) {

        if (view == this.checkboxResolution) {
            if (this.checkboxResolution.isChecked()) {
                this.bezierView.setShowConstruction(true);
            } else {
                this.bezierView.setShowConstruction(false);
            }
        }
    }

    /*
     * implementing interface 'SeekBar.OnSeekBarChangeListener'
     */

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        if (seekBar == this.seekBarResolution) {

            Log.v("PeLo", "onProgressChanged Resolution: " + Integer.toString(i));

            String s = Integer.toString(i);
            this.textViewResolution.setText(s);
            this.bezierView.setResolution(i);
        } else if (seekBar == this.seekBarT) {

            Log.v("PeLo", "onProgressChanged T: " + Integer.toString(i));

            float constructionPosition = (float) 0.01 * i;
            String pos = String.format(Locale.getDefault(), "%.2f", constructionPosition);
            this.textViewT.setText(pos);
            this.bezierView.setT(constructionPosition);
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
                this.bezierView.setMode(BezierMode.Create);
                break;
            case 1:
                this.bezierView.setMode(BezierMode.Edit);
                break;
            case 2:
                this.bezierView.setMode(BezierMode.Delete);
                break;
            case 3:
                this.bezierView.clear();
                this.bezierView.setMode(BezierMode.Create);
                this.seekBarResolution.setProgress(50);
                this.seekBarT.setProgress(50);
                this.spinnerMode.setSelection(0);

                // JUST FOR MAKING SCREENSHOTS: Begin
                // this.bezierView.showScreenshot();
                // JUST FOR MAKING SCREENSHOTS: End
                break;
            case 4:
                this.bezierView.setMode(BezierMode.Demo);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.v("PeLo", "onNothingSelected");
    }
}
