package com.paint.onboarding

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OnBoardingViewModel : ViewModel() {
    private var TIMER_IN_MILLIS = 5000L
    private var INTERVAL_IN_MILLIS = 50L

    private var timeLeftInMillis = TIMER_IN_MILLIS
    var currentPage = 0
        private set
    var currentProgress = 0
        private set
    var slideCount = 0
    var timer: CountDownTimer? = null
        private set

    private val _onBoardingState = MutableLiveData<OnBoardingState>()
    val onBoardingState = _onBoardingState as LiveData<OnBoardingState>

    fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, INTERVAL_IN_MILLIS) {
            override fun onFinish() {
                _onBoardingState.value = OnBoardingState.NEXT
            }

            override fun onTick(tick: Long) {
                timeLeftInMillis = tick
                currentProgress++
                _onBoardingState.value = OnBoardingState.UPDATE_PB
            }
        }
        timer?.start()
    }

    fun resetTimer(position: Int) {
        timer?.cancel()
        currentProgress = 0
        currentPage = position
        timeLeftInMillis = TIMER_IN_MILLIS
        startTimer()
    }

    fun onPreviousSlide() {
        if (currentPage > 0)
            _onBoardingState.value = OnBoardingState.PREVIOUS
        else
            startTimer()
    }

    fun onNextSlide() {
        if (currentPage != slideCount - 1)
            _onBoardingState.value = OnBoardingState.NEXT
        else
            checkOnNotFinish()
    }

    // for finish last progressBar
    private fun checkOnNotFinish() {
        if (timeLeftInMillis > INTERVAL_IN_MILLIS)
            startTimer()
    }


    override fun onCleared() {
        timer?.cancel()
        super.onCleared()
    }
}
