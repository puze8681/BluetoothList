package kr.puze.bluetooth.Fragment

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.bluetooth.le.BluetoothLeAdvertiser
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
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
import kr.puze.bluetooth.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
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
    lateinit var pairedAdapter: PairedAdapter
    lateinit var mDevices: Set<BluetoothDevice>
    lateinit var mSocket: BluetoothSocket
    lateinit var mOutputStream: OutputStream
    lateinit var mInputStream: InputStream
    lateinit var mWorkerThread: Thread
    var simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREAN)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_bluetooth, container, false)
        thisView = view
        settingBluetooth(view)
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

        selectDevice(view)
    }

    fun selectDevice(view: View) {
        mDevices = mBluetoothAdapter.bondedDevices;
        var mPairedDeviceCount = mDevices.size

        if(mPairedDeviceCount > 0){
            // 페어링 된 블루투스 장치의 이름 목록 작성
            var paring: Vector<Paired> = Vector()
            for (device: BluetoothDevice in mDevices){
                paring.add(Paired(device.name, device.address, device.uuids.toString()))
                pairedAdapter = PairedAdapter(paring, layoutInflater)
                view.list_bluetooth.adapter = beaconAdapter
                view.list_bluetooth.onItemClickListener = OnItemClickListener { parent, view, position, id ->
                    connectToSelectedDevice(paring[position].name)
                }
                beaconAdapter.notifyDataSetChanged()
            }
        }
    }

    fun getDeviceFromBondedList(name: String): BluetoothDevice? {
        var selectedDevice: BluetoothDevice? = null

        for (device: BluetoothDevice in mDevices){
            if(name == device.name) {
                selectedDevice = device
                break;
            }
        }

        return selectedDevice;
    }

    fun connectToSelectedDevice(selectedDeviceName: String) {
        var mRemoteDevice = getDeviceFromBondedList(selectedDeviceName)
        var uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        if(mRemoteDevice != null){
            try {
                // 소켓 생성
                mSocket = mRemoteDevice.createRfcommSocketToServiceRecord(uuid)
                // RFCOMM 채널을 통한 연결
                mSocket.connect();

                // 데이터 송수신을 위한 스트림 열기
                mOutputStream = mSocket.outputStream
                mInputStream = mSocket.inputStream

                // 데이터 수신 준비
                beginListenForData()
                Toast.makeText(activity, "블루투스 연결 성공!", Toast.LENGTH_SHORT).show()
            }catch(e: Exception) {
                // 블루투스 연결 중 오류 발생
                Toast.makeText(activity, "블루투스 연결 실패!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun beginListenForData(){
        var handler = Handler()

        var readBuffer = ByteArray(1024) ;  //  수신 버퍼
        var readBufferPositon = 0;        //   버퍼 내 수신 문자 저장 위치

        mWorkerThread = Thread(Runnable {
            while (!Thread.currentThread().isInterrupted){
                try {
                    var bytesAvailable = mInputStream.available()
                    if(bytesAvailable > 0){
                        var packetBytes = ByteArray(bytesAvailable)
                        mInputStream.read(packetBytes)
                        for(i in 0..bytesAvailable){
                            var b = packetBytes[i]
                            var a = "123"
                            a += b
                            if(b.equals('x')){
                                var encodedBytes = ByteArray(readBufferPositon)
                                System.arraycopy(readBuffer,0,encodedBytes,0,encodedBytes.size)
                                var data = String(encodedBytes, Charset.forName("US-ASCII"))
                                readBufferPositon = 0
                                var result = 0
                                for (i in readBuffer.indices) {
                                    result = result or (readBuffer[i].toInt() shl 8 * i)
                                }
                                handler.post(Runnable {
                                    Toast.makeText(this.activity!!, data, Toast.LENGTH_SHORT).show()
                                    Toast.makeText(this.activity!!, result.toString(), Toast.LENGTH_SHORT).show()
                                    //데이터가 수신되면 할 것
                                })
                            }else{
                                readBuffer[readBufferPositon++] = b
                            }
                        }
                    }
                }catch (e: IOException){

                }
            }
        })
        mWorkerThread.start()

        MainActivity.isBluetoothConnected = true
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
                        beacon.add(Beacon(result.device.address, result.rssi, simpleDateFormat.format(Date()), result.device))
                        beaconAdapter = BeaconAdapter(beacon, layoutInflater)
                        view.list_bluetooth.adapter = beaconAdapter
                        view.list_bluetooth.onItemClickListener =
                            OnItemClickListener { parent, view, position, id ->
                                try {
                                    beacon[position].device.createBond()
                                    Toast.makeText(activity, "블루투스 페어링 성공!", Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    Toast.makeText(activity, "블루투스 페어링 실패!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        beaconAdapter.notifyDataSetChanged()
                    }
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDestroy() {
        super.onDestroy()
        mBluetoothLeScanner.stopScan(mScanCallback)
        try {
          mWorkerThread.interrupt();   // 데이터 수신 쓰레드 종료
          mInputStream.close();
          mOutputStream.close();
          mSocket.close();
        } catch(e: Exception) {

        }
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