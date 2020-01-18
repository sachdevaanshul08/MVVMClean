package com.clean.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Typeface
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.clean.R
import com.clean.utils.extensions.dpToPx
import com.google.android.gms.maps.model.LatLng

/**
 *
 *Circle view with text that blink along with text
 *
 * @param context
 */
class CustomHighlightView(context: Context) : View(context) {
    private var size: Float = 0.toFloat()
    private var scaleAnimation: Animation? = null
    private var strokeBackgroundPaint: Paint? = null
    private var text: String? = null
    private var textPaint: Paint? = null
    protected lateinit var point: Point
    protected lateinit var latLng: LatLng

    init {
        visibility = INVISIBLE
        setupSizes(context)
        setupScaleAnimation(context)
        setupStrokeBackgroundPaint(context)
        setupTextPaint(context)
    }

    constructor(context: Context, latLng: LatLng, point: Point, clickedPosition: String) : this(context) {
        text = clickedPosition
        this.point = point
        this.latLng = latLng
    }


    private fun setupSizes(context: Context) {
        size = context.dpToPx(SIZE) / 2
    }


    private fun setupScaleAnimation(context: Context) {
        scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.bounce)
    }

    private fun setupTextPaint(context: Context) {
        textPaint = Paint()
        textPaint!!.color = ContextCompat.getColor(context, R.color.colorWhite)
        textPaint!!.textAlign = Paint.Align.CENTER
        textPaint!!.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        textPaint!!.textSize = context.resources.getDimensionPixelSize(R.dimen.textsize_medium).toFloat()
    }

    private fun setupStrokeBackgroundPaint(context: Context) {
        strokeBackgroundPaint = Paint()
        strokeBackgroundPaint!!.color = ContextCompat.getColor(context, R.color.colorPink)
        strokeBackgroundPaint!!.style = Paint.Style.STROKE
        strokeBackgroundPaint!!.isAntiAlias = true
        strokeBackgroundPaint!!.strokeWidth = context.dpToPx(STROKE_DIMEN)
    }

    private fun drawText(canvas: Canvas) {
        if (text != null && !TextUtils.isEmpty(text))
            canvas.drawText(text!!, size, size - (textPaint!!.descent() + textPaint!!.ascent()) / 2, textPaint!!)
    }

    private fun drawStrokeBackground(canvas: Canvas) =
        canvas.drawCircle(size, size, context.dpToPx(40) / 2, strokeBackgroundPaint!!)


    private fun updateHightlightViewLayoutParams(point: Point) {
        this.point = point
        super.setLayoutParams(getCustomLayoutParams())
        invalidate()
    }

    private fun getCustomLayoutParams(): FrameLayout.LayoutParams {
        val params =
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.width = context.dpToPx(SIZE).toInt()
        params.height = context.dpToPx(SIZE).toInt()
        params.leftMargin = point.x - params.width / 2
        params.topMargin = point.y - params.height / 2
        return params
    }


    override fun setLayoutParams(params: ViewGroup.LayoutParams) {
        super.setLayoutParams(getCustomLayoutParams())
    }

    fun animateView() {
        startAnimation(scaleAnimation)
    }

    override fun onDraw(canvas: Canvas) {
        drawStrokeBackground(canvas)
        drawText(canvas)
        super.onDraw(canvas)
    }


    fun refresh(point: Point) {
        this.point = point
        updateHightlightViewLayoutParams(point)
    }

    fun show() {
        visibility = VISIBLE
        invalidate()
    }


    companion object {
        private val STROKE_DIMEN = 2
        private val SIZE = 44
    }
}