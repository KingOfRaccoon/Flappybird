package com.example.flappybirdisreal;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Pipe extends BaseObject {
    private Bitmap bm;
    private static int speed;
    private boolean isPassed;
    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        Pipe.speed= speed;
    }

    @Override
    public Bitmap getBm() {
        return bm;
    }

    public Pipe(float x, float y, int width, int height) {
        super(x, y, width, height);
        speed = 5 * Constants.SCREEN_WIDTH / 1080; // Set speed here
    }

    public void draw(Canvas canvas) {
        this.x -= speed; // Move pipe to the left by speed
        canvas.drawBitmap(this.bm, this.x, this.y, null);
    }
    public void setPassed(boolean passed) {
        this.isPassed = passed;
    } public boolean isPassed() {
        return isPassed;
    }

    // Đặt lại vị trí ống và trạng thái đã vượt qua khi ra khỏi màn hình
    public void resetPipe() {
        setX(Constants.SCREEN_WIDTH);
        setPassed(false);  // Đặt lại trạng thái đã vượt qua
    }

    public void ramdomY() {
        Random r = new Random();
        this.y = -r.nextInt(this.height / 2); // Randomly generate a Y position
    }

    @Override
    public void setBm(Bitmap bm) {
        this.bm = Bitmap.createScaledBitmap(bm, width, height, true);
    }
}
