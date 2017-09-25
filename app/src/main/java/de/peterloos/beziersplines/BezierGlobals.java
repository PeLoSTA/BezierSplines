package de.peterloos.beziersplines;

/**
 * Project: Bézier Splines Simulation
 * Copyright (c) 2017 by PeLo on 23.01.2017. All rights reserved.
 * Contact info: peterloos@gmx.de
 */

public class BezierGlobals {

    public static final String VersionReadable = "Redesign including new snap lines algorithm - test";

    public static final String TAG = "PeLo";

    // language settings
    public static final String LanguageGerman = "German";
    public static final String LanguageEnglish = "English";
    public static final String DefaultLanguage = LanguageEnglish;

    // scale factor for stroke widths
    public static final int DefaultStrokewidthFactor = 2;
    public static final float[] StrokewidthFactors = new float[]{0.5F, 0.75F, 1.0F, 1.25F, 1.5F};

    // gridlines factor
    public static final int DefaultGridlineFactor = 1;   // 0 (low), 1 (normal) or 2 (high)
    public static final int GridlinesDensityLow = 20;
    public static final int GridlinesDensityNormal = 30;
    public static final int GridlinesDensityHigh = 40;

    // density-independent pixels for lines
    public static final float StrokeWidthControlPointsDp = 3.5F;
    public static final float StrokeWidthCurveLineDp = 4.5F;
    public static final float StrokeWidthConstructionLinesDp = 2.5F;

    // density-independent pixels for circles
    public static final int StrokeWidthCircleRadiusDp = 9;
    public static final int StrokeWidthBorderWidthDp = 2;
    public static final int DistanceFromNumberDp = 12;
    public static final int NearestDistanceMaximumDp = 16;

    // density-independent pixels for text
    public static final int StrokeWidthTextSizeDp = 18;
}
