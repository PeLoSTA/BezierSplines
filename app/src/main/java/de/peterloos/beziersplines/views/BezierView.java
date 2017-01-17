package de.peterloos.beziersplines.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.peterloos.beziersplines.utils.BezierMode;
import de.peterloos.beziersplines.utils.BezierPoint;
import de.peterloos.beziersplines.utils.BezierUtils;

public class BezierView extends View implements View.OnTouchListener {

    // density-independent pixels for lines
    private static final int StrokeWidthLineBetweenControlPointsDp = 4;
    private static final int StrokeWidthCurveLineDp = 5;
    private static final int StrokeWidthConstructionLineDp = 3;

    // density-independent pixels for circles
    private static final int StrokeWidthCircleRadiusDp = 10;
    private static final int StrokeWidthBorderWidthDp = 2;
    private static final int DistanceFromNumberDp = 12;
    private static final int NearestDistanceMaximumDp = 16;

    // density-independent pixels for text
    private static final int StrokeWidthTextSizeDp = 18;
    private static final int StrokeWidthInfoPaddingDp = 5;

    // color settings
    private static final int ColorLineBetweenControlPoints = 0xFF565656; /* inverted dark gray */
    private static final int ColorCurveLine = Color.RED;
    private static final int ColorConstructionLine = Color.BLUE;

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

    //  real pixel densities for lines
    private float strokeWidthLineBetweenControlPoints;
    private float strokeWidthCurveLine;
    private float strokeWidthConstructionLine;

    //  real pixel densities for circles
    private float strokeWidthCircle;
    private float strokeWidthBorderWidth;
    private float distanceFromNumber;
    private float nearestDistanceMaximum;

    //  real pixel densities for text
    private float strokeTextSize;
    private float strokeWidthInfoPadding;

    //  bézier curve specific parameters
    private int resolution;
    private float constructionPosition;

    // position info for touch gestures
    private String positionInfo;
    private Rect positionBounds;
    private float xPosInfo;
    private float yPosInfo;

    // size of this view
    private int viewDigitsOfWidth;
    private int viewDigitsOfHeight;

    // c'tor
    public BezierView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mode = BezierMode.Create;

        // TODO: Warum wird da this.getContext() gerufem ... haben WIR DAOCH ALS PARAMETER
        // TODO: Die CValls zerlegen ....
        this.touchSlop = ViewConfiguration.get(this.getContext()).getScaledTouchSlop();

        this.controlPoints = new ArrayList<>();

        this.showControlPoints = true;
        this.showBezierCurve = true;
        this.showConstruction = true;

        this.setOnTouchListener(this);

        this.strokeWidthLineBetweenControlPoints = this.convertDpToPixel(StrokeWidthLineBetweenControlPointsDp);
        this.strokeWidthCurveLine = this.convertDpToPixel(StrokeWidthCurveLineDp);
        this.strokeWidthConstructionLine = this.convertDpToPixel(StrokeWidthConstructionLineDp);
        this.strokeWidthCircle = this.convertDpToPixel(StrokeWidthCircleRadiusDp);
        this.strokeWidthBorderWidth = this.convertDpToPixel(StrokeWidthBorderWidthDp);
        this.strokeTextSize = this.convertDpToPixel(StrokeWidthTextSizeDp);
        this.distanceFromNumber = this.convertDpToPixel(DistanceFromNumberDp);
        this.nearestDistanceMaximum = this.convertDpToPixel(NearestDistanceMaximumDp);

        this.linePaint = new Paint();
        this.linePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        this.linePaint.setStrokeCap(Paint.Cap.ROUND);

        this.circlePaint = new Paint();
        this.circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        this.textPaint = new Paint();
        this.textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        this.textPaint.setColor(Color.BLACK);
        this.textPaint.setTextSize(this.strokeTextSize);

        this.resolution = 50;
        this.constructionPosition = (float) 0.5;

        this.positionInfo = "";
        this.positionBounds = null;
        this.xPosInfo = 0.0F;
        this.yPosInfo = 0.0F;
        this.strokeWidthInfoPadding = this.convertDpToPixel(StrokeWidthInfoPaddingDp);

        //  need size of view (when view is visible
        ViewTreeObserver vto = this.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BezierView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int width = BezierView.this.getMeasuredWidth();
                int height = BezierView.this.getMeasuredHeight();

