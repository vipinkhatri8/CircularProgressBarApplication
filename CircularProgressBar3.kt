package com.example.circularprogressbarapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View

class CircularProgressBar3(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val progressColorFirst = Color.parseColor("#99C817")
    private val progressColorSecond = Color.parseColor("#FDEB48")
    private val progressColorThird = Color.parseColor("#FED137")
    private val progressColorForth = Color.parseColor("#F7B11E")
    private val progressColorFifth = Color.parseColor("#D72626")


    private var progressPaint: Paint = Paint()
    private var whiteBorderPaint: Paint = Paint()
    private var whiteBorderPaint1: Paint = Paint()
    private var backgroundPaint: Paint = Paint()
    private var backgroundPaint1: Paint = Paint()
    private var breakPaint: Paint = Paint()
    private var progress = 0f
    private var maxProgress = 100f
    private var strokeWidth = 30f
    private var breakWidth = 10f
    private var breakLength = 20f

    private var backgroundColor = Color.parseColor("#000000")
    private var breakColor = Color.parseColor("#000000")




    init {


        progressPaint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = this@CircularProgressBar3.strokeWidth
            strokeCap = Paint.Cap.BUTT // Flat edges
        }


        backgroundPaint.apply {
            color = backgroundColor
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = this@CircularProgressBar3.strokeWidth
        }

        backgroundPaint1.apply {
            color = backgroundColor
            isAntiAlias = false
            style = Paint.Style.FILL
            strokeWidth = this@CircularProgressBar3.strokeWidth
        }

        breakPaint.apply {
            color = breakColor
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = this@CircularProgressBar3.breakWidth
        }

        whiteBorderPaint1 = Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = this@CircularProgressBar3.strokeWidth - 5f // Set the width of the white border
        }


        whiteBorderPaint = Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = this@CircularProgressBar3.strokeWidth - 25f // Set the width of the white border
        }

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val diameter = Math.min(width, height) - strokeWidth
        val padding = 10f // Convert dp to pixels

        val centerX = width / 2f
        val centerY = height / 2f

        val progressGradient = SweepGradient(
            centerX, centerY,
            intArrayOf(
                progressColorFirst,
                progressColorSecond,
                progressColorThird,
                progressColorForth,
                progressColorFifth
            ),
            floatArrayOf(0f, 0.2f, 0.4f, 0.6f, 1f)
        )

        val matrix = Matrix()
        matrix.setRotate(-90f, centerX, centerY)
        progressGradient.setLocalMatrix(matrix)

        progressPaint.shader = progressGradient

        val rectF = RectF(
            strokeWidth / 2 + padding * 2,
            strokeWidth / 2 + padding * 2,
            diameter + strokeWidth / 2 - padding * 2,
            diameter + strokeWidth / 2 - padding * 2
        )
        val totalAngle = 360 * progress / maxProgress

        // Draw the background circle with white border
        val outerRectF = RectF(
            strokeWidth / 2 + padding * 3.8f,
            strokeWidth / 2 + padding * 3.8f,
            diameter + strokeWidth / 2 - padding * 3.8f,
            diameter + strokeWidth / 2 - padding * 3.8f
        )
        canvas.drawOval(outerRectF, whiteBorderPaint)
        canvas.drawOval(outerRectF, whiteBorderPaint1)

        // Draw the inner circle with white border
        val innerRectF = RectF(
            strokeWidth / 2,
            strokeWidth / 2,
            diameter + strokeWidth / 2,
            diameter + strokeWidth / 2
        )

        val innerRectF1 = RectF(
            strokeWidth / 2,
            strokeWidth / 2,
            diameter + strokeWidth / 2,
            diameter + strokeWidth / 2
        )

        canvas.drawOval(innerRectF, whiteBorderPaint)
        canvas.drawOval(innerRectF1, whiteBorderPaint1)

        // Draw the background circle
        canvas.drawOval(rectF, backgroundPaint)
        canvas.drawOval(rectF, backgroundPaint1)

        val segmentAngle =
            breakLength / (Math.PI * diameter / 180)
        val breakAngle =
            (breakWidth / (Math.PI * diameter / 180))
        var startAngle = -90f


        progressPaint.shader = progressGradient

        try {
            while (startAngle < totalAngle - 90) {
                canvas.drawArc(rectF, startAngle, segmentAngle.toFloat(), false, progressPaint)
                startAngle += segmentAngle.toFloat() + breakAngle.toFloat()
            }
        } catch (e: Exception) {
            Log.e("Progress Bar error", e.message.toString())
        }

        // If there's a remaining portion that's smaller than a full segment, draw it
        if (startAngle < totalAngle - 90 + segmentAngle) {
            canvas.drawArc(
                rectF,
                startAngle,
                (totalAngle - 90 + segmentAngle - startAngle).toFloat(),
                false,
                progressPaint
            )
        }
    }

    fun setProgress(progress: Int) {
        this.progress = progress.toFloat()
        invalidate()
    }

    fun setMax(progress: Float) {
        this.maxProgress = progress
        invalidate() // Force a view to draw again
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateDimensions()
    }

    private fun calculateDimensions() {
        val displayMetrics = resources.displayMetrics
        strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7f, displayMetrics)
        breakWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, displayMetrics)
        breakLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40f, displayMetrics)


    }

    override fun setBackgroundColor(color: Int) {
        backgroundPaint.color = color
        backgroundPaint1.color = color

        invalidate()
    }
}