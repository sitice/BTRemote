package com.example.btremote.app

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btremote.bluetooth.BluetoothService
import com.example.btremote.bluetooth.MESSAGE_READ
import com.example.btremote.database.cmd.CMDSend
import com.example.btremote.database.cmd.CMDSendDao
import com.example.btremote.database.cmd.CMDSendDatabase
import com.example.btremote.database.viewpos.ViewPosAndGesture
import com.example.btremote.database.viewpos.ViewPosAndGestureDao
import com.example.btremote.database.viewpos.ViewPosAndGestureDatabase
import com.example.btremote.protocol.readUartData
import com.example.btremote.tools.LogUtil
import com.example.btremote.tools.ToastUtil
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.concurrent.thread


@HiltAndroidApp
class App : Application() {

    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            when (p1?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val scanDevice: BluetoothDevice = p1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) ?: return
                    if (isNeglectNoNameDeviceFlow.value && scanDevice.name != null) {
                        unpairedBluetoothFlow.value = unpairedBluetoothFlow.value.toMutableList().also {
                            it.add(scanDevice)
                        }
                    }
                    LogUtil.log("Device :", "name: ${scanDevice.name ?: "null"}  id: ${scanDevice.address}")
                    unpairedBluetoothFlow.value = unpairedBluetoothFlow.value.toSet().toMutableList()
                }
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    when (p1.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)) {
                        BluetoothAdapter.STATE_ON -> {
                            bluetoothStateFlow.value = BLUETOOTHStatus.DISCONNECTED
                            ToastUtil.toast(this@App, "蓝牙打开成功")
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
                    val btDevice: BluetoothDevice = p1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) ?: return
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

    private val handlerCallBack = Handler.Callback {
        when (it.what) {
            MESSAGE_READ -> {
                bluetoothRecData = it.obj as ByteArray
                readUartData(bluetoothRecData)
            }
        }
        true
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
        intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
        this.registerReceiver(bluetoothReceiver, intentFilter)
        val handlerThread = HandlerThread("")
        handlerThread.start()
        val handler = Handler(handlerThread.looper, handlerCallBack)
        bluetoothService = BluetoothService(this, handler)
        pairedBluetoothFlow.value = bluetoothService.mManager.adapter.bondedDevices.toList()
        bluetoothStateFlow.value = if (bluetoothService.mManager.adapter.isEnabled) BLUETOOTHStatus.DISCONNECTED else BLUETOOTHStatus.DISABLE
        if (bluetoothStateFlow.value == BLUETOOTHStatus.DISCONNECTED) {
            bluetoothService.startScan()
        }
        cmdSendDao = CMDSendDatabase.getInstance(context = appContext).CMDSendDao()
        viewPosDao = ViewPosAndGestureDatabase.getInstance(appContext).viewPosAndGestureDao()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var bluetoothService: BluetoothService
        val bluetoothScanStateFlow = MutableStateFlow(false)
        val isNeglectNoNameDeviceFlow = MutableStateFlow(true)
        val bluetoothStateFlow = MutableStateFlow(BLUETOOTHStatus.DISABLE)
        val unpairedBluetoothFlow = MutableStateFlow(listOf<BluetoothDevice>())
        val pairedBluetoothFlow = MutableStateFlow(listOf<BluetoothDevice>())

        lateinit var appContext: Context

        var bluetoothRecData = byteArrayOf()

        lateinit var cmdSendDao: CMDSendDao
        lateinit var viewPosDao: ViewPosAndGestureDao

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
    CONNECTING,
    DISCONNECTING,
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