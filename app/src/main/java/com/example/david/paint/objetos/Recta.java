package com.example.david.paint.objetos;

import android.graphics.Paint;

/**
 * Created by David on 17/02/2016.
 */
public class Recta {

    private float x0, xi, y0, yi;
    private Paint pincel;

    public Recta(float x0, float y0, float xi, float yi, Paint pincel) {
        this.x0 = x0;
        this.xi = xi;
        this.y0 = y0;
        this.yi = yi;
        this.pincel = pincel;
    }

    public float getX0() {
        return x0;
    }

    public float getXi() {
        return xi;
    }

    public float getY0() {
        return y0;
    }

    public float getYi() {
        return yi;
    }

    public Paint getPincel() {
        return pincel;
    }
}
