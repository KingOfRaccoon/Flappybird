package com.example.flappybirdisreal;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import java.util.ArrayList;

public class Bird extends BaseObject {
    public ArrayList<Bitmap> arrBms = new ArrayList<>();
    private int cout, vFlap, idCurrentBitmap;
    private float drop;

    public Bird() {
        this.cout = 0;
        this.vFlap = 5;
        this.idCurrentBitmap = 0;
        this.drop = 0;
    }

    public int getCout() {
        return cout;
    }

    public void setCout(int cout) {
        this.cout = cout;
    }

    public int getvFlap() {
        return vFlap;
    }

    public void setvFlap(int vFlap) {
        this.vFlap = vFlap;
    }

    public int getIdCurrentBitmap() {
        return idCurrentBitmap;
    }

    public void setIdCurrentBitmap(int idCurrentBitmap) {
        this.idCurrentBitmap = idCurrentBitmap;
    }

    public float getDrop() {
        return drop;
    }

    public void setDrop(float drop) {
        this.drop = drop;
    }

    public void draw(Canvas canvas) {
        drop();

        // Check the position of the bird
        if (this.x < 0 || this.x > Constants.SCREEN_WIDTH || this.y < 0 || this.y > Constants.SCREEN_HEIGHT) {
            Log.e("Bird", "Bird coordinates are out of screen bounds: x=" + this.x + ", y=" + this.y);
        }

        Bitmap bitmap = this.getBm();
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, this.x, this.y, null);
        } else {
            Log.e("Bird", "Bitmap is null");
        }
    }

    private void drop() {
        this.drop += 0.6;
        this.y += this.drop;
    }

    public ArrayList<Bitmap> getArrBms() {
        return arrBms;
    }

    public void setArrBms(ArrayList<Bitmap> arrBms) {
        this.arrBms = arrBms;
        for (int i = 0; i < arrBms.size(); i++) {
            this.arrBms.set(i, Bitmap.createScaledBitmap(this.arrBms.get(i), this.width, this.height, true));
        }
    }

    @Override
    public Bitmap getBm() {
        cout++;
        if (this.cout == this.vFlap) {
            for (int i = 0; i < arrBms.size(); i++) {
                if (i == arrBms.size() - 1) {
                    this.idCurrentBitmap = 0;
                    break;
                } else if (this.idCurrentBitmap == i) {
                    idCurrentBitmap = i + 1;
                    break;
                }
            }
            cout = 0;
        }

        if (this.drop < 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(-25);
            return Bitmap.createBitmap(arrBms.get(idCurrentBitmap), 0, 0, arrBms.get(idCurrentBitmap).getWidth(), arrBms.get(idCurrentBitmap).getHeight(), matrix, true);
        } else if (drop >= 0) {
            Matrix matrix = new Matrix();
            if (drop > 70) {
                matrix.postRotate(-25 + (drop * 2));
            } else {
                matrix.postRotate(45);
            }
            return Bitmap.createBitmap(arrBms.get(idCurrentBitmap), 0, 0, arrBms.get(idCurrentBitmap).getWidth(), arrBms.get(idCurrentBitmap).getHeight(), matrix, true);
        }

        return this.arrBms.get(idCurrentBitmap);
    }
}
