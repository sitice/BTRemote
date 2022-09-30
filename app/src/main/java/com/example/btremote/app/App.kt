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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.alibaba.fastjson.JSON
import com.example.btremote.connect.*
import com.example.btremote.database.cmd.Cmd
import com.example.btremote.database.cmd.CmdDao
import com.example.btremote.database.cmd.CmdDatabase
import com.example.btremote.database.protocol.CustomProtocolDatabase
import com.example.btremote.database.protocol.ProtocolDao
import com.example.btremote.database.protocol.Protocol
import com.example.btremote.database.remoteWidget.RemoteWidget
import com.example.btremote.database.remoteWidget.RemoteWidgetDao
import com.example.btremote.database.remoteWidget.RemoteWidgetDatabase
import com.example.btremote.protocol.*
import com.example.btremote.tools.LogUtil
import com.example.btremote.tools.ToastUtil
import com.example.btremote.tools.readAssetsFile
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlin.concurrent.thread

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "DFRemote")

class App : Application() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
        getSerialService()
        registerReceiver()
        myDataStore = dataStore
        readDataBase()
    }

    private fun readDataBase() {
        protocolDao = CustomProtocolDatabase.getInstance(this).customProtocolDao()
        cmdDao = CmdDatabase.getInstance(this).cmdDao()
        remoteWidgetDao = RemoteWidgetDatabase.getInstance(this).remoteWidgetDao()
        thread {
            if (protocolDao.getAllProtocol().isEmpty())
            {
                val list = JSON.parseArray(
                    readAssetsFile(this.applicationContext, "protocol.json"),
                    Protocol::class.java
                )
                protocolDao.insert(list)
            }
            if (cmdDao.getAllCmd().isEmpty())
            {
                val list = JSON.parseArray(
                    readAssetsFile(this.applicationContext, "cmd.json"),
                    Cmd::class.java
                )
                cmdDao.insert(list)
            }
            if (remoteWidgetDao.getAllWidget().isEmpty())
            {
                val list = JSON.parseArray(
                    readAssetsFile(this.applicationContext, "remoteWidgets.json"),
                    RemoteWidget::class.java
                )
                remoteWidgetDao.insert(list)
            }
        }
    }

    private fun getSerialService() {
        val handler = Handler(mainLooper, HandleCallBack(this.applicationContext))
        bluetoothService = BluetoothService(this, handler)
        bluetoothState =
            if (bluetoothService.mManager.adapter.isEnabled) BluetoothStatus.DISCONNECTED else BluetoothStatus.DISABLE
        if (bluetoothState == BluetoothStatus.DISCONNECTED) {
            bluetoothService.startScan()
        }

        mUSBService = USBService(this, handler)
        wifiService = WifiService(this, handler)
        wifiState = if (wifiService.wifiManager.isWifiEnabled)
            WifiStatus.DISCONNECTED
        else
            WifiStatus.DISABLE
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
        this.registerReceiver(BluetoothReceiver(), intentFilter)
        //注册WIFI广播
        val wifiIntentFilter = IntentFilter()
        wifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        this.registerReceiver(WifiReceiver(), wifiIntentFilter)
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
                    if (wifiState != WifiStatus.DISABLE) {
                        wifiService.getNetStatus()
                    }
                }
            })
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        //获取全局的context
        lateinit var appContext: Context
        //连接服务
        lateinit var bluetoothService: BluetoothService
        lateinit var mUSBService: USBService
        lateinit var wifiService: WifiService

        //数据库操作
        lateinit var protocolDao: ProtocolDao
        lateinit var cmdDao: CmdDao
        lateinit var remoteWidgetDao: RemoteWidgetDao
        //datastore
        lateinit var myDataStore: DataStore<Preferences>
        //协议
        var originalProtocol = listOf<Protocol>()
        var customProtocol = listOf<Protocol>()
        //蓝牙状态
        var bluetoothScanning by mutableStateOf(false)
        var isNeglectNoNameDevice by mutableStateOf(true)
        var bluetoothState by mutableStateOf(BluetoothStatus.DISABLE)
        var unpairedBluetooth = mutableStateListOf<BluetoothDevice>()
        var connectedBluetoothDevice by mutableStateOf<BluetoothDevice?>(null)
        var bluetoothRecDataLength by mutableStateOf(0L)
        //wifi状态
        var wifiState by mutableStateOf(WifiStatus.DISABLE)
        var wifiConnectState by mutableStateOf(false)
        var wifiName by mutableStateOf("")
        var wifiRecDataLength by mutableStateOf(0L)

        //usb状态
        var usbState by mutableStateOf(false)
        var usbRecDataLength by mutableStateOf(0L)
        //数据流
        val bluetoothRecData = MutableStateFlow(byteArrayOf())
        val tcpRecData = MutableStateFlow(byteArrayOf())
        val usbRecData = MutableStateFlow(byteArrayOf())
    }
}


