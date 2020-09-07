package com.paint.onboarding

import androidx.lifecycle.ViewModel

class PageViewModel : ViewModel() {
    private var DURATION_CLICK = 200L
    var startClick = 0L
    var clickCoordinateX = 0F

    fun isLongClick(currentTimeMillis: Long) = currentTimeMillis - startClick > DURATION_CLICK

    fun isRightSide() = clickCoordinateX > middleWidthScreen
}