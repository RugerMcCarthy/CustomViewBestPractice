package com.bupt.customview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.drawable.Drawable
import com.bupt.customview.util.px

class TestDrawable: Drawable() {
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var REDIUS = 50.px
    override fun draw(canvas: Canvas) {
        // 获取可绘制区域的宽高，得到可绘制最大圆的半径
        val width: Int = bounds.width()
        val height: Int = bounds.height()
        val radius: Float = Math.min(width, height).toFloat() / 2f
        paint.color = Color.RED
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return android.graphics.PixelFormat.TRANSLUCENT
    }
}