package com.bupt.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.bupt.customview.util.getBitmapByWidth
import com.bupt.customview.util.px

class GeometricXferView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {
    val IMG_WIDTH = 600
    var IMG_PADING = 0f

    var moveMatrix = Matrix()
    var rotateAngle = 0f
    set(value) {
        field = value
        invalidate()
    }

    var flipAngle = 0f
    set(value) {
        field = value
        invalidate()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val camera = Camera()

    init {
        val typedArray = resources.obtainAttributes(attributeSet, R.styleable.GeometricXferView)
        flipAngle = typedArray.getFloat(R.styleable.GeometricXferView_startAngle, 0f)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        IMG_PADING = width / 2f - IMG_WIDTH / 2f
    }

    override fun onDraw(canvas: Canvas) {
        val avatar = getBitmapByWidth(R.drawable.avatar, IMG_WIDTH)
        // 上半部分
        canvas.save()
        canvas.translate((IMG_PADING + IMG_WIDTH / 2), (IMG_PADING + IMG_WIDTH / 2))
        canvas.rotate(-rotateAngle)
        canvas.clipRect(-IMG_WIDTH, -IMG_WIDTH, IMG_WIDTH, 0)
        canvas.rotate(rotateAngle)
        canvas.translate(-(IMG_PADING + IMG_WIDTH / 2), -(IMG_PADING + IMG_WIDTH / 2))
        canvas.drawBitmap(avatar, IMG_PADING, IMG_PADING, paint)
        canvas.restore()

        // 下半部分
        canvas.save()
        canvas.translate((IMG_PADING + IMG_WIDTH / 2), (IMG_PADING + IMG_WIDTH / 2))
        canvas.rotate(-rotateAngle)
        camera.save()
        camera.rotateX(flipAngle)
        camera.setLocation(0f , 0f, -6f * resources.displayMetrics.density)
        camera.applyToCanvas(canvas)
        camera.restore()
        canvas.clipRect(-IMG_WIDTH, 0, IMG_WIDTH, IMG_WIDTH)
        canvas.rotate(rotateAngle)
        canvas.translate(-(IMG_PADING + IMG_WIDTH / 2), -(IMG_PADING + IMG_WIDTH / 2))
        canvas.drawBitmap(avatar, IMG_PADING, IMG_PADING, paint)
        canvas.restore()

//        Log.d("gzz", "rotate: $rotateAngle")
//
//        paint.strokeWidth = 10.px
//        canvas.drawLine((IMG_PADING + IMG_WIDTH / 2), IMG_PADING, (IMG_PADING + IMG_WIDTH / 2), IMG_PADING + IMG_WIDTH, paint);
    }

}