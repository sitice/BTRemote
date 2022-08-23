package com.example.btremote.app

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import com.example.btremote.bluetooth.*
import com.example.btremote.protocol.readProtocolCallBack
import com.example.btremote.protocol.readUartData
import com.example.btremote.tools.LogUtil
import com.example.btremote.tools.ToastUtil
import com.example.btremote.usb.*
import com.example.btremote.wave.waveDataReceiver
import com.example.btremote.wifi.WifiService
import kotlinx.coroutines.flow.MutableStateFlow


class App : Application() {

    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            when (p1?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val scanDevice: BluetoothDevice =
                        p1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) ?: return
                    if (isNeglectNoNameDeviceFlow.value && scanDevice.name != null) {
                        unpairedBluetoothFlow.value =
                            unpairedBluetoothFlow.value.toMutableList().also {
                                it.add(scanDevice)
                            }
                    }
                    LogUtil.log(
                        "Device :",
                        "name: ${scanDevice.name ?: "null"}  id: ${scanDevice.address}"
                    )
                    unpairedBluetoothFlow.value =
                        unpairedBluetoothFlow.value.toSet().toMutableList()
                }
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    when (p1.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)) {
                        BluetoothAdapter.STATE_ON -> {
                            bluetoothStateFlow.value = BLUETOOTHStatus.DISCONNECTED
                            ToastUtil.toast(this@App, "蓝牙打开成功")
                            pairedBluetoothFlow.value =
                                bluetoothService.mManager.adapter.bondedDevices.toList()
                        }
                        BluetoothAdapter.STATE_OFF -> {
                            bluetoothStateFlow.value = BLUETOOTHStatus.DISABLE
                            ToastUtil.toast(this@App, "蓝牙关闭成功")
                        }
                        BluetoothAdapter.STATE_TURNING_OFF -> {
                            bluetoothStateFlow.value = BLUETOOTHStatus.TURNING_OFF
                            ToastUtil.toast(this@App, "蓝牙正在关闭")
                        }
                        BluetoothAdapter.STATE_TURNING_ON -> {
                            bluetoothStateFlow.value = BLUETOOTHStatus.TURNING_ON
                            ToastUtil.toast(this@App, "蓝牙正在打开")
                        }
                    }
                }
                BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED -> {
                    ToastUtil.toast(this@App, "连接成功")
                    when (p1.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, 0)) {
                        BluetoothAdapter.STATE_CONNECTED -> {
                            bluetoothStateFlow.value = BLUETOOTHStatus.CONNECTED
                            ToastUtil.toast(this@App, "连接成功")
                        }
                        BluetoothAdapter.STATE_CONNECTING -> {
                            bluetoothStateFlow.value = BLUETOOTHStatus.CONNECTING
                            ToastUtil.toast(this@App, "正在连接中")
                        }
                        BluetoothAdapter.STATE_DISCONNECTED -> {
                            bluetoothStateFlow.value = BLUETOOTHStatus.DISCONNECTED
                            ToastUtil.toast(this@App, "断开连接")
                        }
                        BluetoothAdapter.STATE_DISCONNECTING -> {
                            bluetoothStateFlow.value = BLUETOOTHStatus.DISCONNECTING
                            ToastUtil.toast(this@App, "断开连接中")
                        }
                    }
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    ToastUtil.toast(this@App, "蓝牙扫描结束")
                    bluetoothScanStateFlow.value = false
                }

                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    ToastUtil.toast(this@App, "蓝牙扫描中")
                    bluetoothScanStateFlow.value = true
                }
                BluetoothDevice.ACTION_PAIRING_REQUEST -> {
                    val btDevice: BluetoothDevice =
                        p1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) ?: return
                    if (btDevice.name != null) {
                        val state: Int = p1.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
                        if (state == BluetoothDevice.BOND_NONE)
                            ToastUtil.toast(this@App, "取消与${btDevice.name}配对成功")
                        if (state == BluetoothDevice.BOND_BONDED)
                            ToastUtil.toast(this@App, "与${btDevice.name}配对成功")
                        if (state == BluetoothDevice.BOND_BONDING)
                            ToastUtil.toast(this@App, "正在与${btDevice.name}配对")
                    }

                }
            }
        }

    }

    private val wifiReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            when (p1?.action) {
                WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                    when (p1.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0)) {
                        WifiManager.WIFI_STATE_DISABLED -> {
                            wifiStatusFlow.value = WIFIStatus.DISABLE
                            LogUtil.log("WIFI", "WIFI_STATE_DISABLED")
                        }
                        WifiManager.WIFI_STATE_DISABLING -> {
                            wifiStatusFlow.value = WIFIStatus.TURNING_OFF
                            LogUtil.log("WIFI", "WIFI_STATE_DISABLING")
                        }
                        WifiManager.WIFI_STATE_ENABLED -> {
                            wifiService.getNetStatus()
                            LogUtil.log("WIFI", "WIFI_STATE_ENABLED")
                        }
                        WifiManager.WIFI_STATE_ENABLING -> {
                            wifiStatusFlow.value = WIFIStatus.TURNING_ON
                            LogUtil.log("WIFI", "WIFI_STATE_ENABLING")
                        }
                    }
                }
            }
        }
    }

    private val handlerCallBack = Handler.Callback { it ->
        when (it.what) {
            MESSAGE_READ -> {
                bluetoothRecDataFlow.value = (it.obj as ByteArray).copyOfRange(0, it.arg1)
                bluetoothRecDataLengthFlow.value += it.arg1
                readUartData(bluetoothRecDataFlow.value)
                LogUtil.log("MESSAGE_READ")
            }
            MESSAGE_WRITE -> {
                LogUtil.log("MESSAGE_WRITE")
            }
            CONNECT_SUCCESS -> {
                bluetoothRecDataLengthFlow.value = 0
                bluetoothStateFlow.value = BLUETOOTHStatus.CONNECTED
                ToastUtil.toast(this, "连接成功")
            }
            CONNECT_FAIL -> {
                bluetoothStateFlow.value = BLUETOOTHStatus.DISCONNECTED
                connectedBluetoothDevice.value = null
                LogUtil.log("CONNECT_FAIL")
            }
            GET_SOCKET_FAIL -> {
                connectedBluetoothDevice.value = null
                LogUtil.log("GET_SOCKET_FAIL")
            }
            CLOSE_CONNECT_FAIL -> {
                connectedBluetoothDevice.value = null
                LogUtil.log("CLOSE_CONNECT_FAIL")
            }
            SEND_FAIL -> {
                bluetoothStateFlow.value = BLUETOOTHStatus.DISCONNECTED
                LogUtil.log("SEND_FAIL")
            }
            REC_FAIL -> {
                bluetoothStateFlow.value = BLUETOOTHStatus.DISCONNECTED
                LogUtil.log("SEND_FAIL")
            }
            USB_OPEN_FAIL -> {
                usbStateFlow.value = false
            }
            USB_INIT_FAIL -> {
                usbStateFlow.value = false
            }
            USB_OPEN_SUCCESS -> {
                usbStateFlow.value = true
            }
            USB_READ -> {

            }
            USB_WRITE -> {

            }
            NO_PERMISSION -> {
                usbStateFlow.value = false
            }
        }
        true
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext

        val handlerThread = HandlerThread("")
        handlerThread.start()
        val handler = Handler(handlerThread.looper, handlerCallBack)
        bluetoothService = BluetoothService(this, handler)
        pairedBluetoothFlow.value = bluetoothService.mManager.adapter.bondedDevices.toList()
        bluetoothStateFlow.value =
            if (bluetoothService.mManager.adapter.isEnabled) BLUETOOTHStatus.DISCONNECTED else BLUETOOTHStatus.DISABLE
        if (bluetoothStateFlow.value == BLUETOOTHStatus.DISCONNECTED) {
            bluetoothService.startScan()
        }

        mUSBService = USBService(this, handler)
        wifiService = WifiService(this,handler)
        if (wifiService.wifiManager.isWifiEnabled)
            wifiStatusFlow.value = WIFIStatus.DISCONNECTED
        else
            wifiStatusFlow.value = WIFIStatus.DISABLE
        val readProtocolHandler = Handler(Looper.getMainLooper()) {
            waveDataReceiver(it.obj as ByteArray)
            true
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
        intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
        this.registerReceiver(bluetoothReceiver, intentFilter)

        val wifiIntentFilter = IntentFilter()
        wifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        this.registerReceiver(wifiReceiver, wifiIntentFilter)

        val builder = NetworkRequest.Builder()
        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        wifiService.connectManager.registerNetworkCallback(builder.build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    if (wifiStatusFlow.value != WIFIStatus.DISABLE) {
                        wifiService.getNetStatus()
                    }

                }
            })
        readProtocolCallBack {
            readProtocolHandler.obtainMessage(1, it).sendToTarget()
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var bluetoothService: BluetoothService
        lateinit var mUSBService: USBService
        lateinit var wifiService: WifiService
        val bluetoothScanStateFlow = MutableStateFlow(false)
        val isNeglectNoNameDeviceFlow = MutableStateFlow(true)
        val bluetoothStateFlow = MutableStateFlow(BLUETOOTHStatus.DISABLE)
        val unpairedBluetoothFlow = MutableStateFlow(listOf<BluetoothDevice>())
        val pairedBluetoothFlow = MutableStateFlow(listOf<BluetoothDevice>())
        val connectedBluetoothDevice = MutableStateFlow<BluetoothDevice?>(null)
        val bluetoothRecDataLengthFlow = MutableStateFlow(0)

        val wifiStatusFlow = MutableStateFlow(WIFIStatus.DISABLE)
        val wifiNameFlow = MutableStateFlow("")

        val usbStateFlow = MutableStateFlow(false)

        lateinit var appContext: Context

        val bluetoothRecDataFlow = MutableStateFlow(byteArrayOf())

    }

}

enum class WIFIStatus {
    DISABLE,
    TURNING_OFF,
    TURNING_ON,
    DISCONNECTED,
    CONNECTED_1,
    CONNECTED_2,
    CONNECTED_3,
    CONNECTED_4,
}

enum class BLUETOOTHStatus {
    DISABLE,
    TURNING_OFF,
    TURNING_ON,
    DISCONNECTING,
    CONNECTED,
    DISCONNECTED,
    CONNECTING,
}