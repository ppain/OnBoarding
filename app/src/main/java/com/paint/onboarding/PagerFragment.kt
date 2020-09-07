package com.paint.onboarding

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_page.*


class PagerFragment : Fragment(R.layout.fragment_page) {

    private lateinit var viewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(PageViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            container.setImageResource(it.getInt(IMAGE))
        }
        initListener()
    }

    private fun initListener() {
        container.setOnClickListener {
            if (viewModel.isRightSide())
                (parentFragment as? OnFragmentInteractionListener)?.onNextSlide()
            else
                (parentFragment as? OnFragmentInteractionListener)?.onPreviousSlide()
        }

        container.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    (parentFragment as? OnFragmentInteractionListener)?.onPauseAutoScroll()
                    viewModel.startClick = System.currentTimeMillis()
                    viewModel.clickCoordinateX = event.rawX
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    (parentFragment as? OnFragmentInteractionListener)?.onResumeAutoScroll()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (viewModel.isLongClick(System.currentTimeMillis())) {
                        (parentFragment as? OnFragmentInteractionListener)?.onResumeAutoScroll()
                        true
                    } else {
                        v.performClick()
                        false
                    }
                }
                else -> {
                    false
                }
            }
        }
    }

    interface OnFragmentInteractionListener {
        fun onPreviousSlide()
        fun onNextSlide()
        fun onResumeAutoScroll()
        fun onPauseAutoScroll()
    }

    companion object {
        private const val IMAGE = "image"

        @JvmStatic
        fun newInstance(position: Int) = PagerFragment().apply {
            arguments = Bundle().apply {
                putInt(IMAGE, ViewPagerAdapter.imageList[position])
            }
        }
    }
}