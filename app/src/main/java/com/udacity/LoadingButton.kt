package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // This would contain the loading background dimensions.
    private val loadingRect = Rect()
    private var progress: Int = 0

    private var oval = RectF(60.25f, 400f, 130f, 148f)
    private var widthSize = 0
    private var heightSize = 0
    private var valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        when (new) {
            ButtonState.Loading -> {
                valueAnimator = ValueAnimator.ofInt(0, 360).setDuration(2000)
                    .apply {
                        addUpdateListener {
                            progress = it.animatedValue as Int
                            invalidate()
                        }
                        repeatCount = ValueAnimator.INFINITE
                        repeatMode = ValueAnimator.RESTART
                        start()
                    }

            }
            ButtonState.Completed -> {
                valueAnimator.cancel()
            }
//            else -> {
//                valueAnimator.currentPlayTime
//            }
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }


    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)

        onSizeChanged(20,40, 10, 10)
        paint.color = if (buttonState == ButtonState.Completed) Color.GREEN else Color.CYAN
//        loadingRect.set(120, 20, width * progress / 360, height)
        loadingRect.offset(-220, 0)
        loadingRect.set(320, 40, width * progress / 360,  height)
        canvas?.drawRect(loadingRect, paint)
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
