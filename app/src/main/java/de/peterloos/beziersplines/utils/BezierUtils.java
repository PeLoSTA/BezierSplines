package de.peterloos.beziersplines.utils;

// import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BezierUtils {

    private enum Direction {ToTheRight, ToTheBottom, ToTheLeft, ToTheTop}

    // just for concentric rectangles
    private static float currentX;
    private static float currentY;
    private static float nextX;
    private static float nextY;

    // schaut langweilig aus
    public static List<BezierPoint> getTotallyRandom(float width, float height, int number) {

        Random rand = new Random();

        // create a border
        int border = 60;

        width -= border;
        height -= border;

        List<BezierPoint> result = new ArrayList<>();

        for (int i = 0; i < number; i ++) {

            float x = rand.nextFloat() * width + border / 2;
            float y = rand.nextFloat() * height + border / 2;

//            String s = String.format("%f,%f", x, y);
//            Log.v("PeLo", s);

            BezierPoint p = new BezierPoint(x, y);
            result.add(p);
        }

//        BezierPoint last = new BezierPoint(centerX + radius * (float) Math.sin(0.0), centerY + radius * (float) Math.cos(0.0));
//        result.add(last);

        return result;
    }



    public static List<BezierPoint> getDemoCircle(float centerX, float centerY, float radius, float arcLentgth) {

        List<BezierPoint> result = new ArrayList<>();

        for (double z = 2 * Math.PI; z >= 0.1; z -= arcLentgth) {

            float x = centerX + radius * (float) Math.sin(z);
            float y = centerY + radius * (float) Math.cos(z);

            BezierPoint p = new BezierPoint(x, y);
            result.add(p);
        }

//        BezierPoint last = new BezierPoint(centerX + radius * (float) Math.sin(0.0), centerY + radius * (float) Math.cos(0.0));
//        result.add(last);

        return result;
    }


    public static List<BezierPoint> getDemoConcentricCircles(float centerX, float centerY, float radius1, float radius2, float arcLentgth) {

        List<BezierPoint> result = new ArrayList<>();

        // inner circle
        for (double z = 2 * Math.PI; z >= 0.0; z = z - arcLentgth) {
            float x = centerX + radius1 * (float) Math.sin(z);
            float y = centerY + radius1 * (float) Math.cos(z);

            BezierPoint p = new BezierPoint(x, y);
            result.add(p);
        }

        for (double z = 0.0; z < 2 * Math.PI - 0.1; z = z + arcLentgth / 1.5) {
            float x = centerX + radius2 * (float) Math.sin(z);
            float y = centerY + radius2 * (float) Math.cos(z);

            BezierPoint p = new BezierPoint(x, y);
            result.add(p);
        }

        return result;
    }

    public static List<BezierPoint> getDemoCircle01(float centerX, float centerY, float radius, int partitions) {

        List<BezierPoint> result = new ArrayList<>();

        float x, y;
        BezierPoint p;

        BezierPoint center = new BezierPoint(centerX, centerY);

        double delta = 2 * Math.PI / partitions;
        double z = 0;

        for (int i = 0; i < partitions; i += 2) {
            result.add(center);

            x = centerX + radius * (float) Math.sin(z);
            y = centerY + radius * (float) Math.cos(z);
            p = new BezierPoint(x, y);
            result.add(p);

            z -= delta;

            x = centerX + radius * (float) Math.sin(z);
            y = centerY + radius * (float) Math.cos(z);
            p = new BezierPoint((int) x, (int) y);
            result.add(p);

            z -= delta;
        }

        // close figure with center point
        result.add(center);

        return result;
    }

    public static List<BezierPoint> getDemoCircle02(float centerX, float centerY, float radius, int partition) {

        List<BezierPoint> result = new ArrayList<>();

        BezierPoint center = new BezierPoint(centerX, centerY);

        float x, y;
        BezierPoint p;

        double delta = 2 * Math.PI / partition;
        double z = 0;

        for (int i = 0; i < partition; i++) {
            result.add(center);

            x = centerX + radius * (float) Math.sin(z);
            y = centerY + radius * (float) Math.cos(z);
            p = new BezierPoint(x, y);
            result.add(p);

            z -= delta;

            x = centerX + radius * (float) Math.sin(z);
            y = centerY + radius * (float) Math.cos(z);
            p = new BezierPoint((int) x, (int) y);
            result.add(p);
        }

        // close figure with center point
        result.add(center);

        return result;
    }

    public static List<BezierPoint> getDemoRectangle(float centerX, float centerY, float squareLength, int numEdges) {

        List<BezierPoint> result = new ArrayList<>();

        currentX = centerX;
        currentY = centerY;
        nextX = currentX;
        nextY = currentY;

        Direction direction = Direction.ToTheRight;

        for (int i = 1; i < numEdges; i++) {
            computeLine(result, i, direction, squareLength);
            direction = switchDirection(direction);
            computeLine(result, i, direction, squareLength);
            direction = switchDirection(direction);
        }

        return result;
    }

    private static void computeLine(List<BezierPoint> list, int count, Direction direction, float squareLength) {
        for (int j = 0; j < count; j++) {
            if (direction == Direction.ToTheRight) {
                nextX += squareLength;
            } else if (direction == Direction.ToTheBottom) {
                nextY += squareLength;
            } else if (direction == Direction.ToTheLeft) {
                nextX -= squareLength;
            } else if (direction == Direction.ToTheTop) {
                nextY -= squareLength;
            }

            // paint
//            String msg = String.format("%s   (%f,%f) ==> (%f,%f)",
//                    direction.toString(), currentX, currentY, nextX, nextY);
//            Log.v("PeLo", msg);

            BezierPoint point = new BezierPoint(currentX, currentY);
            list.add(point);

            // advance last point
            currentX = nextX;
            currentY = nextY;
        }
    }

    private static Direction switchDirection(Direction direction) {
        // switch direction
        if (direction == Direction.ToTheRight) {
            return Direction.ToTheBottom;
        } else if (direction == Direction.ToTheBottom) {
            return Direction.ToTheLeft;
        } else if (direction == Direction.ToTheLeft) {
            return Direction.ToTheTop;
        } else {
            return Direction.ToTheRight;
        }
    }
}
