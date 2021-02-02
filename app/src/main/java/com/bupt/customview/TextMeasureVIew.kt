package com.bupt.customview

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.bupt.customview.util.getBitmapByWidth
import com.bupt.customview.util.px

class TextMeasureVIew(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    val xferMode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    val REDIUS = 100.px
    val IMG_WIDTH = 400
    val IMG_OFFSET_X = 200.px
    val IMG_OFFSET_Y = 100.px

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val fontMetric = Paint.FontMetrics()
    val text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed condimentum porttitor volutpat. Maecenas velit massa, accumsan a maximus laoreet, iaculis dictum diam. Cras tincidunt porta semper. Pellentesque in facilisis erat. Proin erat erat, placerat a porttitor id, porttitor sit amet lorem. Integer egestas felis dapibus ex imperdiet placerat. Integer vitae efficitur ante, ac sollicitudin quam. Ut semper mi quis libero dapibus sagittis. Morbi sapien leo, maximus et facilisis et, vulputate id mi. Nulla in dapibus felis. Nullam blandit augue facilisis est pharetra, in euismod quam elementum."
    val bounds = Rect()
    val measured = floatArrayOf(0f)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {

//        var innerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
//        var layer = canvas.saveLayer(0f, 0f, IMG_WIDTH.toFloat(), IMG_WIDTH.toFloat(), innerPaint)
//        canvas.drawCircle(IMG_WIDTH / 2f, IMG_WIDTH / 2f, 200f, paint)
//        paint.xfermode = xferMode
//        canvas.drawBitmap(getBitmap(IMG_WIDTH, R.drawable.avatar), 0f, 0f, paint)
//        paint.xfermode = null
//        canvas.restoreToCount(layer)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10.px
        canvas.drawBitmap(getBitmapByWidth(R.drawable.avatar, IMG_WIDTH), IMG_OFFSET_X, IMG_OFFSET_Y, paint)
        paint.style = Paint.Style.FILL
        paint.textSize = 20.px
        paint.getFontMetrics(fontMetric)
        var baseCount = 0
        var drawHeight = -fontMetric.top
        while (baseCount < text.length) {
            var maxWidth = 0f
            var isSplit = false
            var splitWidth = 0f
            if (drawHeight + fontMetric.bottom < IMG_OFFSET_Y
                    || drawHeight > IMG_OFFSET_Y + IMG_WIDTH) {
                maxWidth = width.toFloat()
            } else {
                maxWidth = IMG_OFFSET_X
                if (width - IMG_OFFSET_X - IMG_WIDTH > 10) {
                    isSplit = true
                    splitWidth = width - IMG_OFFSET_X - IMG_WIDTH
                }
            }
            var count = paint.breakText(text, baseCount, text.length, true, maxWidth, measured)
            canvas.drawText(text, baseCount, baseCount + count, 0f, drawHeight, paint)
            baseCount += count
            if (isSplit) {
                count = paint.breakText(text, baseCount, text.length, true, splitWidth, measured)
                canvas.drawText(text, baseCount, baseCount + count, IMG_OFFSET_X + IMG_WIDTH, drawHeight, paint)
                baseCount += count
            }
            drawHeight += paint.fontSpacing
        }
    }


}