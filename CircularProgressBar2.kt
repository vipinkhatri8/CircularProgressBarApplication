package com.example.circularprogressbarapplication


    import android.content.Context
    import android.graphics.Canvas
    import android.graphics.Color
    import android.graphics.Paint
    import android.util.AttributeSet
    import android.view.View

    class CircularProgressBar2(context: Context, attrs: AttributeSet?) : View(context, attrs) {

        private val backgroundColor = Color.BLACK
        private val outerRingColor = Color.WHITE
        private val innerRingColor = Color.WHITE
        private val progressColor = Color.GREEN

        private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = backgroundColor
        }

        private val outerRingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = outerRingColor
            style = Paint.Style.STROKE
            strokeWidth = dpToPx(20f) // Width of the outer ring
        }

        private val innerRingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = innerRingColor
            style = Paint.Style.STROKE
            strokeWidth = dpToPx(5f) // Width of the inner ring
        }

        private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = progressColor
            style = Paint.Style.STROKE
            strokeWidth = dpToPx(10f) // Width of the progress indicator
        }

        private var progress = 0.5f // Initial progress value (0 to 1)

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            val centerX = width / 2f
            val centerY = height / 2f
            val radius = width / 2f - dpToPx(20f) // Adjust padding

            // Draw background circle
            canvas.drawCircle(centerX, centerY, radius, backgroundPaint)

            // Draw outer white ring
            canvas.drawCircle(centerX, centerY, radius, outerRingPaint)

            // Draw inner white ring
            val innerRingRadius = radius - dpToPx(55f) // Adjust distance from outer ring
            canvas.drawCircle(centerX, centerY, innerRingRadius, innerRingPaint)

            // Draw progress indicator
            val startAngle = -90f // Start angle at 12 o'clock position
            val sweepAngle = 360f * progress // Sweep angle based on progress
            canvas.drawArc(
                centerX - innerRingRadius,
                centerY - innerRingRadius,
                centerX + innerRingRadius,
                centerY + innerRingRadius,
                startAngle,
                sweepAngle,
                false,
                progressPaint
            )
        }

        fun setProgress(progress: Int) {
            this.progress = progress.toFloat()
            invalidate()
        }


        private fun dpToPx(dp: Float): Float {
            val density = resources.displayMetrics.density
            return dp * density
        }
    }





