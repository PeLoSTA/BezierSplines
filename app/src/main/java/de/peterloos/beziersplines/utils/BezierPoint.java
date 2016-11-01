package de.peterloos.beziersplines.utils;

import java.util.Locale;

public class BezierPoint {

    private float x;
    private float y;

    public BezierPoint(float x, float y) {
        this.setX(x);
        this.setY(y);
    }

    public float getX() {
        return x;
    }

    private void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    private void setY(float y) {
        this.y = y;
    }

    // linear interpolation of two points
    public static BezierPoint Interpolate(BezierPoint p0, BezierPoint p1, float t) {
        float x = t * p1.x + (1 - t) * p0.x;
        float y = t * p1.y + (1 - t) * p0.y;
        return new BezierPoint(x, y);
    }

    // distance between two points
    public static float Distance(BezierPoint p0, BezierPoint p1) {
        float dx = p1.x - p0.x;
        float dy = p1.y - p0.y;
        float sum = dx * dx + dy * dy;
        return (float) Math.sqrt(sum);
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "(%.3f,%.3f)", this.x, this.y);
    }
}
