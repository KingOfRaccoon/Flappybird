package com.example.flappybirdisreal

import android.graphics.Bitmap
import android.graphics.Canvas
import java.util.Random

// Класс Pipe представляет трубу в игре, которая движется слева направо.
class Pipe(x: Float, y: Float, width: Int, height: Int) : BaseObject(x, y, width, height) {
    private var isPassed = false // Флаг, указывающий, пролетела ли птица через эту трубу

    // Возвращает текущий Bitmap для трубы
    override fun getBm(): Bitmap {
        return _bm!!
    }

    init {
        // Установка скорости движения трубы в зависимости от ширины экрана
        speed = 5 * Constants.SCREEN_WIDTH / 1080
    }

    // Отрисовка трубы на холсте
    fun draw(canvas: Canvas) {
        this._x -= speed.toFloat() // Смещение трубы влево на значение скорости
        canvas.drawBitmap(this._bm!!, this._x, this._y, null)
    }

    // Установка статуса "пройденности" трубы
    fun setPassed(passed: Boolean) {
        this.isPassed = passed
    }

    // Возвращает статус "пройденности" трубы
    fun isPassed(): Boolean {
        return isPassed
    }

    // Сброс трубы в начальное положение (за пределы экрана справа)
    fun resetPipe() {
        setX(Constants.SCREEN_WIDTH.toFloat()) // Установка X-координаты за экран
        setPassed(false) // Сброс флага "пройденности"
    }

    // Установка случайной высоты трубы
    fun ramdomY() {
        val r = Random()
        this._y = -r.nextInt(this._height / 2).toFloat() // Генерация случайной высоты в пределах половины высоты экрана
    }

    // Установка Bitmap с масштабированием до размеров трубы
    override fun setBm(bm: Bitmap?) {
        this._bm = Bitmap.createScaledBitmap(bm!!, _width, _height, true)
    }

    companion object {
        private var speed: Int = 0 // Скорость движения труб
    }
}