package com.bupt.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.bupt.customview.util.px

class DrawableView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var drawable = TestDrawable();
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888)
        var bitmapConvas = Canvas(bitmap)
        bitmapConvas.drawColor(Color.BLACK)
        drawable.setBounds(0, 0, 200, 200)
        drawable.draw(bitmapConvas)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }
}