                BezierView.this.viewDigitsOfWidth = BezierView.this.digitsOfNumber(width);
                BezierView.this.viewDigitsOfHeight = BezierView.this.digitsOfNumber(height);

                Log.v("PeLo", "View Metrics (2) ===> " + BezierView.this.viewDigitsOfWidth + "," + BezierView.this.viewDigitsOfHeight);
            }
        });
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
        this.positionInfo = "                ";
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (this.showControlPoints)
            this.drawControlPoints(canvas);
        if (this.showBezierCurve)
            this.drawBezierCurve(canvas);
        if (this.showConstruction)
            this.drawConstructionPoints(canvas);

        this.drawPositionInfo(canvas);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        if (this.mode == BezierMode.Demo)
            return true;

        // prevent touch events outside the bounds of the view
        if (event.getX() <= this.strokeWidthCircle)
            return true;
        if (event.getX() >= this.getWidth() - this.strokeWidthCircle)
            return true;
        if (event.getY() <= this.strokeWidthCircle)
            return true;
        if (event.getY() >= this.getHeight() - this.strokeWidthCircle)
            return true;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            this.startX = event.getX();
            this.startY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            float endX = event.getX();
            float endY = event.getY();
            float dX = Math.abs(endX - this.startX);
            float dY = Math.abs(endY - this.startY);

            if (Math.sqrt(dX * dX + dY * dY) <= this.touchSlop) {
                BezierPoint p = new BezierPoint(this.startX, this.startY);
                if (this.mode == BezierMode.Create) {
                    // add new control point
                    this.addControlPoint(p);
                    this.setTouchPosition((int) p.getX(), (int) p.getY());
                } else if (this.mode == BezierMode.Delete) {
                    int index = this.getNearestPointIndex(p);
                    if (index == -1) {
                        return true;
                    }

                    // remove this control point
                    p = this.controlPoints.get(index);
                    this.setTouchPosition((int) p.getX(), (int) p.getY());
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
                this.setTouchPosition((int) p.getX(), (int) p.getY());
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
        return (dist < this.nearestDistanceMaximum) ? index : -1;
    }

    // drawing helper methods for points
    private void drawControlPoint(Canvas canvas, float cx, float cy, String text) {
        this.drawPoint(canvas, cx, cy, Color.LTGRAY, Color.BLACK, text, false);
    }

    private void drawConstructionPoint(Canvas canvas, float cx, float cy, String text) {
        this.drawPoint(canvas, cx, cy, Color.RED, Color.DKGRAY, text, false);
    }

    private void drawPoint(Canvas canvas, float cx, float cy, int colorStart, int colorEnd, String text, boolean drawBorder) {

        Shader shader = new LinearGradient(cx, cy, cx + this.strokeWidthCircle, cy + this.strokeWidthCircle, colorStart, colorEnd, Shader.TileMode.CLAMP);
        this.circlePaint.setShader(shader);
        this.circlePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx, cy, this.strokeWidthCircle, this.circlePaint);
        canvas.drawText(text, cx + this.distanceFromNumber, cy - this.distanceFromNumber, this.textPaint);

        // draw border, if any
        if (drawBorder) {
            this.circlePaint.setShader(null);
            this.circlePaint.setColor(Color.BLACK);
            this.circlePaint.setStyle(Paint.Style.STROKE);
            this.circlePaint.setStrokeWidth(this.strokeWidthBorderWidth);
            canvas.drawCircle(cx, cy, this.strokeWidthCircle - (this.strokeWidthBorderWidth / 2), this.circlePaint);
        }
    }


    private void setTouchPosition(int x, int y) {

        // GEHT - aber die 4 ist statisch
        // this.positionInfo = String.format(Locale.getDefault(), "%04d, %04d", x, y);

        // GEHT - mit führender Null
//        String fmt = "%0" + this.viewDigitsOfWidth + "d, %0" + this.viewDigitsOfHeight + "d";
//        Log.v("PeLo", "fmt ==> " + fmt);
//        this.positionInfo = String.format(Locale.getDefault(), fmt, x, y);

        // GEHT - ohne führender Null
//        String fmt = "%" + this.viewDigitsOfWidth + "d, %" + this.viewDigitsOfHeight + "d";
//        Log.v("PeLo", "fmt ==> " + fmt);
//        this.positionInfo = String.format(Locale.getDefault(), fmt, x, y);

        // GEHT - am einfachsten !!!!!!
        this.positionInfo = String.format(Locale.getDefault(), "%d, %d", x, y);
    }

    private void drawPositionInfo(Canvas canvas) {

        // calculate coordinates of info text
//        if (this.positionBounds == null) {
//
//            this.positionBounds = new Rect();
//            String test = String.format(Locale.getDefault(), "%04d, %04d", 8888, 8888);
//            this.textPaint.getTextBounds(test, 0, test.length(), this.positionBounds);
//
//            this.xPosInfo = (int) (this.getWidth() - this.positionBounds.width() - this.strokeWidthInfoPadding);
//            this.yPosInfo = (int) (this.positionBounds.height() + this.strokeWidthInfoPadding);
//        }

        if (this.positionBounds == null) {

            this.positionBounds = new Rect();

            // build dummy string: maximum number of digits, plus one comma, plus one space
//            String fmt = "%" + this.viewDigitsOfWidth + "d,%" + this.viewDigitsOfHeight + "d";
//            Log.v("PeLo", "fmt ==> " + fmt);
            String fmt = "%d, %d";
            Log.v("PeLo", "fmt ==> " + fmt);

            // want numbers consisting of '8s
            int dummy1 = 0;
            for (int i = 0; i < this.viewDigitsOfWidth; i ++)
                dummy1 = 10 * dummy1 + 8;

            int dummy2 = 0;
            for (int i = 0; i < this.viewDigitsOfHeight; i ++)
                dummy2 = 10 * dummy2 + 8;

            Log.v("PeLo", "dummy1 ==> " + dummy1);
            Log.v("PeLo", "dummy2 ==> " + dummy2);

//            String test = String.format(Locale.getDefault(), "%04d, %04d", 8888, 8888);
//            this.textPaint.getTextBounds(test, 0, test.length(), this.positionBounds);

            String test = String.format(Locale.getDefault(), fmt, dummy1, dummy2);
            this.textPaint.getTextBounds(test, 0, test.length(), this.positionBounds);
            Log.v("PeLo", "test ==> " + test);

            this.xPosInfo = (int) (this.getWidth() - this.positionBounds.width() - this.strokeWidthInfoPadding);
            this.yPosInfo = (int) (this.positionBounds.height() + this.strokeWidthInfoPadding);
        }

        canvas.drawText(this.positionInfo, this.xPosInfo, this.yPosInfo, this.textPaint);
    }

    // drawing helper methods for lines
    private void drawConstructionLine(Canvas canvas, BezierPoint p0, BezierPoint p1) {
        this.drawLine(canvas, p0, p1, ColorConstructionLine, this.strokeWidthConstructionLine);
    }

    private void drawCurveLine(Canvas canvas, BezierPoint p0, BezierPoint p1) {
        this.drawLine(canvas, p0, p1, this.ColorCurveLine, this.strokeWidthCurveLine);
    }

    private void drawLineBetweenControlPoints(Canvas canvas, BezierPoint p0, BezierPoint p1) {
        this.drawLine(canvas, p0, p1, ColorLineBetweenControlPoints, this.strokeWidthLineBetweenControlPoints);
    }

    private void drawLine(Canvas canvas, BezierPoint p0, BezierPoint p1, int color, float strokeWidth) {
        this.linePaint.setColor(color);
        this.linePaint.setStrokeWidth(strokeWidth);
        canvas.drawLine(p0.getX(), p0.getY(), p1.getX(), p1.getY(), this.linePaint);
    }


 private  float convertDpToPixel (int dpSize) {
        Resources res = this.getResources();
        DisplayMetrics dm = res.getDisplayMetrics() ;
        float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, dm);

        // String test = String.format(Locale.getDefault(), "  converted %d Dp to %d pixel ###", dpSize, (int) strokeWidth);
        // Log.v("PeLo", test);

        return strokeWidth;
    }

    private float convertDpToPixel_Variant2 (int dpSize) {
        Log.v("PeLo", "convertDpToPixel");
        Resources res = this.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        // convert the dps to pixels, based on density scale
        float strokeWidth = dpSize * dm.density + 0.5f;

        String test = String.format(Locale.getDefault(), "  converted %d Dp to %d pixel #####", dpSize, (int) strokeWidth);
        Log.v("PeLo", test);

        return strokeWidth;
    }

    private int digitsOfNumber(int number) {

        int result = 0;

        while (number != 0) {
            number = number / 10;
            result ++;
        }

        return result;
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
}
