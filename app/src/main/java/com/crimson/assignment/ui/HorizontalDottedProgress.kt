package com.crimson.assignment.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.Transformation

class HorizontalDottedProgress : View {

    private val mDotRadius = 5

    private val mBounceDotRadius = 8


    private var mDotPosition = 0


    private val mDotAmount = 10

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint()


        paint.color = Color.parseColor("#ff9900")


        createDot(canvas, paint)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        startAnimation()
    }

    private fun createDot(
        canvas: Canvas,
        paint: Paint
    ) {


        for (i in 0 until mDotAmount) {
            if (i == mDotPosition) {
                canvas.drawCircle(
                    10 + (i * 20).toFloat(),
                    mBounceDotRadius.toFloat(),
                    mBounceDotRadius.toFloat(),
                    paint
                )
            } else {
                canvas.drawCircle(
                    10 + (i * 20).toFloat(),
                    mBounceDotRadius.toFloat(),
                    mDotRadius.toFloat(),
                    paint
                )
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width: Int
        val height: Int


        val calculatedWidth = 20 * 9
        width = calculatedWidth
        height = mBounceDotRadius * 2



        setMeasuredDimension(width, height)
    }

    private fun startAnimation() {
        val bounceAnimation =
            BounceAnimation()
        bounceAnimation.duration = 100
        bounceAnimation.repeatCount = Animation.INFINITE
        bounceAnimation.interpolator = LinearInterpolator()
        bounceAnimation.setAnimationListener(object :
            Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {
                mDotPosition++

                if (mDotPosition == mDotAmount) {
                    mDotPosition = 0
                }
                Log.d("INFOMETHOD", "----On Animation Repeat----")
            }
        })
        startAnimation(bounceAnimation)
    }

    private inner class BounceAnimation : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation
        ) {
            super.applyTransformation(interpolatedTime, t)

            invalidate()
        }
    }
}