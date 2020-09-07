package com.paint.onboarding

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_onbording.*

class OnBoardingFragment : DialogFragment(), PagerFragment.OnFragmentInteractionListener {

    private lateinit var viewModel: OnBoardingViewModel

    private var arrayPB = emptyArray<ProgressBar>()

    override fun getTheme(): Int {
        return R.style.Theme_App_Dialog_FullScreen
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(OnBoardingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_onbording, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initClickListener()
        initObserveTimer()
    }

    private fun initClickListener() {
        iv_on_boarding_close.setOnClickListener { dismiss() }
    }

    private fun initObserveTimer() {
        viewModel.onBoardingState.observe(viewLifecycleOwner, Observer {
            when (it) {
                OnBoardingState.PREVIOUS -> vp_on_boarding.setCurrentItem(
                    viewModel.currentPage - 1,
                    true
                )
                OnBoardingState.NEXT -> vp_on_boarding.setCurrentItem(
                    viewModel.currentPage + 1,
                    true
                )
                OnBoardingState.UPDATE_PB -> arrayPB[viewModel.currentPage].progress =
                    viewModel.currentProgress
            }
        })
    }

    private fun initViewPager() {
        vp_on_boarding.apply {
            adapter = ViewPagerAdapter(this@OnBoardingFragment)
            registerOnPageChangeCallback(slidingCallback)
        }

        initIndicator()
    }

    private val slidingCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            for (i in 0 until viewModel.slideCount) {
                arrayPB[i].progress = 0
            }
            viewModel.resetTimer(position)
        }
    }

    //ToDo mb customView?
    private fun initIndicator() {
        viewModel.slideCount = ViewPagerAdapter.imageList.size
        arrayPB = Array(viewModel.slideCount) {
            this@OnBoardingFragment.layoutInflater.inflate(
                R.layout.view_progress_bar, null
            ) as ProgressBar
        }

        for (i in 0 until viewModel.slideCount) {
            val paramsPB = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 6, 1f)
            paramsPB.setMargins(8, 0, 8, 0)
            ll_on_boarding_indicator.addView(arrayPB[i], paramsPB)
        }

        viewModel.startTimer()
    }

    override fun onPreviousSlide() {
        viewModel.onPreviousSlide()
    }

    override fun onNextSlide() {
        viewModel.onNextSlide()
    }

    override fun onPauseAutoScroll() {
        viewModel.timer?.cancel()
    }

    override fun onResumeAutoScroll() {
        viewModel.startTimer()
    }

    override fun onDismiss(dialog: DialogInterface) {
        vp_on_boarding.unregisterOnPageChangeCallback(slidingCallback)
        super.onDismiss(dialog)
    }
}