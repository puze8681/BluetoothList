package kr.puze.bluetooth

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import kr.puze.bluetooth.Fragment.MainPagerAdapter
import com.google.android.material.tabs.TabLayout
import kr.puze.bluetooth.Fragment.DetectFragment

class MainActivity : AppCompatActivity() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var contextMain: Context
        private var viewPager: ViewPager? = null
        private var currentPage: Int = 0
        private var backKeyPressedTime: Long = 0L
        var isBluetoothConnected = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contextMain = this@MainActivity
        val viewPagerAdapter = MainPagerAdapter(supportFragmentManager)

        tab_layout_main.addTab(tab_layout_main.newTab().setText("home").setIcon(R.mipmap.ic_launcher).setTag("home"), 0)
        tab_layout_main.addTab(tab_layout_main.newTab().setText("bluetooth").setIcon(R.mipmap.ic_launcher).setTag("bluetooth"), 1)
        tab_layout_main.addTab(tab_layout_main.newTab().setText("detect").setIcon(R.mipmap.ic_launcher).setTag("detect"), 2)
        tab_layout_main.tabGravity = TabLayout.GRAVITY_FILL

        viewPager = view_pager_main

        view_pager_main.adapter = viewPagerAdapter
        view_pager_main.addOnPageChangeListener(viewPageChangeListener)

        tab_layout_main.setupWithViewPager(view_pager_main)
        initTabLayout()
        view_pager_main.currentItem = 0
        view_pager_main.offscreenPageLimit = 4
    }

    private val viewPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            offTabLayout()
            for (index in 0..2) {
                when (index) {
                    0 -> if (index == position){
                        currentPage = 0
                        tab_layout_main.getTabAt(position)!!.setIcon(R.mipmap.ic_launcher)
                    }
                    1 -> if (index == position){
                        currentPage = 1
                        tab_layout_main.getTabAt(position)!!.setIcon(R.mipmap.ic_launcher)
                    }
                    2 -> if (index == position){
                        currentPage = 2
                        tab_layout_main.getTabAt(position)!!.setIcon(R.mipmap.ic_launcher)
                    }
                }
            }
        }
    }

    fun goPage(index: Int){
        viewPager?.currentItem = index
    }

    private fun initTabLayout() {
        tab_layout_main.getTabAt(0)?.setIcon(R.mipmap.ic_launcher)
        tab_layout_main.getTabAt(1)?.setIcon(R.mipmap.ic_launcher)
        tab_layout_main.getTabAt(2)?.setIcon(R.mipmap.ic_launcher)
    }

    private fun offTabLayout() {
        tab_layout_main.getTabAt(0)?.setIcon(R.mipmap.ic_launcher)
        tab_layout_main.getTabAt(1)?.setIcon(R.mipmap.ic_launcher)
        tab_layout_main.getTabAt(2)?.setIcon(R.mipmap.ic_launcher)
    }

    override fun onBackPressed() {
        if(currentPage == 0){
            if(System.currentTimeMillis() > backKeyPressedTime + 2000L){
                backKeyPressedTime = System.currentTimeMillis()
                Toast.makeText(this@MainActivity, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            }else{
                finish()
            }
        }else{
            goPage(0)
        }
    }
}
