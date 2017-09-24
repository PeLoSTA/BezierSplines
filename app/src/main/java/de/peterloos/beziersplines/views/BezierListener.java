package de.peterloos.beziersplines.views;

import android.util.Size;

import de.peterloos.beziersplines.utils.BezierMode;

/**
 * Project: Bézier Splines Simulation
 * Copyright (c) 2017 by PeLo on 02.02.2017. All rights reserved.
 * Contact info: peterloos@gmx.de
 */

public interface BezierListener {
    void setInfo(String info);
    void setSize(int width, int height);
    void changeMode(BezierMode mode);
}
