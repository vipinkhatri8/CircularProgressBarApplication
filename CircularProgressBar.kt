package com.example.circularprogressbarapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View

class CircularProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    val startColor = Color.parseColor("#B9FA47") // B9FA47 Light green color
    val centerColor = Color.parseColor("#D6EE2E") //  D6EE2E Yellow color
    val midColor = Color.parseColor("#FC8200") // FC8200 ; orange color
    val endColor = Color.parseColor("#ea2423") //  ea2423 Red color


    private var progressPaint: Paint = Paint()
    private var whiteBorderPaint: Paint = Paint()
    private var backgroundPaint: Paint = Paint()
    private var breakPaint: Paint = Paint()
    private var progress = 0f
    private var maxProgress = 100f
    private var strokeWidth = 30f
    private var breakWidth = 10f
    private var breakLength = 20f
    private var progressGradient : LinearGradient? = null


    var x0 = 300F
    var y0 = 120F
    var x1 = 20F
    var y1 = 60F

    private var backgroundColor = Color.parseColor("#000000")
    private var breakColor = Color.parseColor("#000000")

    init {

        progressGradient = LinearGradient(
            this@CircularProgressBar.x0,
            this@CircularProgressBar.y0,
            this@CircularProgressBar.x1,
            this@CircularProgressBar.y1,
            intArrayOf(startColor, centerColor, midColor,endColor),
            floatArrayOf(0f, 0.5f, 0.75f,1f), // Adjusted the position of the center color to 0.35f
            Shader.TileMode.CLAMP // Use CLAMP to avoid repeating the gradient
        )

        progressPaint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = this@CircularProgressBar.strokeWidth
            strokeCap = Paint.Cap.BUTT // Flat edges
        }

        progressPaint.shader = progressGradient


        backgroundPaint.apply {
            color = backgroundColor
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = this@CircularProgressBar.strokeWidth
        }

        breakPaint.apply {
            color = breakColor
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = this@CircularProgressBar.breakWidth
        }

        whiteBorderPaint = Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth =
                this@CircularProgressBar.strokeWidth - 25f // Set the width of the white border
        }

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val diameter = Math.min(width, height) - strokeWidth
        val padding = 10f // Convert dp to pixels

        val rectF = RectF(
            strokeWidth / 2 + padding * 2,
            strokeWidth / 2 + padding * 2,
            diameter + strokeWidth / 2 - padding * 2,
            diameter + strokeWidth / 2 - padding * 2
        )
        val totalAngle = 360 * progress / maxProgress

        // Draw the background circle with black fill
        val backgroundCircle = RectF(
            strokeWidth / 2,
            strokeWidth / 2,
            diameter + strokeWidth / 2,
            diameter + strokeWidth / 2
        )
        canvas.drawOval(backgroundCircle, backgroundPaint)

        // Draw the white ring at the end point
        val endRingRectF = RectF(
            (width - strokeWidth * 1.5f),
            (height - strokeWidth * 1.5f),
            (width - strokeWidth / 2 - padding * 2),
            (height - strokeWidth / 2 - padding * 2)
        )
        canvas.drawOval(endRingRectF, whiteBorderPaint)

        // Draw the progress arc
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
        breakLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, displayMetrics)

        x0 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, displayMetrics)
        y0 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40f, displayMetrics)
        x1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, displayMetrics)
        y1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, displayMetrics)


    }

    override fun setBackgroundColor(color: Int) {
        backgroundPaint.color = color
        invalidate()
    }
}