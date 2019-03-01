package com.rayman.coolgesturelock

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


/**
 * @author 吕少锐 (lvrayman@gmail.com)
 * @version 2019/3/1
 */
class GestureLock : View {
    private val gesturePointList = ArrayList<GesturePoint>()
    private val chosenPointList = ArrayList<GesturePoint>()

    private val originalPaint = Paint()
    private val chosenPaint = Paint()

    private var lastPoint: GesturePoint? = null

    private var vibrator: Vibrator? = null

    private var currentX = 0f
    private var currentY = 0f

    private var finalSize = 0

    var isGesturing = false
    var isDone = false

    var onFinishListener: OnGestureFinishListener? = null

    private var rowCount = 3

    fun setRowCount(rowCount: Int) {
        this.rowCount = rowCount
        initPoint()
        initPosition()
        invalidate()
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        attrs?.let {
            val obtainStyledAttributes = context.obtainStyledAttributes(it, R.styleable.GestureLock)
            rowCount = obtainStyledAttributes.getInteger(R.styleable.GestureLock_rowCount, rowCount)
            obtainStyledAttributes.recycle()
        }
        initPoint()
    }

    private fun initPoint() {
        originalPaint.color = Color.BLACK
        chosenPaint.color = Color.GRAY
        gesturePointList.clear()
        for (i in 0 until (rowCount * rowCount)) {
            gesturePointList.add(GesturePoint(i))
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        finalSize = minOf(widthSize, heightSize)

        initPosition()
    }

    private fun initPosition() {
        val perSize = (finalSize / (rowCount * 2)).toFloat()
        chosenPaint.strokeWidth = perSize / 10

        chosenPointList.forEach {
            it.setRadius(perSize / 10)
        }

        for (i in 0 until rowCount) {
            for (j in 0 until rowCount) {
                gesturePointList[(i * rowCount) + j].apply {
                    x = (((j + 1) * 2) - 1) * perSize
                    y = ((2 * i) + 1) * perSize
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //开启抗锯齿
        canvas?.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        //将所有已经选中的点连接起来
        chosenPointList.forEach {
            lastPoint?.let { lastPoint ->
                canvas?.drawLine(lastPoint.x, lastPoint.y, it.x, it.y, chosenPaint)
            }
            lastPoint = it
        }

        //画出最后一点和手指所在点的连线
        if (lastPoint != null && currentX != 0f && currentY != 0f) {
            canvas?.drawLine(lastPoint!!.x, lastPoint!!.y, currentX, currentY, chosenPaint)
        }

        gesturePointList.forEach {
            if (it.isChosen) {
                canvas?.drawCircle(it.x, it.y, it.chosenRadius, chosenPaint)
            }
            canvas?.drawCircle(it.x, it.y, it.originalRadius, originalPaint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (isDone) {
            clearGesture()
        }
        when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                isGesturing = true
                currentX = event.x
                currentY = event.y
                gesturePointList.forEach {
                    if (Math.abs(currentX - it.x) < 50 && Math.abs(currentY - it.y) < 50 && !it.isChosen) {
                        if (chosenPointList.isNotEmpty()) {
                            val lastPoint = chosenPointList.last()
                            val midX = (lastPoint.x + it.x) / 2
                            val midY = (lastPoint.y + it.y) / 2
                            chosenPointList.forEach { point ->
                                if (Math.abs(point.x - midX) < 5 && Math.abs(point.y - midY) < 5 && !point.isChosen) {
                                    chosenPointList.add(point)
                                    vibrate(1)
                                    point.isChosen = true
                                }
                            }
                        }
                        chosenPointList.add(it)
                        vibrate(1)
                        it.isChosen = true
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                isGesturing = false
                currentX = 0f
                currentY = 0f
                isDone = true
                if (chosenPointList.size > rowCount) {
                    var result = ""
                    chosenPointList.forEach {
                        result += it.number
                    }
                    chosenPaint.color = Color.GREEN
                    onFinishListener?.onFinish(result)
                } else {
                    vibrate(2)
                    chosenPaint.color = Color.RED
                    onFinishListener?.onError()
                }
            }
        }
        lastPoint = null
        this.invalidate()
        return true
    }

    fun clearGesture() {
        if (!isGesturing) {
            chosenPointList.clear()
            gesturePointList.forEach {
                it.isChosen = false
            }
            isDone = false
            originalPaint.color = Color.BLACK
            chosenPaint.color = Color.GRAY
        }
        invalidate()
    }

    private fun vibrate(times: Int) {
        for (i in 0 until times) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(100, 0))
            } else {
                vibrator?.vibrate(100)
            }
        }
    }

    interface OnGestureFinishListener {
        fun onFinish(result: String)
        fun onError()
    }

}
