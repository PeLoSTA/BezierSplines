package de.peterloos.beziersplines.activities;

import de.peterloos.beziersplines.utils.BezierPoint;

/**
 * Project: Bézier Splines Simulation
 * Copyright (c) 2017 by PeLo on 23.01.2017. All rights reserved.
 * Contact info: peterloos@gmx.de
 */

public class UpdateDescriptor {

    private boolean addPoint;
    private boolean changeT;
    private BezierPoint p;
    private float t;

    public UpdateDescriptor(BezierPoint p, float t, boolean addPoint, boolean changeT) {
        this.p = p;
        this.t = t;
        this.addPoint = addPoint;
        this.changeT = changeT;
    }

    public BezierPoint getP() {
        return this.p;
    }

    public float getT() {
        return this.t;
    }

    public boolean isAddPoint() {
        return this.addPoint;
    }

    public boolean isChangeT() {
        return this.changeT;
    }
}
