package com.bupt.customview

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import android.widget.Scroller
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import com.bupt.customview.util.getBitmapByWidth
import com.bupt.customview.util.px

class GestureImageView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val IMG_SIZE = 300.px

    private var isZoomIn = false
    private var currentScale = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var startScale = 0f
    private var endScale = 0f
    private lateinit var zoomAnim: ObjectAnimator
    private var hasZoomed = false
    private var gestureDetector = GestureDetectorCompat(context, GestureDetectorListener())
    private var scaleGestureDetector = ScaleGestureDetector(context, ScaleGestureDetectorListener())
    private var scroller = OverScroller(context)
    private var imgOriginOffsetX = 0f
    private var imgOriginOffsetY = 0f
    private var imgExtraOffsetX = 0f;
    private var imgExtraOffsetY = 0f
    private val bitmap = getBitmapByWidth(R.drawable.avatar, IMG_SIZE.toInt())
    private val painter = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imgOriginOffsetX = (width - IMG_SIZE) / 2
        imgOriginOffsetY = (height - IMG_SIZE) / 2
        if (bitmap.width / bitmap.height.toFloat() > width / height.toFloat()) {
            startScale = width / bitmap.width.toFloat()
            endScale = height / bitmap.height.toFloat()
        } else {
            startScale = height / bitmap.height.toFloat()
            endScale = width / bitmap.width.toFloat()
        }
        currentScale = startScale
        zoomAnim = ObjectAnimator.ofFloat(this, "currentScale", startScale, endScale)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var fraction = (currentScale -  startScale) / (endScale - startScale)
        canvas.translate(imgExtraOffsetX * fraction,imgExtraOffsetY * fraction)
        canvas.scale(currentScale, currentScale, width / 2f, height / 2f)
        canvas.drawBitmap(bitmap, imgOriginOffsetX, imgOriginOffsetY, painter)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var result = scaleGestureDetector.onTouchEvent(event)
        if (!scaleGestureDetector.isInProgress) {
            return gestureDetector.onTouchEvent(event)
        }
        return result
    }


    inner class ScaleGestureDetectorListener: ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            imgExtraOffsetX = (detector.focusX - width / 2f) * (1 - endScale / startScale)
            imgExtraOffsetY = (detector.focusY - height / 2f) * (1 - endScale / startScale)
            imgExtraOffsetX = imgExtraOffsetX.coerceAtLeast(-((bitmap.width * endScale - width) / 2f)).coerceAtMost((bitmap.width * endScale - width) / 2f)
            imgExtraOffsetY = imgExtraOffsetY.coerceAtLeast(-((bitmap.height * endScale - height) / 2f)).coerceAtMost((bitmap.height * endScale - height) / 2f)
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var tempScale = currentScale * detector.scaleFactor
            isZoomIn = detector.scaleFactor > 1f
            if (tempScale in startScale..endScale) {
                currentScale = tempScale
                return true
            }
            if (tempScale < startScale){
                hasZoomed = false
                currentScale = startScale
            } else {
                hasZoomed = true
                currentScale = endScale
            }
            return false
        }
    }

    inner class GestureDetectorListener: GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            imgExtraOffsetX -= distanceX
            imgExtraOffsetY -= distanceY
            imgExtraOffsetX = imgExtraOffsetX.coerceAtLeast(-((bitmap.width * endScale - width) / 2f)).coerceAtMost((bitmap.width * endScale - width) / 2f)
            imgExtraOffsetY = imgExtraOffsetY.coerceAtLeast(-((bitmap.height * endScale - height) / 2f)).coerceAtMost((bitmap.height * endScale - height) / 2f)
            invalidate()
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            var minX = -(bitmap.width * endScale - width) / 2f
            var maxX = (bitmap.width * endScale - width) / 2f
            var minY = -(bitmap.height * endScale - height) / 2f
            var maxY = (bitmap.height * endScale - height) / 2f
            scroller.fling(imgExtraOffsetX.toInt(), imgExtraOffsetY.toInt(), velocityX.toInt(), velocityY.toInt(), minX.toInt(), maxX.toInt(), minY.toInt(), maxY.toInt())
            ViewCompat.postOnAnimation(this@GestureImageView) {
                refresh()
            }
            return true
        }

        private fun refresh() {
            scroller.computeScrollOffset()
            imgExtraOffsetX = scroller.currX.toFloat()
            imgExtraOffsetY = scroller.currY.toFloat()
            invalidate()
            if (!scroller.isFinished) {
                ViewCompat.postOnAnimation(this@GestureImageView) {
                    refresh()
                }
            }
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            var copyAnim = zoomAnim.clone()
            if (currentScale != endScale && currentScale != startScale) {
                hasZoomed = false
                copyAnim.setFloatValues(currentScale, endScale)
                imgExtraOffsetX = (e.x - width / 2f) * (1 - (endScale / startScale))
                imgExtraOffsetY = (e.y - height / 2f) * (1 - (endScale / startScale))
                imgExtraOffsetX = imgExtraOffsetX.coerceAtLeast(-((bitmap.width * endScale - width) / 2f)).coerceAtMost((bitmap.width * endScale - width) / 2f)
                imgExtraOffsetY = imgExtraOffsetY.coerceAtLeast(-((bitmap.height * endScale - height) / 2f)).coerceAtMost((bitmap.height * endScale - height) / 2f)
                copyAnim.start()
            }else if (!hasZoomed) {
                imgExtraOffsetX = (e.x - width / 2f) * (1 - (endScale / startScale))
                imgExtraOffsetY = (e.y - height / 2f) * (1 - (endScale / startScale))

                imgExtraOffsetX = imgExtraOffsetX.coerceAtLeast(-((bitmap.width * endScale - width) / 2f)).coerceAtMost((bitmap.width * endScale - width) / 2f)
                imgExtraOffsetY = imgExtraOffsetY.coerceAtLeast(-((bitmap.height * endScale - height) / 2f)).coerceAtMost((bitmap.height * endScale - height) / 2f)
                zoomAnim.start()
            } else {
                zoomAnim.reverse()
            }
            hasZoomed = !hasZoomed
            return super.onDoubleTap(e)
        }
    }
}