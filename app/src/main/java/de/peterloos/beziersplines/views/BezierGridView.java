package de.peterloos.beziersplines.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import de.peterloos.beziersplines.utils.BezierPoint;

/**
 * Project: Bézier Splines Simulation
 * Copyright (c) 2017 by PeLo on 28.01.2017. All rights reserved.
 * Contact info: peterloos@gmx.de
 */

public class BezierGridView extends BezierView {

    private static final String TAG = "PeLo";

    private static final int NumCellRows = 16;
    private static final int NumCellCols = 16;

    private float cellHeight;
    private float cellWidth;
    private float cellHeightHalf;
    private float cellWidthHalf;

    private Paint linePaint;

    public BezierGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // setup grid details
        this.cellHeight = 0;
        this.cellWidth = 0;

        // setup paint object
        this.linePaint = new Paint();
        this.linePaint.setStrokeWidth(2);
        this.linePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // this.linePaint.setStyle(Paint.Style.FILL_AND_STROKE);   // TODO ??????? Was tut das ????
        this.linePaint.setColor(Color.WHITE);
        // this.linePaint.setStrokeCap(Paint.Cap.ROUND);  // TODO ??????? Evtl besser damit ????
    }

    @Override
    protected void setActualSize(int width, int height) {
        Log.v(TAG, "setActualSize ==> BezierGridView");

        super.setActualSize(width, height);

        this.cellWidth = width / NumCellCols;
        this.cellHeight = height / NumCellRows;

        this.cellWidthHalf = this.cellWidth / 2.0F;
        this.cellHeightHalf = this.cellHeight / 2.0F;
    }

    // properties
    // TODO


    public void addControlPoint(BezierPoint p) {
        Log.v(TAG, "======> child::addControlPoint");

        this.snapPoint(p);   // TODO

        super.addControlPoint(p);

        // super.addControlPoint(p);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        this.drawGrid (canvas);
        super.onDraw(canvas);
    }

    // private helper methods
    private void drawGrid (Canvas canvas) {

        if (this.cellWidth == 0 || this.cellHeight == 0)
            return;

        // draw horizontal lines
        for (int i = 0; i < NumCellRows; i++) {
            canvas.drawLine(0, i * this.cellHeight, this.viewWidth, i * this.cellHeight, this.linePaint);
        }

        // draw vertical lines
        for (int i = 0; i < NumCellCols; i++) {
            canvas.drawLine(i * this.cellWidth, 0, i * this.cellWidth, this.viewHeight, this.linePaint);
        }
    }

//    private BezierPoint computeSnapPoint (float xPos, float yPos) {
//
//        float leftX =  xPos / (float) this.cellWidth;
//
//        float snapX = (xPos <= leftX + (this.cellWidth * 0.5F)) ? leftX : leftX + this.cellWidth;
//
//        float upperY = yPos / (float) this.cellHeight;
//
//        float snapY = (yPos <= upperY + (this.cellHeight * 0.5F)) ? upperY : upperY + this.cellHeight;
//
//        return new BezierPoint(snapX, snapY);
//    }

//    private void snapPoint (BezierPoint p) {
//
//        float realLeft = (float) Math.floor((p.getX() / (float) this.cellWidth)) * this.cellWidth;
//        float snapX = (p.getX() <= realLeft + (this.cellWidth * 0.5F)) ? realLeft : realLeft + this.cellWidth;
//
//        float realUpper = (float) Math.floor((p.getY() / (float) this.cellHeight)) * this.cellHeight;
//        float snapY = (p.getY() <= realUpper + (this.cellHeight * 0.5F)) ? realUpper : realUpper + this.cellHeight;
//
//        p.setX(snapX);
//        p.setY(snapY);
//
////        private float cellHeightHalf;
////        private float cellWidthHalf;
////
////
//        // return new BezierPoint(snapX, snapY);
//    }

    private void snapPoint (BezierPoint p) {

        float realLeft = (float) Math.floor((p.getX() / (float) this.cellWidth)) * this.cellWidth;
        float snapX = (p.getX() <= realLeft + this.cellWidthHalf) ? realLeft : realLeft + this.cellWidth;

        float realUpper = (float) Math.floor((p.getY() / (float) this.cellHeight)) * this.cellHeight;
        float snapY = (p.getY() <= realUpper + this.cellHeightHalf) ? realUpper : realUpper + this.cellHeight;

        p.setX(snapX);
        p.setY(snapY);
    }
}
