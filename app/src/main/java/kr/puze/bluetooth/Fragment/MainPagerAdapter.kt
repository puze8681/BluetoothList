package kr.puze.bluetooth.Fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.view.ViewGroup

class MainPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> HomeFragment.newInstance()
            1 -> BluetoothFragment.newInstance()
            2 -> DetectFragment.newInstance()
            else -> HomeFragment.newInstance()
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    }

    override fun getCount(): Int = 3
}