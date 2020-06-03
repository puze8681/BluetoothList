package kr.puze.bluetooth.Fragment

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeAdvertiser
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_bluetooth.view.*
import kr.puze.bluetooth.Beacon
import kr.puze.bluetooth.BeaconAdapter
import kr.puze.bluetooth.R
import java.text.SimpleDateFormat
import java.util.*


class BluetoothFragment : Fragment() {

    lateinit var mScanCallback: ScanCallback
    lateinit var thisView: View
    var PERMISSIONS = 100
    var REQUEST_ENABLE_BT = 200
    lateinit var mBluetoothAdapter: BluetoothAdapter
    lateinit var mBluetoothLeScanner: BluetoothLeScanner
    lateinit var mBluetoothLeAdvertiser: BluetoothLeAdvertiser
    lateinit var beacon: Vector<Beacon>
    lateinit var beaconAdapter: BeaconAdapter
//    lateinit var mScanSettings: ScanSettings.Builder
//    var scanFilters: ArrayList<ScanFilter> = ArrayList()
    var simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREAN)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_bluetooth, container, false)
        thisView = view
        settingBluetooth(view)
//        mScanSettings = ScanSettings.Builder()
//        mScanSettings.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        // 얘는 스캔 주기를 2초로 줄여주는 Setting입니다.
        // 공식문서에는 위 설정을 사용할 때는 다른 설정을 하지 말고
        // 위 설정만 단독으로 사용하라고 되어 있네요 ^^
        // 위 설정이 없으면 테스트해 본 결과 약 10초 주기로 스캔을 합니다.
//        var scanSettings = mScanSettings.build()
//        scanFilters = ArrayList(Vector())
//        var scanFilter = ScanFilter.Builder()
//        scanFilter.setDeviceAddress("특정 기기의 MAC 주소") //ex) 00:00:00:00:00:00
//        var scan = scanFilter.build()
//        scanFilters.add(scan)
//        mBluetoothLeScanner.startScan(scanFilters.toList(), scanSettings, mScanCallback)

        // filter와 settings 기능을 사용하지 않을 때는  처럼 사용하시면 돼요.
        return view
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun settingBluetooth(view: View){
        ActivityCompat.requestPermissions(
            this.activity!!, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMISSIONS
        )

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!mBluetoothAdapter.isEnabled) {
            startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT)
        }else{
            mBluetoothLeScanner = mBluetoothAdapter.bluetoothLeScanner
            mBluetoothLeAdvertiser = mBluetoothAdapter.bluetoothLeAdvertiser
            beacon = Vector()
            mBluetoothLeScanner.startScan(getScanCallback(view))
        }
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getScanCallback(view: View): ScanCallback {
        mScanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)
                try {
                    val scanRecord = result.scanRecord
                    Log.d(
                        "getTxPowerLevel()",
                        scanRecord!!.txPowerLevel.toString() + ""
                    )
                    result.device
                    Log.d(
                        "onScanResult()",
                        result.device.address.toString() + "\n" + result.rssi + "\n" + result.device.name
                                + "\n" + result.device.bondState + "\n" + result.device.type
                    )
                    activity!!.runOnUiThread {
                        beacon.add(Beacon(result.device.address, result.rssi, simpleDateFormat.format(Date())))
                        beaconAdapter = BeaconAdapter(beacon, layoutInflater)
                        view.list_bluetooth.adapter = beaconAdapter
                        view.list_bluetooth.onItemClickListener =
                            OnItemClickListener { parent, view, position, id ->
                                Toast.makeText(activity, "블루투스 연결 성공!", Toast.LENGTH_SHORT).show()
                            }
                        beaconAdapter.notifyDataSetChanged()
                    }
//                    Thread(Runnable {
//
//                    }).start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onBatchScanResults(results: List<ScanResult?>) {
                super.onBatchScanResults(results)
                Log.d("onBatchScanResults", results.size.toString() + "")
            }

            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
                Log.d("onScanFailed()", errorCode.toString() + "")
            }
        }

        return mScanCallback
    }


//    private fun setRecyclerView(view: View, items: ArrayList<String>) {
//        var adapter = BluetoothRecyclerAdapter(items, this.activity!!)
//        beaconListView!!.adapter = adapter
//        adapter.notifyDataSetChanged()
//        adapter.itemClick = object : BluetoothRecyclerAdapter.ItemClick {
//            override fun onItemClick(view: View?, position: Int) {
//            }
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDestroy() {
        super.onDestroy()
        mBluetoothLeScanner.stopScan(mScanCallback)
    }

    companion object {
        fun newInstance(): BluetoothFragment = BluetoothFragment()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ENABLE_BT) settingBluetooth(thisView)
    }
}