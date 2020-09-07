package com.paint.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment = PagerFragment.newInstance(position)

    override fun getItemCount(): Int = imageList.size

    companion object {
        val imageList = arrayOf(
            R.drawable.onboard_picture,
            R.drawable.onboard_filled,
            R.drawable.onboard_image
        )
    }
}