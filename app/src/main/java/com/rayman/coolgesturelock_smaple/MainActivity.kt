package com.rayman.coolgesturelock_smaple

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.rayman.coolgesturelock.GestureLock
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val gestureLock by lazy { gesture_lock }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gestureLock.onFinishListener = object : GestureLock.OnGestureFinishListener {
            override fun onFinish(result: String) {
            }

            override fun onError() {
            }

        }
        gestureLock.rowCount = 4
    }
}
