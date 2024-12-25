package com.example.flappybirdisreal

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import java.util.ArrayList

// Класс Bird, представляющий птицу в игре
class Bird : BaseObject() {
    // Список битмапов (изображений), представляющих различные кадры анимации птицы.
    private var _bitmaps: ArrayList<Bitmap?> = ArrayList<Bitmap?>()

    // Счетчик кадров для управления анимацией.
    private var count: Int = 0

    // Количество кадров между сменой изображений (скорость махания крыльев).
    private var vFlap: Int = 5
    private var idCurrentBitmap: Int = 0

    // Параметр падения птицы (ускорение).
    private var drop: Float = 0f

    // Устанавливает значение ускорения падения.
    fun setDrop(drop: Float) {
        this.drop = drop
    }

    // Метод для рисования птицы на канве.
    fun draw(canvas: Canvas) {
        drop() // Вызываем обновление позиции птицы.

        val bitmap = this.getBm() // Получаем текущий битмап для отображения.
        if (bitmap != null)
            canvas.drawBitmap(bitmap, this._x, this._y, null) // Рисуем битмап на текущих координатах.
    }

    // Обновляет положение птицы на основе ускорения падения.
    private fun drop() {
        this.drop += 0.6.toFloat() // Увеличиваем ускорение падения.
        this._y += this.drop // Обновляем вертикальное положение птицы.
    }

    // Возвращает список битмапов.
    fun getBitmaps(): ArrayList<Bitmap?> {
        return _bitmaps
    }

    // Устанавливает массив битмапов и масштабирует их до размеров птицы.
    fun setArrBms(arrBms: ArrayList<Bitmap?>) {
        this._bitmaps = arrBms
        for (i in arrBms.indices) {
            _bitmaps[i] = Bitmap.createScaledBitmap(_bitmaps[i]!!, _width, _height, true)
        }
    }

    // Возвращает текущий битмап с учетом анимации и поворота в зависимости от ускорения падения.
    override fun getBm(): Bitmap? {
        count++ // Увеличиваем счетчик кадров.

        // Проверяем, пора ли сменить кадр анимации.
        if (this.count == this.vFlap) {
            for (i in getBitmaps().indices) {
                if (i == getBitmaps().size - 1) { // Если текущий кадр последний, возвращаемся к первому.
                    this.idCurrentBitmap = 0
                    break
                } else if (this.idCurrentBitmap == i) { // Переходим к следующему кадру.
                    idCurrentBitmap = i + 1
                    break
                }
            }
            count = 0
        }

        // Управляем поворотом птицы в зависимости от ускорения падения.
        if (this.drop < 0) {
            val matrix = Matrix()
            matrix.postRotate(-25f) // Поворот вверх.
            return Bitmap.createBitmap(
                getBitmaps()[idCurrentBitmap]!!,
                0,
                0,
                getBitmaps()[idCurrentBitmap]!!.width,
                getBitmaps()[idCurrentBitmap]!!.height,
                matrix,
                true
            )
        } else if (drop >= 0) {
            val matrix = Matrix()
            if (drop > 70) {
                matrix.postRotate(-25 + (drop * 2)) // Сильный поворот вниз при большом ускорении.
            } else {
                matrix.postRotate(45f) // Умеренный поворот вниз.
            }
            return Bitmap.createBitmap(
                getBitmaps()[idCurrentBitmap]!!,
                0,
                0,
                getBitmaps()[idCurrentBitmap]!!.width,
                getBitmaps()[idCurrentBitmap]!!.height,
                matrix,
                true
            )
        }

        return this.getBitmaps()[idCurrentBitmap]
    }
}