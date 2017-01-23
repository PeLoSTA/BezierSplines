package de.peterloos.beziersplines.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.peterloos.beziersplines.utils.BezierMode;
import de.peterloos.beziersplines.utils.BezierPoint;
import de.peterloos.beziersplines.R;
import de.peterloos.beziersplines.utils.BezierUtils;
import de.peterloos.beziersplines.utils.SharedPreferencesUtils;
import de.peterloos.beziersplines.views.BezierView;

/**
 * Project: Bézier Splines Simulation
 * Copyright (c) 2017 by PeLo on 23.01.2017. All rights reserved.
 * Contact info: peterloos@gmx.de
 */

public class DemonstrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int BezierViewResolution = 100;
    private static final int TaskDelay = 30;

    private DemonstrationActivity.DemoOperation task;

    private int width;
    private int height;

    private BezierView bezierView;
    private Button buttonStop;
    private Button buttonRestart;
    private TextView textViewResolution;
    private TextView textViewT;

    private List<BezierPoint> demoControlPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        // prefer action bar title with two lines
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_demo);
            actionBar.setSubtitle(this.getString(R.string.app_main_title));
        }

        // retrieve control references
        this.bezierView = (BezierView) this.findViewById(R.id.bezier_view);
        this.textViewResolution = (TextView) this.findViewById(R.id.textview_resolution);
        this.textViewT = (TextView) this.findViewById(R.id.textview_t);
        this.buttonStop = (Button) this.findViewById(R.id.button_stop);
        this.buttonRestart = (Button) this.findViewById(R.id.button_restart);

        // connect with event handlers
        this.buttonStop.setOnClickListener(this);
        this.buttonRestart.setOnClickListener(this);

        // initialize bezier view
        this.bezierView.setMode(BezierMode.Demo);
        this.bezierView.setResolution(BezierViewResolution);
        this.bezierView.setT(0);
        this.bezierView.setShowConstruction(true);

        // retrieve shared preferences
        Context context = this.getApplicationContext();
        SharedPreferencesUtils.readSharedPreferences(context, this.bezierView);

        // initialize controls
        String resolution = String.format(Locale.getDefault(), "%d", BezierViewResolution);
        this.textViewResolution.setText(resolution);
        String t = String.format(Locale.getDefault(), "%1.2f", 0.0);
        this.textViewT.setText(t);

        this.width = -1;
        this.height = -1;

        this.demoControlPoints = new ArrayList<>();
        this.task = null;

        ViewTreeObserver observer = this.bezierView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (DemonstrationActivity.this.width == -1 && DemonstrationActivity.this.bezierView.getWidth() > 0) {

                    /*
                     * first invocation with (hopefully) final layout data
                     */
                    DemonstrationActivity.this.width = DemonstrationActivity.this.bezierView.getWidth();
                    DemonstrationActivity.this.height = DemonstrationActivity.this.bezierView.getHeight();

                    float centerX = DemonstrationActivity.this.width / 2;
                    float centerY = DemonstrationActivity.this.height / 2;
                    float squareLength = (DemonstrationActivity.this.width < DemonstrationActivity.this.height) ? DemonstrationActivity.this.width : DemonstrationActivity.this.height;
                    float distance = squareLength / 6.5F;   /* so that number of circle doesn't touch border */
                    int numEdges = 6;

                    DemonstrationActivity.this.demoControlPoints = BezierUtils.getDemoRectangle(centerX - distance / 2, centerY - distance / 2, distance, numEdges);
                    DemonstrationActivity.this.task = new DemonstrationActivity.DemoOperation();
                    DemonstrationActivity.this.task.setRunning(true);
                    DemonstrationActivity.this.task.execute("Let's go ...");
                }
            }
        });
    }

    // private helper methods
//    private void computeControlPointsTest_Circle() {
//
//        if (this.width == -1 && this.height == -1)
//            return;
//
//        this.demoControlPoints.clear();
//
//        int squareLength = (this.width < this.height) ? this.width : this.height;
//        double radius = squareLength / 2 - 2 * OffsetFromBorder;
//
//        double cX = this.width / 2;
//        double cY = this.height / 2;
//
//        for (double z = 0.0; z < 2 * Math.PI; z += 0.1) {
//            double x = cX + radius * Math.sin(z);
//            double y = cY + radius * Math.cos(z);
//
//            String s = String.format("PeLo", "%f,%f", x, y);
//            Log.v("PeLo", s);
//
//            BezierPoint p = new BezierPoint((int) x, (int) y);
//            this.demoControlPoints.add(p);
//        }
//    }


    @Override
    public void onClick(View view) {

        if (view == this.buttonStop) {

            if (this.task != null) {

                this.task.setRunning(false);
                this.task = null;
            }
        } else if (view == this.buttonRestart) {

            if (this.task == null) {

                this.bezierView.clear();
                this.bezierView.setT(0);
                String t = String.format(Locale.getDefault(), "%1.2f", 0.0);
                this.textViewT.setText(t);
                this.task = new DemonstrationActivity.DemoOperation();
                this.task.setRunning(true);
                this.task.execute("Let's go ...");
            }
        }
    }

    private class DemoOperation extends AsyncTask<String, UpdateDescriptor, String> {

        private boolean running;

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        protected String doInBackground(String... params) {

            for (int i = 0; i < DemonstrationActivity.this.demoControlPoints.size(); i++) {

                if (!this.running)
                    return "Abort";

                try {
                    Thread.sleep(10 * TaskDelay);

                    BezierPoint p = DemonstrationActivity.this.demoControlPoints.get(i);
                    UpdateDescriptor dsc = new UpdateDescriptor(p, (float) 0.0, true, false);
                    this.publishProgress(dsc);

                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }

            for (int i = 0; i <= 100; i++) {

                if (!this.running)
                    return "Abort";

                try {
                    Thread.sleep(TaskDelay);

                    UpdateDescriptor dsc = new UpdateDescriptor(null, (float) 0.01 * i, false, true);
                    this.publishProgress(dsc);

                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }

            for (int i = 100; i >= 0; i--) {

                if (!this.running)
                    return "Abort";

                try {
                    Thread.sleep(TaskDelay);

                    UpdateDescriptor dsc = new UpdateDescriptor(null, (float) 0.01 * i, false, true);
                    this.publishProgress(dsc);

                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }

            return "Executed";
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String result) {

            // enable another demo to run ...
            DemonstrationActivity.this.task = null;
        }

        @Override
        protected void onProgressUpdate(UpdateDescriptor... values) {

            if (values.length == 1) {
                UpdateDescriptor dsc = values[0];

                if (dsc.isAddPoint()) {
                    // Log.v("PeLo", "onProgressUpdate --> " + dsc.getP().toString());
                    DemonstrationActivity.this.bezierView.addControlPoint(dsc.getP());
                } else if (dsc.isChangeT()) {
                    // Log.v("PeLo", "onProgressUpdate --> " + Double.toString(dsc.getT()));
                    DemonstrationActivity.this.bezierView.setT(dsc.getT());
                    String t = String.format(Locale.getDefault(), "%1.2f", dsc.getT());
                    DemonstrationActivity.this.textViewT.setText(t);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {

        if (this.task != null) {
            this.task.setRunning(false);
            this.task = null;
        }

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
