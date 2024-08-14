package com.example.flappybirdisreal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    public static TextView txt_score, txt_ScoreOver, txt_BestScore;
    public static Button btn_start;
    public static GameView gameView;
    public static RelativeLayout relativeLayout;
    private boolean isSoundOn = true; // Sound is on by default
    private ImageButton btnToggleSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Constants.SCREEN_HEIGHT = displayMetrics.heightPixels;
        Constants.SCREEN_WIDTH = displayMetrics.widthPixels;
        setContentView(R.layout.activity_main);
        txt_score = findViewById(R.id.txt_score);
        btn_start = findViewById(R.id.btn_start);
        gameView = findViewById(R.id.game_view);
        txt_ScoreOver = findViewById(R.id.txt_ScoreOver);
        txt_BestScore = findViewById(R.id.txt_BestScore);
        relativeLayout = findViewById(R.id.rl_Gameover);
        btnToggleSound=findViewById(R.id.btn_toggle_sound);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reset the game state when starting
                gameView.reset();

                // Start the game
                gameView.setStart(true);

                // Hide the start button and the game over layout
                btn_start.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);

                // Show the score text view
                txt_score.setVisibility(View.VISIBLE);
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When clicking the game over screen, reset the game


                // Show the start button
                btn_start.setVisibility(View.VISIBLE);

                // Hide the game over layout
                relativeLayout.setVisibility(View.GONE);

                // Hide the score text view
                txt_score.setVisibility(View.GONE);
            }
        });
        btnToggleSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSound();
            }
        });
    }

    private void toggleSound() {
        isSoundOn = !isSoundOn;
        btnToggleSound.setImageResource(isSoundOn ? R.mipmap.soundo : R.mipmap.sound);

        // Pass the sound status to the GameView class
        GameView gameView = findViewById(R.id.game_view); // Make sure to use the correct ID
        gameView.setSoundEnabled(isSoundOn);
    }


}

