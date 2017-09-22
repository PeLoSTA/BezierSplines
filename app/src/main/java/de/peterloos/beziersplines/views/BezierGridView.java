package de.peterloos.beziersplines.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import de.peterloos.beziersplines.BezierGlobals;
import de.peterloos.beziersplines.utils.BezierPoint;

/**
 * Project: Bézier Splines Simulation
 * Copyright (c) 2017 by PeLo on 28.01.2017. All rights reserved.
 * Contact info: peterloos@gmx.de
 */

public class BezierGridView extends BezierView {

    private int density;
    private int densities[];

    private int numCellRows;
    private int numCellCols;

    private double cellHeight;
    private double cellWidth;
    private double cellHeightHalf;
    private double cellWidthHalf;

    private Paint linePaint;

    // c'tor
    public BezierGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Log.v(BezierGlobals.TAG, "c'tor BezierGridView");

        // setup grid details
        this.cellHeight = 0;
        this.cellWidth = 0;

        // setup paint object
        this.linePaint = new Paint();
        this.linePaint.setStrokeWidth(2);
        this.linePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        this.linePaint.setColor(Color.WHITE);
        this.linePaint.setStrokeCap(Paint.Cap.ROUND);

        // setup density of gridlines
        this.density = 1;
        this.densities = new int[]{
                BezierGlobals.GridlinesDensityLow,
                BezierGlobals.GridlinesDensityNormal,
                BezierGlobals.GridlinesDensityHigh
        };
    }

    @Override
    protected void setActualSize(int width, int height) {

        super.setActualSize(width, height);
        this.calculateCellSize();
    }

    @Override
    public void addControlPoint(BezierPoint p) {
        this.snapPoint(p);
        super.addControlPoint(p);
    }

    @Override
    public void updateControlPoint(int index, BezierPoint p) {
        this.snapPoint(p);
        super.updateControlPoint(index, p);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.drawGrid(canvas);
        super.onDraw(canvas);
    }

    // public interface
    public void setDensityOfGridlines(int density) {
        this.density = density;
        this.calculateCellSize();
        this.invalidate();
    }

    // private helper methods
    private void calculateCellSize() {
        // calculate cell sizes (according to portrait or landscape mode)
        int numCells = this.densities[this.density];
        if (this.viewWidth <= this.viewHeight) {

            this.numCellCols = numCells;
            this.numCellRows = Math.round((float) this.viewHeight / this.viewWidth * numCells);
        } else {
            this.numCellRows = numCells;
            this.numCellCols = Math.round((float) this.viewWidth / this.viewHeight * numCells);
        }

        this.cellWidth = (double) this.viewWidth / (double) this.numCellCols;
        this.cellHeight = (double) this.viewHeight / (double) this.numCellRows;
        this.cellWidthHalf = this.cellWidth / 2.0;
        this.cellHeightHalf = this.cellHeight / 2.0;
    }

    private void drawGrid(Canvas canvas) {

        if (this.cellWidth == 0 || this.cellHeight == 0)
            return;

        // draw horizontal lines
        for (int i = 0; i < this.numCellRows; i++) {
            canvas.drawLine(
                0,
                (float) (i * this.cellHeight),
                (float) this.viewWidth,
                (float) (i * this.cellHeight),
                this.linePaint);
        }

        // draw vertical lines
        for (int i = 0; i < this.numCellCols; i++) {
            canvas.drawLine(
                (float) (i * this.cellWidth),
                0,
                (float) (i * this.cellWidth),
                (float) this.viewHeight,
                this.linePaint);
        }
    }

    private void snapPoint(BezierPoint p) {

        double realLeft = Math.floor((p.getX() / this.cellWidth)) * this.cellWidth;
        float snapX = (p.getX() <= realLeft + this.cellWidthHalf) ?
            (float) realLeft : (float) (realLeft + this.cellWidth);
        double realUpper = Math.floor((p.getY() / this.cellHeight)) * this.cellHeight;
        float snapY = (p.getY() <= realUpper + this.cellHeightHalf) ?
            (float) realUpper : (float) (realUpper + this.cellHeight);

        p.setX(snapX);
        p.setY(snapY);
    }
}
