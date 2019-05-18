package com.rayman.coolgesturelock.util

/**
 * @author 吕少锐 (lvrayman@gmail.com)
 * @version 2019-05-18
 */
class DefaultPointPlainUtil : IPointPlainUtil {
    override fun getPointPlainText(position: Int): String {
        return position.toString()
    }
}