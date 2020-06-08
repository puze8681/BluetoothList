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
                view.text_detect.text = "수치 : -"
                view.layout_detect.setBackgroundColor(Color.WHITE)
            } else {
                when (data) {
                    in 0..200 -> {
                        view.text_detect.text = "수치 : $data (안전)"
                        view.layout_detect.setBackgroundColor(Color.parseColor("#536DFE"))
                    }
                    in 200..400 -> {
                        view.text_detect.text = "수치 : $data (주위)"
                        view.layout_detect.setBackgroundColor(Color.parseColor("#4CAF50"))
                    }
                    else -> {
                        view.text_detect.text = "수치 : $data (위험)"
                        view.layout_detect.setBackgroundColor(Color.parseColor("#E91E63"))
                    }
                }
            }
        }else if(thisView != null){
            thisView.text_detect.text = "수치 : $data"
            if (data == -1) {
                thisView.text_detect.text = "수치 : -"
                thisView.layout_detect.setBackgroundColor(Color.WHITE)
            } else {
                when (data) {
                    in 0..200 -> {
                        thisView.text_detect.text = "수치 : $data (안전)"
                        thisView.layout_detect.setBackgroundColor(Color.parseColor("#536DFE"))
                    }
                    in 200..400 -> {
                        thisView.text_detect.text = "수치 : $data (주위)"
                        thisView.layout_detect.setBackgroundColor(Color.parseColor("#4CAF50"))
                    }
                    else -> {
                        thisView.text_detect.text = "수치 : $data (위험)"
                        thisView.layout_detect.setBackgroundColor(Color.parseColor("#E91E63"))
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