package kr.puze.bluetooth.Fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_detect.view.*
import kr.puze.bluetooth.R

class DetectFragment: Fragment() {

    private var frameId: Int = 0
    private var data = 100

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_detect, container, false)
        view.layout_detect.setOnClickListener {
            changeView(view, data)
            when(data){
                100-> data = 300
                300 -> data = 500
                500 -> data = 100
            }
        }
        return view
    }

    fun init(){

    }

    fun detect(){

    }

    fun changeView(view: View, data: Int){
        view.text_detect.text = "수치 : $data"
        when(data){
            in 0..200->{ view.layout_detect.setBackgroundColor(Color.BLUE) }
            in 200..400->{ view.layout_detect.setBackgroundColor(Color.GREEN) }
            else -> { view.layout_detect.setBackgroundColor(Color.RED) }
        }
    }
    companion object {
        fun newInstance(): DetectFragment = DetectFragment()
    }
}