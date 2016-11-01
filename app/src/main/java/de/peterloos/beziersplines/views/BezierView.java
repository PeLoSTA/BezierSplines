package de.peterloos.beziersplines.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.peterloos.beziersplines.utils.BezierMode;
import de.peterloos.beziersplines.utils.BezierPoint;
import de.peterloos.beziersplines.utils.BezierUtils;

public class BezierView extends View implements View.OnTouchListener {

    private static final int CircleRadius = 15;
    private static final int BorderWidth = 2;
    private static final int NumberDistance = 12;
    private static final int TextSize = 24;

    private static final float StrokeWidthLineBetweenControlPoints = 4f;
    private static final float StrokeWidthCurveLine = 6f;
    private static final float StrokeWidthConstructionLine = 3f;

    private static final int ColorLineBetweenControlPoints = 0xFF565656; /* inverted dark gray */
    private static final int ColorCurveLine = Color.RED;
    private static final int ColorConstructionLine = Color.BLUE;

    private static final float NearstDistanceMaximum = 16f;

    private BezierMode mode;

    private List<BezierPoint> controlPoints;

    private boolean showControlPoints;
    private boolean showBezierCurve;
    private boolean showConstruction;

    private float startX;
    private float startY;

    private float touchSlop;

    private Paint linePaint;
    private Paint circlePaint;
    private Paint textPaint;

    private int resolution;
    private float constructionPosition;

    // c'tor
    public BezierView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mode = BezierMode.Create;

        this.touchSlop = ViewConfiguration.get(this.getContext()).getScaledTouchSlop();

        this.controlPoints = new ArrayList<>();

        this.showControlPoints = true;
        this.showBezierCurve = true;
        this.showConstruction = true;

        this.setOnTouchListener(this);

        this.linePaint = new Paint();
        this.linePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        this.linePaint.setStrokeCap(Paint.Cap.ROUND);

        this.circlePaint = new Paint();
        this.circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        this.textPaint = new Paint();
        this.textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        this.textPaint.setColor(Color.BLACK);
        this.textPaint.setTextSize(TextSize);

