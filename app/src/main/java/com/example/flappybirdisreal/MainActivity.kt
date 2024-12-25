package com.example.flappybirdisreal

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    private var isSoundOn = true // Sound is on by default
    private var btnToggleSound: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val displayMetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        Constants.SCREEN_HEIGHT = displayMetrics.heightPixels
        Constants.SCREEN_WIDTH = displayMetrics.widthPixels
        setContentView(R.layout.activity_main)
        txt_score = findViewById<TextView>(R.id.txt_score)
        btn_start = findViewById<Button>(R.id.btn_start)
        gameView = findViewById<GameView>(R.id.game_view)
        txt_ScoreOver = findViewById<TextView>(R.id.txt_ScoreOver)
        txt_BestScore = findViewById<TextView>(R.id.txt_BestScore)
        constraintLayout = findViewById<ConstraintLayout>(R.id.rl_Gameover)
        btnToggleSound = findViewById<ImageButton>(R.id.btn_toggle_sound)
        btn_start!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                // Reset the game state when starting
                gameView?.reset()

                // Start the game
                gameView?.setStart(true)

                // Hide the start button and the game over layout
                btn_start?.visibility = View.GONE
                constraintLayout?.visibility = View.GONE

                // Show the score text view
                txt_score!!.visibility = View.VISIBLE
            }
        })

        constraintLayout!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                // When clicking the game over screen, reset the game

                btn_start!!.visibility = View.VISIBLE

                // Hide the game over layout
                constraintLayout!!.visibility = View.GONE

                // Hide the score text view
                txt_score!!.visibility = View.GONE
            }
        })
        btnToggleSound!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                toggleSound()
            }
        })
    }

    private fun toggleSound() {
        isSoundOn = !isSoundOn
        btnToggleSound!!.setImageResource(if (isSoundOn) R.drawable.ic_volume_up else R.drawable.ic_volume_off)
        val gameView = findViewById<GameView>(R.id.game_view)
        gameView.setSoundEnabled(isSoundOn)
    }


    companion object {
        var txt_score: TextView? = null
        var txt_ScoreOver: TextView? = null
        var txt_BestScore: TextView? = null
        var btn_start: Button? = null
        var gameView: GameView? = null
        var constraintLayout: ConstraintLayout? = null
    }
}

