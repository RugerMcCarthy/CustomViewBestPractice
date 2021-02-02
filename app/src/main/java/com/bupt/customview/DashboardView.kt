package com.bupt.customview

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.bupt.customview.util.px
import kotlin.math.cos
import kotlin.math.sin

class DashboardView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {
    val REDIUS = 100f.px
    val OPEN_ANGLE = 0f
    val DASH_WIDTH = 4f.px
    val DASH_LENGTH = 10f.px
    val POINTER_LENGTH = 70f.px
    val CLOCK_SCALE = 12
    var currentAngle = 90 + OPEN_ANGLE / 2

    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var path = Path()
    lateinit var effect: PathDashPathEffect

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.addArc(width / 2f - REDIUS, height / 2f - REDIUS, width / 2f + REDIUS, height / 2f + REDIUS, 90 + OPEN_ANGLE / 2, 360 - OPEN_ANGLE)
        PathMeasure(path, false).apply {
            var dash = Path().apply{
                addRect(0f, 0f, DASH_WIDTH, DASH_LENGTH, Path.Direction.CW)
            }
            effect = PathDashPathEffect(dash, (length - DASH_WIDTH) / CLOCK_SCALE, 0f, PathDashPathEffect.Style.ROTATE)
        }
    }

    override fun onDraw(canvas: Canvas) {
        paint.strokeWidth = (5f).px
        paint.style = Paint.Style.STROKE
        canvas.drawPath(path, paint)
        paint.pathEffect = effect
        canvas.drawPath(path, paint)
        paint.pathEffect = null
        paint.style = Paint.Style.FILL
        canvas.drawCircle(width / 2f, height / 2f, 5f.px, paint)

        canvas.drawLine(width / 2f, height / 2f, width / 2f + POINTER_LENGTH * cos(Math.toRadians(currentAngle.toDouble())).toFloat(), height / 2f + POINTER_LENGTH * sin(Math.toRadians(currentAngle.toDouble())).toFloat(), paint)
//        Log.d("gzz", "angle: ${currentAngle}");
        currentAngle += 1f
    }
}