        this.resolution = 50;
        this.constructionPosition = (float) 0.5;
    }

    // getter/setter
    public void setResolution(int resolution) {
        this.resolution = resolution;
        this.invalidate();
    }

    public void setT(float t) {
        this.constructionPosition = t;
        this.invalidate();
    }

    public void setShowConstruction(boolean showConstruction) {
        this.showConstruction = showConstruction;
        this.invalidate();
    }

    public void setMode(BezierMode mode) {
        this.mode = mode;
    }

    // public interface
    public void clear() {
        this.controlPoints.clear();
        this.invalidate();
    }

    public void addControlPoint(BezierPoint p) {
        this.controlPoints.add(p);
        this.invalidate();
    }

    public void addControlPoints(List<BezierPoint> points) {
        this.controlPoints = points;
        this.invalidate();
    }

    public void removeControlPoint(int index) {
        this.controlPoints.remove(index);
        this.invalidate();
    }

    public void updateControlPoint(int index, BezierPoint p) {
        this.controlPoints.set(index, p);
        this.invalidate();
    }

    // test interface
    public void showScreenshot() {

        List<BezierPoint> circleList;

        // screen-shot (totally random)
        circleList = showScreenshot01();

        // screen-shot (lines)
        // circleList = showScreenshot02();

        // screen-shot (two concentric circles)
        // circleList = showScreenshot03();

        // screet-shot (bunch of lines)
        // circleList = showScreenshot04();

        this.addControlPoints(circleList);
    }

    private List<BezierPoint> showScreenshot01() {
        return BezierUtils.getTotallyRandom(this.getWidth(), this.getHeight(), 70);
    }

    private List<BezierPoint> showScreenshot02() {
        float centerX = this.getWidth() / 2;
        float centerY = this.getHeight() / 2;
        float squareLength = (this.getWidth() < this.getHeight()) ? this.getWidth() : this.getHeight();
        int partitions = 6;
        return BezierUtils.getDemoCircle01(centerX, centerY, squareLength / 2 - 50, partitions);
    }

    private List<BezierPoint> showScreenshot03() {
        float centerX = this.getWidth() / 2;
        float centerY = this.getHeight() / 2;
        float squareLength = (this.getWidth() < this.getHeight()) ? this.getWidth() : this.getHeight();
        float arcLentgth = 0.5f;
        return BezierUtils.getDemoConcentricCircles(centerX, centerY, squareLength / 5, squareLength / 2 - 50, arcLentgth);
    }

    private List<BezierPoint> showScreenshot04() {
        float centerX = this.getWidth() / 2;
        float centerY = this.getHeight() / 2;
        float squareLength = (this.getWidth() < this.getHeight()) ? this.getWidth() : this.getHeight();
        float distance = squareLength / 6;
        int numEdges = 6;
        return BezierUtils.getDemoRectangle(centerX - distance / 2, centerY - distance / 2, distance, numEdges);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (this.showControlPoints)
            this.drawControlPoints(canvas);
        if (this.showBezierCurve)
            this.drawBezierCurve(canvas);
        if (this.showConstruction)
            this.drawConstructionPoints(canvas);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        if (this.mode == BezierMode.Demo)
            return true;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startX = (int) event.getX();
            startY = (int) event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            float endX = event.getX();
            float endY = event.getY();
            float dX = Math.abs(endX - startX);
            float dY = Math.abs(endY - startY);

            if (Math.sqrt(dX * dX + dY * dY) <= this.touchSlop) {
                BezierPoint p = new BezierPoint(this.startX, this.startY);
                if (this.mode == BezierMode.Create) {

                    // add new control point
                    this.addControlPoint(p);
                } else if (this.mode == BezierMode.Delete) {
                    int index = this.getNearestPointIndex(p);
                    if (index == -1) {
                        return true;
                    }

                    // remove this control point
                    this.removeControlPoint(index);
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            BezierPoint p = new BezierPoint((int) event.getX(), (int) event.getY());
            if (this.mode == BezierMode.Edit) {
                int dragIndex = this.getNearestPointIndex(p);
                if (dragIndex == -1) {
                    return true;
                }

                // update control point
                this.updateControlPoint(dragIndex, p);
            }
        }

        return true;
    }

    // private helper methods
    private void drawControlPoints(Canvas canvas) {
        int numPoints = this.controlPoints.size();
        if (numPoints == 0)
            return;

        BezierPoint p0, p1;
        p0 = this.controlPoints.get(0);
        this.drawControlPoint(canvas, p0.getX(), p0.getY(), "0");

        for (int i = 1; i < numPoints; i++) {
            p1 = this.controlPoints.get(i);
            String s = Integer.toString(i);
            this.drawControlPoint(canvas, p1.getX(), p1.getY(), s);
            this.drawLineBetweenControlPoints(canvas, p0, p1);
            p0 = p1;
        }
    }

    private void drawBezierCurve(Canvas canvas) {
        int numPoints = this.controlPoints.size();
        if (numPoints < 3)
            return;

        BezierPoint p0 = this.controlPoints.get(0);
        for (int i = 1; i <= this.resolution; i++) {
            float t = ((float) i) / ((float) this.resolution);
            BezierPoint p1 = this.computeBezierPoint(t);
            this.drawCurveLine(canvas, p0, p1);
            p0 = p1;
        }
    }

//    // alternative implementation of drawBezierCurve - using drawLines
//    private void drawBezierCurve(Canvas canvas) {
//        int numPoints = this.controlPoints.size();
//        if (numPoints < 3)
//            return;
//
//        ArrayList<Float> points = new ArrayList<>();
//
//        BezierPoint p0 = this.controlPoints.get(0);
//        for (int i = 1; i <= this.resolution; i++) {
//            float t = ((float) i) / ((float) this.resolution);
//            BezierPoint p1 = this.computeBezierPoint(t);
//
//            points.add(p0.getX());
//            points.add(p0.getY());
//            points.add(p1.getX());
//            points.add(p1.getY());
//
//
//            p0 = p1;
//        }
//
//        // convert List<Float> to float[] - the only way to create a genuine array
//        float[] pts = new float [points.size()];
//        int i = 0;
//        for (float f : points) {
//            pts[i++] = f;
//        }
//
//        this.linePaint.setColor(ColorCurveLine);
//        this.linePaint.setStrokeWidth(StrokeWidthCurveLine);
//        canvas.drawLines(pts, this.linePaint );
//    }

    // helper function to compute a bezier point defined by the control polygon at value t
    private BezierPoint computeBezierPoint(float t) {
        int numPoints = this.controlPoints.size();
        BezierPoint[] dest = new BezierPoint[numPoints];
        BezierPoint[] src = new BezierPoint[numPoints];

        this.controlPoints.toArray(src);
        System.arraycopy(src, 0, dest, 0, numPoints);

        for (int n = dest.length; n > 0; n--)
            for (int i = 1; i < n; i++)
                dest[i - 1] = BezierPoint.Interpolate(dest[i - 1], dest[i], t);

        return dest[0];
    }

    private void drawConstructionPoints(Canvas canvas) {
        int numPoints = this.controlPoints.size();
        if (numPoints < 3)
            return;

        BezierPoint[][] constructionPoints = this.computeConstructionPoints(this.constructionPosition);
        numPoints = constructionPoints.length;

        for (int i = numPoints - 2; i >= 0; i--) {
            BezierPoint[] array = constructionPoints[i];

            BezierPoint p0 = array[0];
            for (int j = 1; j <= i; j++) {
                BezierPoint p1 = array[j];
                this.drawConstructionLine(canvas, p0, p1);
                p0 = p1;
            }
        }

        // drawing Bezier point itself
        BezierPoint pBezier = constructionPoints[0][0];
        String pos = String.format(Locale.getDefault(), "%.2f", this.constructionPosition);
        this.drawConstructionPoint(canvas, pBezier.getX(), pBezier.getY(), pos);
    }

    // helper function to compute all the construction points
    // needed to compute a single bezier point at value t
    private BezierPoint[][] computeConstructionPoints(float t) {
        int numPoints = controlPoints.size();
        BezierPoint[][] constructionPoints = new BezierPoint[numPoints][];

        BezierPoint[] tmpArray = new BezierPoint[numPoints];
        this.controlPoints.toArray(tmpArray);
        constructionPoints[numPoints - 1] = tmpArray;

        for (int n = numPoints - 1; n > 0; n--) {
            tmpArray = new BezierPoint[n];
            for (int i = 1; i <= n; i++) {
                BezierPoint p0 = constructionPoints[n][i - 1];
                BezierPoint p1 = constructionPoints[n][i];
                BezierPoint p = BezierPoint.Interpolate(p0, p1, t);
                tmpArray[i - 1] = p;
            }
            constructionPoints[n - 1] = tmpArray;
        }
        return constructionPoints;
    }

    // helper function to find the nearest control point
    private int getNearestPointIndex(BezierPoint p) {
        int numPoints = controlPoints.size();
        if (numPoints == 0)
            return -1;

        int index = -1;
        float dist = Float.MAX_VALUE;

        for (int i = 0; i < numPoints; i++) {
            BezierPoint p0 = this.controlPoints.get(i);
            float newDist = BezierPoint.Distance(p, p0);
            if (newDist < dist) {
                dist = newDist;
                index = i;
            }
        }
        return (dist < NearstDistanceMaximum) ? index : -1;
    }

    // drawing helper methods for points
    private void drawControlPoint(Canvas canvas, float cx, float cy, String text) {
        this.drawPoint(canvas, cx, cy, Color.LTGRAY, Color.BLACK, text, false);
    }

    private void drawConstructionPoint(Canvas canvas, float cx, float cy, String text) {
        this.drawPoint(canvas, cx, cy, Color.RED, Color.DKGRAY, text, false);
    }

    private void drawPoint(Canvas canvas, float cx, float cy, int colorStart, int colorEnd, String text, boolean drawBorder) {

        Shader shader = new LinearGradient(cx, cy, cx + CircleRadius, cy + CircleRadius, colorStart, colorEnd, Shader.TileMode.CLAMP);
        this.circlePaint.setShader(shader);
        this.circlePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx, cy, CircleRadius, this.circlePaint);
        canvas.drawText(text, cx + NumberDistance, cy - NumberDistance, this.textPaint);

        // draw border, if any
        if (drawBorder) {
            this.circlePaint.setShader(null);
            this.circlePaint.setColor(Color.BLACK);
            this.circlePaint.setStyle(Paint.Style.STROKE);
            this.circlePaint.setStrokeWidth(BorderWidth);
            canvas.drawCircle(cx, cy, CircleRadius - (BorderWidth / 2), this.circlePaint);
        }
    }

    // drawing helper methods for lines
    private void drawConstructionLine(Canvas canvas, BezierPoint p0, BezierPoint p1) {
        this.drawLine(canvas, p0, p1, ColorConstructionLine, StrokeWidthConstructionLine);
    }

    private void drawCurveLine(Canvas canvas, BezierPoint p0, BezierPoint p1) {
        this.drawLine(canvas, p0, p1, ColorCurveLine, StrokeWidthCurveLine);
    }

    private void drawLineBetweenControlPoints(Canvas canvas, BezierPoint p0, BezierPoint p1) {
        this.drawLine(canvas, p0, p1, ColorLineBetweenControlPoints, StrokeWidthLineBetweenControlPoints);
    }

    private void drawLine(Canvas canvas, BezierPoint p0, BezierPoint p1, int color, float strokeWidth) {
        this.linePaint.setColor(color);
        this.linePaint.setStrokeWidth(strokeWidth);
        canvas.drawLine(p0.getX(), p0.getY(), p1.getX(), p1.getY(), this.linePaint);
    }
}
