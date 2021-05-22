package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val paintInitialButtonColor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.colorPrimary)
        isAntiAlias = true
    }

    private val paintLoadingButtonColor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.colorPrimaryDark)
        isAntiAlias = true
    }

    private val paintButtonText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.white)
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        isAntiAlias = true
    }


    // Paint object for coloring and styling Circle
    private var paintCircle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.colorAccent)
        isAntiAlias = true
    }



    private var circleAnimator = ValueAnimator()
    private var buttonAnimator = ValueAnimator()
    private lateinit var buttonText: String

    private var buttonWidth = 0.0f
    private var loadingAngle = 0.0f
    private var animDuration = 3000L


    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        buttonText = resources.getString(R.string.button_loading)

        when(new) {

            ButtonState.Loading -> {
                // button is loading
                paintCircle.color = context.getColor(R.color.colorAccent)
                loadingAngle = 0.0f
                buttonWidth = measuredWidth.toFloat()

                // circle aninmation
                circleAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
                    duration = animDuration
                    repeatMode = ValueAnimator.REVERSE
                    repeatCount = ValueAnimator.INFINITE
                    interpolator = AccelerateInterpolator(1f)
                    addUpdateListener {
                        loadingAngle = animatedValue as Float
                        invalidate()
                    }
                }

                // button animation
                buttonAnimator = ValueAnimator.ofInt(0, widthSize).apply {
                    duration = animDuration
                    repeatMode = ValueAnimator.RESTART
                    repeatCount = 1
                    addUpdateListener {
                        buttonWidth = animatedValue as Float
                        invalidate()
                    }
                }
                circleAnimator.start()
                buttonAnimator.start()

            }

            ButtonState.Completed -> {
                // downloading completed : reset value
                buttonWidth = 0f
                loadingAngle = 0f
                buttonAnimator.cancel()
                circleAnimator.cancel()
                animDuration = 0
                invalidate()


            }
            else -> {
                buttonState = ButtonState.Clicked
            }
        }

    }


    init {

        buttonState = ButtonState.Clicked
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val textHeight: Float = paintButtonText.descent() - paintButtonText.ascent()
        val textOffset: Float = textHeight / 2 - paintButtonText.descent()
        canvas?.let {
           it.apply {
               drawRect(0.0f, 0.0f, widthSize.toFloat(), 0.0f, paintInitialButtonColor)
               drawRect(0.0f, 0.0f, 0.0f, heightSize.toFloat(), paintLoadingButtonColor)
               drawText(buttonText, widthSize.toFloat() / 2.0f, heightSize.toFloat() / 2.0f + textOffset, paintButtonText )

               canvas?.drawArc(0.0f- 145f,
                   heightSize / 2 - 35f,
                   widthSize - 75f,
                   heightSize / 2 + 35f,
                   0F,
                   0.0f,
                   true,
                   paintCircle)
           }

        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}