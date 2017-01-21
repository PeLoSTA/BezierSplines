package de.peterloos.beziersplines.activities;

/**
 * Created by Peter on 19.01.2017.
 */

public class BezierGlobals {

    // scale factor for stroke widths
    public static final int DefaultScaleFactor = 2;
    public static final float[] ScaleFactors = new float[] { 0.6F, 0.8F, 1.0F, 1.2F, 1.4F };

    // TODO Klären ob das Englisch / Deutsch sein sollte ....
    public static final CharSequence[] ScaleFactorDisplayNames = { "Extra Light", "Light", "Normal", "Bold", "Extra Bold" };

    // density-independent pixels for lines
    public static final float StrokeWidthControlPointsDp = 4F;
    public static final float StrokeWidthCurveLineDp = 5F;
    public static final float StrokeWidthConstructionLinesDp = 3F;
}
