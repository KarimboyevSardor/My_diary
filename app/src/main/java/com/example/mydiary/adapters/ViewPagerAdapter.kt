package com.example.mydiary.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mydiary.fragments.AllDiaryView
import com.example.mydiary.fragments.FavoriteView

class ViewPagerAdapter(fm: Fragment) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                AllDiaryView()
            }
            1 -> {
                FavoriteView()
            }
            else -> {Fragment()}
        }
    }
}