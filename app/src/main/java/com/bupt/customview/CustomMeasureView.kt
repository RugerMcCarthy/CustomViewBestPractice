package com.bupt.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.bupt.customview.util.px
import kotlin.reflect.KProperty

class CustomMeasureView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {


    val PADDING = 100.px
    val REDIUS = 50.px
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(PADDING + REDIUS, PADDING + REDIUS, REDIUS, paint)
    }

}