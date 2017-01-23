package de.peterloos.beziersplines.activities;

/**
 * Project: Bézier Splines Simulation
 * Copyright (c) 2017 by PeLo on 23.01.2017. All rights reserved.
 * Contact info: peterloos@gmx.de
 */

public class BezierGlobals {

    // language settings
    public static final String LanguageGerman = "German";
    public static final String LanguageEnglish = "English";
    public static final String DefaultLanguage = LanguageEnglish;

    // scale factor for stroke widths
    public static final int DefaultScaleFactor = 2;
    public static final float[] ScaleFactors = new float[] { 0.5F, 0.75F, 1.0F, 1.25F, 1.5F };

    // density-independent pixels for lines
    public static final float StrokeWidthControlPointsDp = 4F;
    public static final float StrokeWidthCurveLineDp = 5F;
    public static final float StrokeWidthConstructionLinesDp = 3F;

    // density-independent pixels for circles
    public static final int StrokeWidthCircleRadiusDp = 9;
    public static final int StrokeWidthBorderWidthDp = 2;
    public static final int DistanceFromNumberDp = 12;
    public static final int NearestDistanceMaximumDp = 16;

    // density-independent pixels for text
    public static final int StrokeWidthTextSizeDp = 18;
    public static final int StrokeWidthInfoPaddingDp = 8;
}
