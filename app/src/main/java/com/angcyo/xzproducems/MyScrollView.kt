package com.angcyo.xzproducems

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.HorizontalScrollView

/**
 * Created by angcyo on 2017-08-04.
 */
class MyScrollView(context: Context, attributeSet: AttributeSet? = null) : HorizontalScrollView(context, attributeSet) {

    var scrollListener: OnScrollListener? = null

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        scrollListener?.onScroll(l, t)
    }

//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//        canvas.save()
//        try {
//            canvas.translate(scrollX.toFloat(), 0f)
//            (getChildAt(0) as ViewGroup).getChildAt(0).draw(canvas)
//        } catch(e: Exception) {
//        }
//        canvas.restore()
//    }

    interface OnScrollListener {
        fun onScroll(toX: Int, toY: Int)
    }
}
