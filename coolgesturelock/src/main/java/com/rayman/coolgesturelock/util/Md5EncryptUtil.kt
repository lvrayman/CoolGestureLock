package com.rayman.coolgesturelock.util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @author 吕少锐 (lvshaorui@parkingwang.com)
 * @version 2019/3/1
 */
class Md5EncryptUtil : IEncryptUtil {
    override fun encrypt(result: String): String {
        return encryptWithMD5(result)
    }

    private fun encryptWithMD5(string: String): String {
        try {
            val instance: MessageDigest = MessageDigest.getInstance("MD5")
            val digest: ByteArray = instance.digest(string.toByteArray())
            val sb = StringBuffer()
            for (b in digest) {
                val i: Int = b.toInt() and 0xff
                var hexString = Integer.toHexString(i)
                if (hexString.length < 2) {
                    hexString = "0$hexString"
                }
                sb.append(hexString)
            }
            return sb.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }
}