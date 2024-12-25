package com.example.flappybirdisreal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class GameView extends View {
    private Bird bird;
    private Handler handler;
    private Runnable r;
    private ArrayList<Pipe> arrsPipes;
    private int sumpipe, distance;
    private int score, bestscore;
    public boolean start;
    private SoundPool soundPool;
    private int jumpSound;
    private boolean soundLoaded;
    private int gameOverSound;
    private int passPipeSound;
    private boolean isSoundEnabled = true;
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bestscore = 0;
        score = 0;
        start = false;
        initBird();
        initPipe();
        handler = new Handler();
        r = this::postInvalidate;

          AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();


        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            if (status == 0) { // 0 means the sound was successfully loaded
                soundLoaded = true;
            }
        });
        jumpSound = soundPool.load(context, R.raw.jumpsound, 1);
        gameOverSound = soundPool.load(context, R.raw.gameover_sound, 1);
        passPipeSound = soundPool.load(context, R.raw.pass_pipe_sound, 1); // Load pass pipe sound
    }

    private void initPipe() {
        sumpipe = 6;
        distance = 400 * Constants.SCREEN_HEIGHT / 1920;
        arrsPipes = new ArrayList<>();
        for (int i = 0; i < sumpipe; i++) {
            float xPosition = Constants.SCREEN_WIDTH + i * ((float) (Constants.SCREEN_WIDTH + 200 * Constants.SCREEN_WIDTH / 1000) / ((float) sumpipe / 2));
            if (i < sumpipe / 2) {
                Pipe pipe = new Pipe(xPosition, 0, 200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT / 2);
                pipe.setBm(BitmapFactory.decodeResource(getResources(), R.drawable.pipedown));
                pipe.ramdomY();
                this.arrsPipes.add(pipe);
            }

            else {
                Pipe otherPipe = arrsPipes.get(i - sumpipe / 2);
                Pipe pipe = new Pipe(otherPipe.getX(), otherPipe.getY() + otherPipe.getHeight() + distance,
                        200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT / 2);
                pipe.setBm(BitmapFactory.decodeResource(getResources(), R.drawable.pipeup));
                this.arrsPipes.add(pipe);
            }
        }
    }

    private void initBird() {
        bird = new Bird();
        bird.setWidth(100 * Constants.SCREEN_WIDTH / 1080);
        bird.setHeight(100 * Constants.SCREEN_HEIGHT / 1920);
        bird.setX((float) (100 * Constants.SCREEN_WIDTH) / 1080);
        bird.setY((float) Constants.SCREEN_HEIGHT / 2 - (float) bird.getHeight() / 2);

        ArrayList<Bitmap> arrBms = new ArrayList<>();
        arrBms.add(BitmapFactory.decodeResource(getResources(), R.drawable.bstar));

        arrBms.add(BitmapFactory.decodeResource(getResources(), R.drawable.btap));
        arrBms.add(BitmapFactory.decodeResource(getResources(), R.drawable.btap2));
        bird.setArrBms(arrBms);
    }

    private boolean checkCollision() {

        float birdLeft = bird.getX();
        float birdRight = bird.getX() + bird.getWidth();
        float birdTop = bird.getY();
        float birdBottom = bird.getY() + bird.getHeight();

        for (Pipe pipe : arrsPipes) {

            float pipeLeft = pipe.getX();
            float pipeRight = pipe.getX() + pipe.getWidth();
            float pipeTop = pipe.getY();
            float pipeBottom = pipe.getY() + pipe.getHeight();

            if (birdRight > pipeLeft && birdLeft < pipeRight &&
                    birdBottom > pipeTop && birdTop < pipeBottom) {
                return true;
            }
        }

        // Kiểm tra nếu chim rơi ra khỏi màn hình
        if (birdBottom < 0 || birdTop > Constants.SCREEN_HEIGHT) {
            return true;
        }

        return false;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (start) {
            // Check collision
            if (checkCollision()) {
                gameOver();
                return;
            }

            // Draw pipes
            for (int i = 0; i < sumpipe; i++) {
                Pipe currentPipe = this.arrsPipes.get(i);

                // Handle pipe movement and reset
                currentPipe.setX(currentPipe.getX() - 5);
                if (currentPipe.getX() < -currentPipe.getWidth()) {
                    currentPipe.resetPipe();
                    if (i < sumpipe / 2) {
                        currentPipe.ramdomY();
                    } else {
                        currentPipe.setY(arrsPipes.get(i - sumpipe / 2).getY() +
                                arrsPipes.get(i - sumpipe / 2).getHeight() + distance);
                    }
                    currentPipe.setPassed(false);
                }


                if (i < sumpipe / 2 && !currentPipe.isPassed() &&
                        this.bird.getX() > currentPipe.getX() + currentPipe.getWidth()) {
                    score++;
                    MainActivity.txt_score.setText("" + score);
                    currentPipe.setPassed(true);


                    if (isSoundEnabled && soundLoaded) {
                        playSound(passPipeSound);
                    }
                }

                currentPipe.draw(canvas);
            }


            if (bird != null) {
                bird.draw(canvas);
            }
        } else {

            if (bird.getY() > Constants.SCREEN_HEIGHT / 2) {
                bird.setDrop(-15 * Constants.SCREEN_HEIGHT / 1920);
            }
        }


        handler.postDelayed(r, 10);
    }


    private void gameOver() {
        handler.removeCallbacks(r);
        if (isSoundEnabled && soundLoaded) {
            playSound(gameOverSound);
        }
        bestscore = Math.max(bestscore, score);
        MainActivity.txt_ScoreOver.setText(MainActivity.txt_score.getText());
        MainActivity.txt_BestScore.setText("Best: " + MainActivity.txt_BestScore.getText());
        MainActivity.txt_score.setVisibility(INVISIBLE);
        MainActivity.relativeLayout.setVisibility(VISIBLE);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            bird.setDrop(-15); // Simulate bird jump on touch
            if (isSoundEnabled && soundLoaded) {
                playSound(jumpSound);
            }

            if (!start) {
                start = true;
            }
        }
        return true;
    }

    public boolean isSoundEnabled() {
        return isSoundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        isSoundEnabled = soundEnabled;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
    private void playSound(int soundId) {
        if (isSoundEnabled && soundLoaded) {
            soundPool.play(soundId, 1, 1, 0, 0, 1);
        }
    }
    public void reset() {
        MainActivity.txt_score.setText("0");
        score = 0;
        start = false;
        initBird();
        initPipe();

        invalidate();
    }
}
