package kr.puze.bluetooth.Fragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_home.view.*
import kr.puze.bluetooth.MainActivity
import kr.puze.bluetooth.R

class HomeFragment: Fragment() {

    private var frameId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        view.button_start.setOnClickListener {
            if(MainActivity.isBluetoothConnected){
                Toast.makeText(this.activity!!, "3초간 숨을 크게 내쉬어주세요!", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({
                    MainActivity().goPage(2)
                }, 2000)
            }else{
                Toast.makeText(this.activity!!, "블루투스가 연결되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }
}