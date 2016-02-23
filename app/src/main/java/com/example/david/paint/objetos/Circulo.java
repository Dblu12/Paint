package com.example.david.paint.objetos;

import android.graphics.Paint;

/**
 * Created by David on 17/02/2016.
 */
public class Circulo {

    private float x0, xi;
    private float radio;
    private Paint pincel;

    public Circulo(float x0, float xi, float radio, Paint pincel) {
        this.x0 = x0;
        this.xi = xi;
        this.radio = radio;
        this.pincel = pincel;
    }

    public float getX0() {
        return x0;
    }

    public float getXi() {
        return xi;
    }

    public float getRadio() {
        return radio;
    }

    public Paint getPincel() {
        return pincel;
    }
}
