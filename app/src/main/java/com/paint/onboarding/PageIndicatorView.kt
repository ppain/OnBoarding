package com.paint.onboarding

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2

class PageIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    init {
        orientation = HORIZONTAL
        handleAttributes(context, attrs)
    }

    private var startMargin = 0
    private var endMargin = 0
    private var timer = 0L
    private var interval = 0L

    private var arrayPB = emptyArray<ProgressBar>()
    var slideCount = 0
        set(value) {
            field = value
            initIndicator()
        }
    private var pager: ViewPager2? = null
    private var timeLeftInMillis = timer
    private var currentPage = 0
    private var currentProgress = 0
    private var countDownTimer: CountDownTimer? = null

    private fun initIndicator() {
        removeAllViews()

        arrayPB = Array(slideCount) {
            ProgressBar(context, null, R.style.Widget_AppCompat_ProgressBar_Horizontal).apply {
                max = 95
                progress = 50
                isIndeterminate = false
                progressDrawable = ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.progress_bar_rounded,
                    null
                )
            }
        }
        val paramsPB = LayoutParams(LayoutParams.WRAP_CONTENT, 6, 1f)
        paramsPB.setMargins(startMargin, 0, endMargin, 0)
        for (i in 0 until slideCount) {
            addView(arrayPB[i], paramsPB)
        }

        startTimer()
    }

    private val slidingCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            for (i in 0 until slideCount) {
                arrayPB[i].progress = 0
            }
            resetTimer(position)
        }
    }

    private fun handleAttributes(context: Context, attrs: AttributeSet?) {
        attrs?.let {
            val styleAttributes =
                context.obtainStyledAttributes(it, R.styleable.PageIndicatorView, 0, 0)
            try {
                startMargin = styleAttributes.getDimensionPixelSize(
                    R.styleable.PageIndicatorView_startMargin, 8
                )
                endMargin = styleAttributes.getDimensionPixelSize(
                    R.styleable.PageIndicatorView_endMargin, 8
                )
                timer = styleAttributes.getDimensionPixelSize(
                    R.styleable.PageIndicatorView_timer, 5000
                ).toLong()
                interval = styleAttributes.getDimensionPixelSize(
                    R.styleable.PageIndicatorView_interval, 50
                ).toLong()
            } finally {
                styleAttributes.recycle()
            }
        }
    }

    fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, interval) {
            override fun onFinish() {
                pager?.setCurrentItem(currentPage + 1, true)
            }

            override fun onTick(tick: Long) {
                timeLeftInMillis = tick
                currentProgress++
                arrayPB[currentPage].progress = currentProgress
            }
        }
        countDownTimer?.start()
    }

    fun resetTimer(position: Int) {
        cancelTimer()
        currentProgress = 0
        currentPage = position
        timeLeftInMillis = timer
        startTimer()
    }

    fun cancelTimer() {
        countDownTimer?.cancel()
    }

    fun onPreviousSlide() {
        if (currentPage > 0)
            pager?.setCurrentItem(currentPage - 1, true)
        else
            startTimer()
    }

    fun onNextSlide() {
        if (currentPage != slideCount - 1)
            pager?.setCurrentItem(currentPage + 1, true)
        else
            checkOnNotFinish()
    }

    // for finish last progressBar
    private fun checkOnNotFinish() {
        if (timeLeftInMillis > interval)
            startTimer()
    }

    fun attachToViewPager(viewPager: ViewPager2) {
        if (pager != null) throw IllegalStateException("A ViewPager2 has already been attached")
        pager = viewPager
        viewPager.registerOnPageChangeCallback(slidingCallback)
    }

    fun detachFromViewPager() {
        pager?.unregisterOnPageChangeCallback(slidingCallback)
        cancelTimer()
    }

}