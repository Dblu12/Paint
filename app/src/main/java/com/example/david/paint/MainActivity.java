package com.example.david.paint;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {

    private Paint p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        p= (Paint) findViewById(R.id.view);

    }

    public void setRed(View v){
        p.setColor(Color.RED);
    }

    public void setBlack(View v){
        p.setColor(Color.BLACK);
    }

    public void setBlue(View v){
        p.setColor(Color.BLUE);
    }

    public void setGreen(View v){
        p.setColor(Color.GREEN);
    }

    public void setOther(View v){
        AmbilWarnaDialog a= new AmbilWarnaDialog(this, p.getColorPincel(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                p.setColor(color);
            }
        });
        a.show();
    }

    public void eraseAll(View v){
        p.eraseAll();
    }

    public void drawStraight(View v){
        p.setTipo(2);
    }

    public void drawLine(View v){
        p.setTipo(0);
    }

    public void drawCircle(View v){
        p.setTipo(1);
    }

    public void save(View v){
        Bitmap bmp=p.getBitmap();
        Date a= new GregorianCalendar().getTime();
        File toSave= new File(Environment.getExternalStorageDirectory().getPath()+"/paint");

        toSave.mkdirs(); //create folders where write files
        final File file = new File(toSave, "img"+a.getTime()+".png");
        FileOutputStream outputStream= null;
        try {

            outputStream= new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            Toast.makeText(this,"Guardado", Toast.LENGTH_SHORT).show();
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(this,"Fallo al guardar", Toast.LENGTH_SHORT).show();
            Log.v("xxxError; ", e.toString());
            Log.v("xxxError; ", Environment.getExternalStorageDirectory().getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        Log.v("xxx", "de");

        if (p.undoSize() > 0) {
            p.deshacer();
        }else{
            Toast.makeText(this, "No puede deshacer", Toast.LENGTH_SHORT).show();
        }

    }
}
