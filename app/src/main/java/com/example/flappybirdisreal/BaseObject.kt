package com.example.flappybirdisreal

import android.graphics.Bitmap

// Базовый класс для игровых объектов, определяет основные свойства и методы для управления их положением и размерами
open class BaseObject {
    // Координата X объекта
    protected var _x: Float = 0f
    // Координата Y объекта
    protected var _y: Float = 0f
    // Ширина объекта
    protected var _width: Int = 0
    // Высота объекта
    protected var _height: Int = 0
    // Битмап (графическое представление) объекта
    protected open var _bm: Bitmap? = null

    // Конструктор без параметров, инициализирует объект значениями по умолчанию
    constructor()

    // Конструктор с параметрами для задания начальных значений координат, ширины и высоты
    constructor(x: Float, y: Float, width: Int, height: Int) {
        this._x = x
        this._y = y
        this._width = width
        this._height = height
    }

    // Получение текущей координаты X объекта
    fun getX(): Float {
        return _x
    }

    // Установка новой координаты X объекта
    fun setX(x: Float) {
        this._x = x
    }

    // Получение текущей координаты Y объекта
    fun getY(): Float {
        return _y
    }

    // Установка новой координаты Y объекта
    fun setY(y: Float) {
        this._y = y
    }

    // Получение текущей ширины объекта
    fun getWidth(): Int {
        return _width
    }

    // Установка новой ширины объекта
    fun setWidth(width: Int) {
        this._width = width
    }

    // Получение текущей высоты объекта
    fun getHeight(): Int {
        return _height
    }

    // Установка новой высоты объекта
    fun setHeight(height: Int) {
        this._height = height
    }

    // Получение текущего битмапа объекта (может быть переопределено в дочерних классах)
    open fun getBm(): Bitmap? {
        return _bm
    }

    // Установка нового битмапа объекта (может быть переопределено в дочерних классах)
    open fun setBm(bm: Bitmap?) {
        this._bm = bm
    }
}