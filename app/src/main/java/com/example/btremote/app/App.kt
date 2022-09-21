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
import androidx.compose.runtime.mutableStateOf
import com.alibaba.fastjson.JSON
import com.example.btremote.bluetooth.*
import com.example.btremote.compose.waveDisplay.DFProtocol
import com.example.btremote.database.protocol.Protocol
import com.example.btremote.database.protocol.ProtocolDao
import com.example.btremote.database.protocol.ProtocolDatabase
import com.example.btremote.protocol.*
import com.example.btremote.tools.EasyDataStore
import com.example.btremote.tools.EasyDataStore.dataStore1
import com.example.btremote.tools.LogUtil
import com.example.btremote.tools.ToastUtil
import com.example.btremote.tools.readAssetsFile
import com.example.btremote.usb.*
import com.example.btremote.wifi.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


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

    private val handlerCallBack = Handler.Callback {
        when (it.what) {
            MESSAGE_READ -> {
                bluetoothRecDataFlow.value = (it.obj as ByteArray).copyOfRange(0, it.arg1)
                bluetoothRecDataLengthFlow.value += it.arg1
                readUartData(bluetoothRecDataFlow.value, 0)
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
                bluetoothRecDataFlow.value = byteArrayOf()
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
                bluetoothRecDataFlow.value = byteArrayOf()
                bluetoothStateFlow.value = BLUETOOTHStatus.DISCONNECTED
                LogUtil.log("SEND_FAIL")
            }
            REC_FAIL -> {
                bluetoothRecDataFlow.value = byteArrayOf()
                bluetoothStateFlow.value = BLUETOOTHStatus.DISCONNECTED
                LogUtil.log("SEND_FAIL")
            }
            USB_OPEN_FAIL -> {
                ToastUtil.toast(this, "打开失败")
                usbStateFlow.value = false
            }
            USB_INIT_FAIL -> {
                usbStateFlow.value = false
            }
            USB_OPEN_SUCCESS -> {
                ToastUtil.toast(this, "连接成功")
                usbStateFlow.value = true
            }
            USB_READ -> {
                usbRecDataFlow.value = (it.obj as ByteArray).copyOfRange(0, it.arg1)
                usbRecDataLengthFlow.value += it.arg1
                readUartData(usbRecDataFlow.value, 1)
                LogUtil.log("USB_READ")
            }
            USB_WRITE -> {
                LogUtil.log("USB_WRITE")
            }
            NO_PERMISSION -> {
                ToastUtil.toast(this, "未能取得权限")
                usbStateFlow.value = false
            }
            TCP_CONNECT_FAIL -> {
                LogUtil.log("TCP_CONNECT_FAIL")
                wifiConnectStateFlow.value = false
                ToastUtil.toast(this, "连接失败")
            }
            TCP_CONNECT_SUCCESS -> {
                LogUtil.log("TCP_CONNECT_SUCCESS")
                wifiConnectStateFlow.value = true
                ToastUtil.toast(this, "连接成功")
            }
            TCP_READ -> {
                wifiRecDataFlow.value = (it.obj as ByteArray).copyOfRange(0, it.arg1)
                wifiRecDataLengthFlow.value += it.arg1
                readUartData(wifiRecDataFlow.value, 2)
                LogUtil.log("TCP_READ")
            }
            TCP_CLOSE_FAIL -> {
                wifiConnectStateFlow.value = false
                LogUtil.log("TCP_CLOSE_FAIL")
            }
            TCP_READ_FAIL -> {
                wifiConnectStateFlow.value = false
                LogUtil.log("TCP_READ_FAIL")
            }
            TCP_SEND_FAIL -> {
                LogUtil.log("TCP_SEND_FAIL")
            }
        }
        true
    }


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
        getSerialService()
        registerReceiver()
        readDatabase()
        //读取发送接收设置
        EasyDataStore.readSendRecType(this)
    }

    private fun getSerialService() {
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
        wifiService = WifiService(this, handler)
        if (wifiService.wifiManager.isWifiEnabled)
            wifiStatusFlow.value = WIFIStatus.DISCONNECTED
        else
            wifiStatusFlow.value = WIFIStatus.DISABLE
    }

    private fun registerReceiver() {
        //注册蓝牙广播
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
        intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
        this.registerReceiver(bluetoothReceiver, intentFilter)
        //注册WIFI广播
        val wifiIntentFilter = IntentFilter()
        wifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        this.registerReceiver(wifiReceiver, wifiIntentFilter)
        //注册网络广播
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
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun readDatabase() {
        //读取数据库
        protocolDao = ProtocolDatabase.getInstance(this).protocolDao()
        GlobalScope.launch(Dispatchers.IO) {
            if (protocolDao.getAllProtocol().isEmpty()) {
                val json = readAssetsFile(this@App, "protocol.json")
                protocolDao.insert(JSON.parseArray(json, Protocol::class.java))
            }
            val data = protocolDao.getAllProtocol()
            data.forEach {
                val list = mutableListOf<DFProtocol.Data>()
                it.dataList.forEach { protocolData: Protocol.Data ->
                    list.add(
                        DFProtocol.Data(
                            protocolData.dataName,
                            protocolData.dataType,
                            protocolData.color,
                            protocolData.select,
                            MutableStateFlow(null)
                        )
                    )
                }
                val dfProtocol = DFProtocol(
                    it.name,
                    it.mAddress,
                    it.targetAddress,
                    it.frameType,
                    it.ctrlType,
                    it.length.toByte(),
                    list
                )
                DFProtocolList.add(dfProtocol)
            }
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var appContext: Context
        lateinit var bluetoothService: BluetoothService
        lateinit var mUSBService: USBService
        lateinit var wifiService: WifiService

        lateinit var protocolDao: ProtocolDao

        val bluetoothScanStateFlow = MutableStateFlow(false)
        val isNeglectNoNameDeviceFlow = MutableStateFlow(true)
        val bluetoothStateFlow = MutableStateFlow(BLUETOOTHStatus.DISABLE)
        val unpairedBluetoothFlow = MutableStateFlow(listOf<BluetoothDevice>())
        val pairedBluetoothFlow = MutableStateFlow(listOf<BluetoothDevice>())
        val connectedBluetoothDevice = MutableStateFlow<BluetoothDevice?>(null)
        val bluetoothRecDataLengthFlow = MutableStateFlow(0)
        val bluetoothRecDataFlow = MutableStateFlow(byteArrayOf())

        val wifiStatusFlow = MutableStateFlow(WIFIStatus.DISABLE)
        val wifiConnectStateFlow = MutableStateFlow(false)
        val wifiNameFlow = MutableStateFlow("")
        val wifiRecDataLengthFlow = MutableStateFlow(0)
        val wifiRecDataFlow = MutableStateFlow(byteArrayOf())

        val usbStateFlow = MutableStateFlow(true)
        val usbRecDataLengthFlow = MutableStateFlow(0)
        val usbRecDataFlow = MutableStateFlow(byteArrayOf())

        val baseSendType = MutableStateFlow("Bluetooth")
        val baseRecType = MutableStateFlow("Bluetooth")
        val advanceSendType = MutableStateFlow("Bluetooth")
        val advanceRecType = MutableStateFlow("Bluetooth")
        val remoteSendType = MutableStateFlow("Bluetooth")
        val remoteRecType = MutableStateFlow("Bluetooth")
        val waveSendType = MutableStateFlow("Bluetooth")
        val waveRecType = MutableStateFlow("Bluetooth")

        var DFProtocolList = mutableListOf<DFProtocol>()
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

enum class USBStatus {
    DISABLE,
    TURNING_OFF,
    TURNING_ON,
    DISCONNECTING,
    CONNECTED,
    DISCONNECTED,
    CONNECTING,
}
