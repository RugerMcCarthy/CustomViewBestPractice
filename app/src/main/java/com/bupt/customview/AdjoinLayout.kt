package com.bupt.customview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.math.max
import kotlin.math.min

class AdjoinLayout(context: Context, attributeSet: AttributeSet): ViewGroup(context, attributeSet) {
    var isVertical = false
    init {
        resources.obtainAttributes(attributeSet, R.styleable.AdjoinLayout).run {
           isVertical = getInteger(R.styleable.AdjoinLayout_orientation, 0) == 0
        }
    }

    private val childLayoutList by lazy {
        mutableListOf<Rect>()
    }


    private fun getMeasuredSpec(fatherMeasureSpec: Int, used: Int, specificValue: Int): Int {
        var fatherMeasureSpecMode = MeasureSpec.getMode(fatherMeasureSpec)
        var fatherMeasureSpecSize = MeasureSpec.getSize(fatherMeasureSpec)
        var childMeasuredMode = 0
        var childMeasuredSize = 0
        when (fatherMeasureSpecMode) {
            MeasureSpec.EXACTLY, MeasureSpec.AT_MOST ->
                when (specificValue) {
                    LayoutParams.MATCH_PARENT -> {
                        childMeasuredMode = MeasureSpec.EXACTLY
                        childMeasuredSize = fatherMeasureSpecSize
                    }
                    LayoutParams.WRAP_CONTENT -> {
                        childMeasuredMode = MeasureSpec.AT_MOST
                        childMeasuredSize = fatherMeasureSpecSize - used
                    }
                    else -> {
                        childMeasuredMode = MeasureSpec.EXACTLY
                        childMeasuredSize = specificValue
                    }
                }
            else ->
                when (specificValue) {
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT -> {
                        childMeasuredMode = MeasureSpec.UNSPECIFIED
                        childMeasuredSize = 0
                    }
                    else -> {
                        childMeasuredMode = MeasureSpec.EXACTLY
                        childMeasuredSize = specificValue
                    }
                }
        }
        return MeasureSpec.makeMeasureSpec(childMeasuredSize, childMeasuredMode)
    }

    private fun measureChildWithUsed(view: View, widthMeasureSpec: Int, widthUsed: Int, heightMeasureSpec: Int, heightUsed: Int) {
        var childMeasuredWidthSpec = getMeasuredSpec(widthMeasureSpec, widthUsed, view.layoutParams.width)
        var childMeasuredHeightSpec = getMeasuredSpec(heightMeasureSpec, heightUsed, view.layoutParams.height)
        view.measure(childMeasuredWidthSpec, childMeasuredHeightSpec)
    }

    private fun verticalMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var usedWidth = 0
        var usedHeight = 0
        for ((index, child) in children.withIndex()) {
            measureChildWithUsed(child, widthMeasureSpec, 0, heightMeasureSpec, usedHeight)
            if (index >= childLayoutList.size) {
                childLayoutList.add(Rect())
            }
            var currentChildLayout = childLayoutList[index]
            currentChildLayout.set(0, usedHeight, child.measuredWidth, usedHeight + child.measuredHeight)
            usedWidth = max(usedWidth, child.measuredWidth)
            usedHeight += child.measuredHeight
        }
        setMeasuredDimension(resolveSize(usedWidth, widthMeasureSpec), resolveSize(usedHeight, heightMeasureSpec))
    }

    private fun horizontalMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var usedWidth = 0
        var usedHeight = 0
        var lineHeightMax = 0
        var lineWidthMax = 0
        var widthMeasureSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthMeasureSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        for ((index, child) in children.withIndex()) {
            while (true) {
                measureChildWithUsed(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
                if (widthMeasureSpecMode == MeasureSpec.UNSPECIFIED || (child.measuredWidth != 0 && usedWidth + child.measuredWidth <= widthMeasureSpecSize)) {
                    break
                } else {
                    usedHeight += lineHeightMax
                    usedWidth = 0
                    lineHeightMax = 0
                }
            }
            if (index >= childLayoutList.size) {
                childLayoutList.add(Rect())
            }
            var currentChildLayout = childLayoutList[index]
            currentChildLayout.set(usedWidth, usedHeight, usedWidth + child.measuredWidth, usedHeight + child.measuredHeight)
            lineHeightMax = max(child.measuredHeight, lineHeightMax)
            usedWidth += child.measuredWidth
        }
        setMeasuredDimension(resolveSize(usedWidth, widthMeasureSpec), resolveSize(usedHeight + lineHeightMax, heightMeasureSpec))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isVertical) {
            verticalMeasure(widthMeasureSpec, heightMeasureSpec)
        } else {
            horizontalMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for ((index, child) in children.withIndex()) {
            if (index >= childLayoutList.size) {
                break
            }
            val childLayout = childLayoutList[index]
            child.layout(childLayout.left, childLayout.top, childLayout.right, childLayout.bottom)
        }
    }
}