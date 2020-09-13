package com.paint.onboarding

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_onbording.*

class OnBoardingFragment : DialogFragment(), PagerFragment.OnFragmentInteractionListener {

    override fun getTheme(): Int {
        return R.style.Theme_App_Dialog_FullScreen
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_onbording, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initPageIndicatorView()
        initClickListener()
    }

    private fun initClickListener() {
        iv_on_boarding_close.setOnClickListener { dismiss() }
    }

    private fun initPageIndicatorView() {
        pi_on_boarding_indicator.attachToViewPager(vp_on_boarding)
        pi_on_boarding_indicator.slideCount = ViewPagerAdapter.imageList.size
    }

    private fun initViewPager() {
        vp_on_boarding.apply {
            adapter = ViewPagerAdapter(this@OnBoardingFragment)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setPageTransformer(DepthPageTransformer())
            }
        }
    }

    override fun onPreviousSlide() {
        pi_on_boarding_indicator.onPreviousSlide()
    }

    override fun onNextSlide() {
        pi_on_boarding_indicator.onNextSlide()
    }

    override fun onPauseAutoScroll() {
        pi_on_boarding_indicator.cancelTimer()
    }

    override fun onResumeAutoScroll() {
        pi_on_boarding_indicator.startTimer()
    }

    override fun onDismiss(dialog: DialogInterface) {
        pi_on_boarding_indicator.detachFromViewPager()
        super.onDismiss(dialog)
    }
}