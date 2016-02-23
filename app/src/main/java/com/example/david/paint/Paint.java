package com.example.david.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.david.paint.objetos.Circulo;
import com.example.david.paint.objetos.Curva;
import com.example.david.paint.objetos.Recta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by David on 17/02/2016.
 */
public class Paint extends View {

    private float radio;
    private int ancho;
    private int alto;
    private float x0, xi, y0, yi;
    private android.graphics.Paint pincel;
    private int tipo=0; // 0 -> Linea, 1 -> Cuadrado, 2 -> Circulo 3 -> Recta
    private Bitmap mapaDeBits, backup;
    private List<Object> ultimosMovimientos= new ArrayList<>();
    private Canvas lienzoFondo = new Canvas();
    private Path path = new Path();
    private boolean puede=true;
    private int cont=10;
    private boolean restore=false;

    public Paint(Context context, AttributeSet attrs) {
        super(context, attrs);
        pincel = new android.graphics.Paint();
        pincel.setColor(Color.BLACK);
        pincel.setAntiAlias(true);
        pincel.setStrokeWidth(10);
        pincel.setStyle(android.graphics.Paint.Style.STROKE);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(!restore) {
            super.onDraw(canvas);

            //Log.v("xxxcont", "ondra");
            if (cont == 10) {
                backup = mapaDeBits.copy(mapaDeBits.getConfig(), mapaDeBits.isMutable());
                ultimosMovimientos.clear();
                cont = 0;
            }

            canvas.drawBitmap(mapaDeBits, 0, 0, null);



            if (puede) {
                if (tipo == 1) {
                    canvas.drawCircle(x0, xi, radio, pincel);

                } else if (tipo == 2) {
                    canvas.drawLine(x0, y0, xi, yi, pincel);

                }
                else{
                    canvas.drawPath(path, pincel);
                }
            } else {
                puede = true;
            }


        }

    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        alto = h;
        ancho = w;
        mapaDeBits = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mapaDeBits.eraseColor(Color.WHITE);
        lienzoFondo = new Canvas(mapaDeBits);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (tipo==0) {
            return dibujar(event);
        //}else if (tipo==1){
            //return  rectangulo(event);
        }else if(tipo==1){
            return circulo(event);
        }else{
            return linea(event);
        }
    }


    public boolean dibujar(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x0 = x;
                y0 = y;
                xi = x;
                yi = y;
                path.moveTo(x0, y0);
                break;
            case MotionEvent.ACTION_MOVE:
                xi = x;
                yi = y;
                path.quadTo(x0, y0,(x + xi) / 2, (y + yi) / 2);
                x0 = xi;
                y0 = yi;
                invalidate();

                break;
            case MotionEvent.ACTION_UP:
                xi = x;
                yi = y;
                lienzoFondo.drawPath(path, pincel);
                path= new Path();
                Curva c= new Curva(path, pincel);

                cont++;

                anadirMov(c);
                Log.v("xxx", "Curva add");
                Log.v("xxx", ultimosMovimientos.toString());
                break;
        }
        return true;
    }

    public boolean linea(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x0 = x;
                y0 = y;
                xi = x;
                yi = y;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:

                xi = x;
                yi = y;


                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                xi = x;
                yi = y;

                lienzoFondo.drawLine(x0, y0, xi, yi, pincel);
                Recta r= new Recta(x0, y0, xi, yi, pincel);
                anadirMov(r);
                Log.v("xxx", "Recta add");
                cont++;
                Log.v("xxx", ultimosMovimientos.toString());
                invalidate();

                break;

        }
        return true;
    }

    public boolean rectangulo (MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x0 = x;
                y0 = y;
                xi = x;
                yi = y;
                break;
            case MotionEvent.ACTION_MOVE:
                xi = x;
                yi = y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                xi = x;
                yi = y;
                lienzoFondo.drawRect(x0, y0, xi, yi, pincel);

                invalidate();
                break;
        }
        return true;
    }

    public boolean circulo(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        radio = (float) Math.sqrt(Math.pow(x0 - xi, 2) + Math.pow(y0 - yi, 2));
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x0 = x;
                y0 = y;
                xi = x;
                yi = y;
                break;
            case MotionEvent.ACTION_MOVE:
                xi = x;
                yi = y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                xi = x;
                yi = y;

                lienzoFondo.drawCircle(x0, xi, radio, pincel);
                Circulo c= new Circulo(x0, xi, radio, pincel);
                anadirMov(c);
                Log.v("xxx", "Circulo add");
                Log.v("xxx", ultimosMovimientos.toString());
                cont++;
                invalidate();

                break;
        }
        return true;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setColor(int c){
        pincel.setColor(c);
    }

    public void eraseAll(){
        mapaDeBits = Bitmap.createBitmap(ancho, alto, Bitmap.Config.ARGB_8888);
        mapaDeBits.eraseColor(Color.WHITE);
        lienzoFondo = new Canvas(mapaDeBits);
        backup = mapaDeBits.copy(mapaDeBits.getConfig(), mapaDeBits.isMutable());
        ultimosMovimientos.clear();
        cont = 0;
        puede=false;
        invalidate();
    }

    public Bitmap getBitmap(){
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);
        return bmp;
    }

    private void anadirMov(Object o){
        if(ultimosMovimientos.size()<=10){
            ultimosMovimientos.add(o);
            Log.v("xxx", "b");
            invalidate();
        }else{
            ultimosMovimientos.remove(0);
            ultimosMovimientos.add(o);
            Log.v("xxx", "c");
            invalidate();
        }
    }

    public void deshacer(){
        Log.v("xxx", (ultimosMovimientos.size()-1)+"antes");
        ultimosMovimientos.remove(ultimosMovimientos.size()-1);

        Log.v("xxx", ultimosMovimientos.toString());
        Log.v("xxx", (ultimosMovimientos.size() - 1) + "");
        invalidate();
        restore=true;
        //mapaDeBits=backup.copy(mapaDeBits.getConfig(), mapaDeBits.isMutable());
        mapaDeBits = Bitmap.createBitmap(ancho, alto, Bitmap.Config.ARGB_8888);
        mapaDeBits.eraseColor(Color.WHITE);
        lienzoFondo = new Canvas(mapaDeBits);


        cont--;
        if(restore){
            for(Object o: ultimosMovimientos){

                Log.v("xxx", "a");
                if(o.getClass().toString().contains("Circulo")){
                    Log.v("xxx", "Circulo");
                    Circulo c= (Circulo) o;
                    lienzoFondo.drawCircle(c.getX0(), c.getXi(), c.getRadio(), c.getPincel());
                    invalidate();

                }else if(o.getClass().toString().contains("Recta")){
                    Log.v("xxx", "Recta");
                    Recta r= (Recta) o;
                    lienzoFondo.drawLine(r.getX0(), r.getY0(), r.getXi(), r.getYi(), r.getPincel());
                    invalidate();

                }else if(o.getClass().toString().contains("Curva")){
                    Log.v("xxx", "Curva");
                    Curva c= (Curva) o;
                    //Log.v("xxx", c.getPath().toString());
                    lienzoFondo.drawPath(c.getPath(), pincel);
                    invalidate();


                }




            }

            restore=false;
        }

        invalidate();


    }

    public int undoSize(){
        return ultimosMovimientos.size();
    }

    public int getColorPincel(){
        return pincel.getColor();
    }
}
