package kr.puze.bluetooth

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import java.util.*


class PairedAdapter(beacons: Vector<Paired>, layoutInflater: LayoutInflater?) :
    BaseAdapter() {
    private val beacons: Vector<Paired> = beacons
    private val layoutInflater: LayoutInflater? = layoutInflater
    override fun getCount(): Int {
        return beacons.size
    }

    override fun getItem(position: Int): Any? {
        return beacons.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView: View? = convertView
        val beaconHolder: BeaconHolder
        if (convertView == null) {
            beaconHolder = BeaconHolder()
            convertView = layoutInflater!!.inflate(R.layout.item_beacon, parent, false)
            beaconHolder.address = convertView.findViewById(R.id.address)
            beaconHolder.name = convertView.findViewById(R.id.rssi)
            beaconHolder.uuid = convertView.findViewById(R.id.time)
            convertView.tag = beaconHolder
        } else {
            beaconHolder = convertView.tag as BeaconHolder
        }
        beaconHolder.uuid!!.text = "uuid :" + beacons[position].uuid
        beaconHolder.address!!.text = "MAC Addr :" + beacons[position].address
        beaconHolder.name!!.text = "name :" + beacons[position].name
        return convertView
    }

    private inner class BeaconHolder {
        var address: TextView? = null
        var name: TextView? = null
        var uuid: TextView? = null
    }

}

