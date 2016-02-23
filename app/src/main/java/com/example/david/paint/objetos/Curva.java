package com.example.david.paint.objetos;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by David on 17/02/2016.
 */
public class Curva {

    private Path path;
    private Paint pincel;

    public Curva(Path path, Paint pincel) {
        this.path = path;
        this.pincel = pincel;
    }

    public Path getPath() {
        return path;
    }

    public Paint getPincel() {
        return pincel;
    }
}
