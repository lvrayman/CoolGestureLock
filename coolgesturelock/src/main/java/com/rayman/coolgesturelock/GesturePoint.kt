package com.rayman.coolgesturelock

/**
 * @author 吕少锐 (lvshaorui@parkingwang.com)
 * @version 2019/3/1
 */
class GesturePoint(val number: Int) {
    var isChosen = false
    var x = 0f
    var y = 0f
    var originalRadius = 5f
    var chosenRadius = 10f

    fun setRadius(radius: Float) {
        originalRadius = radius
        chosenRadius = radius * 3
    }
}