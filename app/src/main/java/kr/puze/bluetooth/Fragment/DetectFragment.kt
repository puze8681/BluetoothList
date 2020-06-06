package kr.puze.bluetooth.Fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_detect.view.*
import kr.puze.bluetooth.R

class DetectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_detect, container, false)
        thisView = view
        changeView(view, detectData)
        return view
    }

    fun changeView(view: View?, data: Int) {
        detectData = data
        if(view != null){
            view.text_detect.text = "수치 : $data"
            if (data == -1) {
                view.layout_detect.setBackgroundColor(Color.WHITE)
            } else {
                when (data) {
                    in 0..200 -> {
                        view.layout_detect.setBackgroundColor(Color.BLUE)
                    }
                    in 200..400 -> {
                        view.layout_detect.setBackgroundColor(Color.GREEN)
                    }
                    else -> {
                        view.layout_detect.setBackgroundColor(Color.RED)
                    }
                }
            }
        }else if(thisView != null){
            thisView.text_detect.text = "수치 : $data"
            if (data == -1) {
                thisView.layout_detect.setBackgroundColor(Color.WHITE)
            } else {
                when (data) {
                    in 0..200 -> {
                        thisView.layout_detect.setBackgroundColor(Color.BLUE)
                    }
                    in 200..400 -> {
                        thisView.layout_detect.setBackgroundColor(Color.GREEN)
                    }
                    else -> {
                        thisView.layout_detect.setBackgroundColor(Color.RED)
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(): DetectFragment = DetectFragment()
        var detectData = -1
        lateinit var thisView: View
    }
}