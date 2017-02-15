package de.peterloos.beziersplines.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
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
import de.peterloos.beziersplines.views.BezierGridView;
import de.peterloos.beziersplines.views.BezierView;

/**
 * Project: Bézier Splines Simulation
 * Copyright (c) 2017 by PeLo on 23.01.2017. All rights reserved.
 * Contact info: peterloos@gmx.de
 */

public class DemonstrationActivity extends AppCompatActivity implements OnClickListener {

    private static final int DemoViewResolution = 200; // resolution of demonstration view
    private static final int TaskDelay = 30; // animation velocity
    private static final float OffsetFromBorderDp = 15F; // density-independent pixels: offset from border

    private DemonstrationActivity.DemoOperation task;

    private int width;
    private int height;

    // private BezierView bezierView;
    private BezierGridView bezierView;
    private Button buttonStop;
    private Button buttonRestart;
    private TextView textViewResolution;
    private TextView textViewT;

    private List<BezierPoint> demoControlPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // prefer action bar title with two lines
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_demo);
            actionBar.setSubtitle(this.getString(R.string.app_main_title));
        }

        // retrieve control references
        // this.bezierView = (BezierView) this.findViewById(R.id.bezier_view_demo);
        this.bezierView = (BezierGridView) this.findViewById(R.id.bezier_view_demo);
        this.bezierView.setDensityOfGridlines(2);

        this.textViewResolution = (TextView) this.findViewById(R.id.textview_resolution);
        this.textViewT = (TextView) this.findViewById(R.id.textview_t);
        this.buttonStop = (Button) this.findViewById(R.id.button_stop);
        this.buttonRestart = (Button) this.findViewById(R.id.button_restart);

        // connect with event handlers
        this.buttonStop.setOnClickListener(this);
        this.buttonRestart.setOnClickListener(this);

        // initialize bezier view
        this.bezierView.setMode(BezierMode.Demo);
        this.bezierView.setResolution(DemoViewResolution);
        this.bezierView.setT(0);
        this.bezierView.setShowConstruction(true);

        // retrieve shared preferences
        Context context = this.getApplicationContext();
        int strokewidthFactor = SharedPreferencesUtils.getPersistedStrokewidthFactor(context);
        this.bezierView.setStrokewidthFactor(strokewidthFactor);

        // initialize controls
        String resolution = String.format(Locale.getDefault(), "%d", DemoViewResolution);
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

                    DemonstrationActivity.this.computeDemoControlPoints();
                    // DemonstrationActivity.this.computeControlPointsTest_VariantWithCircle();
                }
            }
        });
    }

    // private helper methods
    @SuppressWarnings("unused")
    private void computeDemoControlPoints() {

        if (this.width == -1 || this.height == -1)
            return;

        int numEdges = 8;

        float deltaX = DemonstrationActivity.this.width / (float) numEdges;
        float deltaY = DemonstrationActivity.this.height / (float) numEdges;

        float centerX = DemonstrationActivity.this.width / 2;
        float centerY = DemonstrationActivity.this.height / 2;

        DemonstrationActivity.this.demoControlPoints = BezierUtils.getDemoRectangle(centerX, centerY, deltaX, deltaY, numEdges - 1);
        DemonstrationActivity.this.task = new DemoOperation();
        DemonstrationActivity.this.task.setRunning(true);
        DemonstrationActivity.this.task.execute();
    }

    @SuppressWarnings("unused")
    private void computeControlPointsTest_VariantWithCircle() {

        if (this.width == -1 || this.height == -1)
            return;

        int squareLength = (this.width < this.height) ? this.width : this.height;

        Resources res = this.getResources();
        float offsetFromBorder = BezierView.convertDpToPixel(res, OffsetFromBorderDp);
        float radius = squareLength / 2 - 2 * offsetFromBorder;

        float centerX = this.width / 2;
        float centerY = this.height / 2;

        float arcLength = (float) (2 * Math.PI / 40);

        DemonstrationActivity.this.demoControlPoints =
                BezierUtils.getDemo_SingleCircleOppositeConnected(
                        centerX,
                        centerY,
                        radius,
                        arcLength);

        DemonstrationActivity.this.task = new DemoOperation();
        DemonstrationActivity.this.task.setRunning(true);
        DemonstrationActivity.this.task.execute();
    }

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
                this.task.execute();
            }
        }
    }

    private class DemoOperation extends AsyncTask<Void, UpdateDescriptor, Void> {

        private boolean running;

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            for (int i = 0; i < DemonstrationActivity.this.demoControlPoints.size(); i++) {

                if (!this.running)
                    return null;

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
                    return null;

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
                    return null;

                try {
                    Thread.sleep(TaskDelay);

                    UpdateDescriptor dsc = new UpdateDescriptor(null, (float) 0.01 * i, false, true);
                    this.publishProgress(dsc);

                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void voids) {

            // enable another demo to run ...
            DemonstrationActivity.this.task = null;
        }

        @Override
        protected void onProgressUpdate(UpdateDescriptor... values) {

            if (values.length == 1) {

                UpdateDescriptor dsc = values[0];

                if (dsc.isAddPoint()) {
                    DemonstrationActivity.this.bezierView.addControlPoint(dsc.getP());
                } else if (dsc.isChangeT()) {
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
