package de.peterloos.beziersplines.activities;

import de.peterloos.beziersplines.utils.BezierPoint;

/**
 * Created by Peter on 28.10.2016.
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
