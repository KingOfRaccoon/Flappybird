package com.example.flappybirdisreal

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.flappybirdisreal.MainActivity.Companion.constraintLayout
import com.example.flappybirdisreal.MainActivity.Companion.txt_BestScore
import com.example.flappybirdisreal.MainActivity.Companion.txt_ScoreOver
import com.example.flappybirdisreal.MainActivity.Companion.txt_score
import java.util.ArrayList
import kotlin.math.max

class GameView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    // Объявление основных переменных для игры
    private var bird: Bird = Bird()
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val r: Runnable = Runnable { this.postInvalidate() }
    private var pipes: MutableList<Pipe> = mutableListOf<Pipe>()
    private var summaryPipes = 0
    private var distance = 0
    private var score: Int = 0
    private var bestScore: Int = 0
    private var _start: Boolean = false

    // Настройка звуковых эффектов игры
    private val soundPool: SoundPool =
        SoundPool.Builder().setMaxStreams(1).setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        ).build()
    private var soundLoaded = false
    private var isSoundEnabled = true
    private val jumpSound: Int by lazy { soundPool.load(context, R.raw.jumpsound, 1) }
    private val gameOverSound: Int by lazy { soundPool.load(context, R.raw.gameover_sound, 1) }
    private val passPipeSound: Int by lazy { soundPool.load(context, R.raw.pass_pipe_sound, 1) }

    init {
        // Инициализация птицы, труб и звуков
        initBird()
        initPipe()
        soundPool.setOnLoadCompleteListener(SoundPool.OnLoadCompleteListener { _: SoundPool?, _: Int, status: Int ->
            if (status == 0) // успешная загрузка звука
                soundLoaded = true
        })
    }

    // Метод для инициализации труб
    private fun initPipe() {
        summaryPipes = 6 // Количество труб
        distance = 400 * Constants.SCREEN_HEIGHT / 1920 // Расстояние между верхней и нижней трубой
        pipes.clear()
        for (i in 0 until summaryPipes) {
            val xPosition =
                Constants.SCREEN_WIDTH + i * ((Constants.SCREEN_WIDTH + 200 * Constants.SCREEN_WIDTH / 1000).toFloat() / (summaryPipes.toFloat() / 2))
            if (i < summaryPipes / 2) {
                // Создание верхней трубы
                val pipe = Pipe(
                    xPosition,
                    0f,
                    200 * Constants.SCREEN_WIDTH / 1080,
                    Constants.SCREEN_HEIGHT / 2
                )
                pipe.setBm(BitmapFactory.decodeResource(resources, R.drawable.pipedown))
                pipe.ramdomY()
                pipes.add(pipe)
            } else {
                // Создание нижней трубы
                val otherPipe = pipes[i - summaryPipes / 2]
                val pipe = Pipe(
                    otherPipe.getX(), otherPipe.getY() + otherPipe.getHeight() + distance,
                    200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT / 2
                )
                pipe.setBm(BitmapFactory.decodeResource(resources, R.drawable.pipeup))
                pipes.add(pipe)
            }
        }
    }

    // Метод для инициализации птицы
    private fun initBird() {
        bird = Bird()
        bird.setWidth(100 * Constants.SCREEN_WIDTH / 1080)
        bird.setHeight(100 * Constants.SCREEN_HEIGHT / 1920)
        bird.setX((100 * Constants.SCREEN_WIDTH).toFloat() / 1080)
        bird.setY(Constants.SCREEN_HEIGHT.toFloat() / 2 - bird.getHeight().toFloat() / 2)

        val arrBms = ArrayList<Bitmap?>()
        arrBms.add(BitmapFactory.decodeResource(resources, R.drawable.bstar))

        arrBms.add(BitmapFactory.decodeResource(resources, R.drawable.btap))
        arrBms.add(BitmapFactory.decodeResource(resources, R.drawable.btap2))
        bird.setArrBms(arrBms)
    }

    // Проверка столкновения птицы с трубами или краем экрана
    private fun checkCollision(): Boolean {
        val birdLeft = bird.getX()
        val birdRight = bird.getX() + bird.getWidth()
        val birdTop = bird.getY()
        val birdBottom = bird.getY() + bird.getHeight()

        pipes.forEach { pipe ->
            val pipeLeft = pipe.getX()
            val pipeRight = pipe.getX() + pipe.getWidth()
            val pipeTop = pipe.getY()
            val pipeBottom = pipe.getY() + pipe.getHeight()

            if (birdRight > pipeLeft && birdLeft < pipeRight && birdBottom > pipeTop && birdTop < pipeBottom) {
                return true
            }
        }

        return birdBottom < 0 || birdTop > Constants.SCREEN_HEIGHT
    }

    // Основной метод отрисовки
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (_start) {
            // Проверка на столкновение
            if (checkCollision()) {
                gameOver()
                return
            }

            // Отрисовка труб
            for (i in 0 until summaryPipes) {
                val currentPipe = this.pipes[i]

                // Движение труб и сброс при выходе за пределы экрана
                currentPipe.setX(currentPipe.getX() - 5)
                if (currentPipe.getX() < -currentPipe.getWidth()) {
                    currentPipe.resetPipe()
                    if (i < summaryPipes / 2) {
                        currentPipe.ramdomY()
                    } else {
                        currentPipe.setY(
                            pipes[i - summaryPipes / 2].getY() +
                                    pipes[i - summaryPipes / 2].getHeight() + distance
                        )
                    }
                    currentPipe.setPassed(false)
                }

                // Увеличение счёта при прохождении трубы
                if (i < summaryPipes / 2 && !currentPipe.isPassed() && this.bird.getX() > currentPipe.getX() + currentPipe.getWidth()) {
                    score++
                    txt_score?.text = "$score"
                    currentPipe.setPassed(true)

                    if (isSoundEnabled && soundLoaded) {
                        playSound(passPipeSound)
                    }
                }

                currentPipe.draw(canvas)
            }

            bird.draw(canvas)
        } else {
            // Начальное падение птицы до старта игры
            if (bird.getY() > Constants.SCREEN_HEIGHT / 2) {
                bird.setDrop((-15 * Constants.SCREEN_HEIGHT / 1920).toFloat())
            }
        }

        handler.postDelayed(r, 10) // Обновление экрана каждые 10 мс
    }

    // Обработка окончания игры
    private fun gameOver() {
        handler.removeCallbacks(r)
        if (isSoundEnabled && soundLoaded)
            playSound(gameOverSound)
        bestScore = max(bestScore, score)
        txt_ScoreOver?.text = txt_score?.text
        txt_BestScore?.text = "Best: $bestScore"
        txt_score?.visibility = INVISIBLE
        constraintLayout?.visibility = VISIBLE
    }

    // Обработка нажатий на экран
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            bird.setDrop(-15f) // Имитация прыжка птицы при нажатии
            if (isSoundEnabled && soundLoaded) {
                playSound(jumpSound)
            }

            if (!_start) {
                _start = true
            }
        }
        return true
    }

    // Включение или отключение звука
    fun setSoundEnabled(soundEnabled: Boolean) {
        isSoundEnabled = soundEnabled
    }

    // Установка состояния начала игры
    fun setStart(start: Boolean) {
        this._start = start
    }

    // Проигрывание звука
    private fun playSound(soundId: Int) {
        if (isSoundEnabled && soundLoaded) {
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
        }
    }

    // Сброс игры к начальному состоянию
    fun reset() {
        txt_score?.text = "0"
        score = 0
        _start = false
        initBird()
        initPipe()
        invalidate()
    }
}