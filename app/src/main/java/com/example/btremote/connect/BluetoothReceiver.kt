package com.example.btremote.connect

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.btremote.app.App
import com.example.btremote.tools.ToastUtil

class BluetoothReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(p0: Context?, p1: Intent?) {
        when (p1?.action) {
            BluetoothDevice.ACTION_FOUND -> {
                val scanDevice: BluetoothDevice =
                    p1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) ?: return
                if (App.isNeglectNoNameDevice && scanDevice.name != null) {
                    App.unpairedBluetooth.forEach {
                        if (scanDevice.name == it.name && scanDevice.address == it.address) {
                            return
                        }
                    }
                    App.unpairedBluetooth.add(scanDevice)
                }
            }
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                when (p1.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)) {
                    BluetoothAdapter.STATE_ON -> {
                        App.bluetoothState = BluetoothStatus.DISCONNECTED
                        ToastUtil.toast(p0!!, "蓝牙打开成功")
                    }
                    BluetoothAdapter.STATE_OFF -> {
                        App.bluetoothState = BluetoothStatus.DISABLE
                        ToastUtil.toast(p0!!, "蓝牙关闭成功")
                    }
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                        App.bluetoothState = BluetoothStatus.TURNING_OFF
                        ToastUtil.toast(p0!!, "蓝牙正在关闭")
                    }
                    BluetoothAdapter.STATE_TURNING_ON -> {
                        App.bluetoothState = BluetoothStatus.TURNING_ON
                        ToastUtil.toast(p0!!, "蓝牙正在打开")
                    }
                }
            }
            BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED -> {
                when (p1.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, 0)) {
                    BluetoothAdapter.STATE_CONNECTED -> {
                        App.bluetoothState = BluetoothStatus.CONNECTED
                        ToastUtil.toast(p0!!, "连接成功")
                    }
                    BluetoothAdapter.STATE_CONNECTING -> {
                        App.bluetoothState = BluetoothStatus.CONNECTING
                        ToastUtil.toast(p0!!, "正在连接中")
                    }
                    BluetoothAdapter.STATE_DISCONNECTED -> {
                        App.bluetoothState = BluetoothStatus.DISCONNECTED
                        ToastUtil.toast(p0!!, "断开连接")
                    }
                    BluetoothAdapter.STATE_DISCONNECTING -> {
                        App.bluetoothState = BluetoothStatus.DISCONNECTING
                        ToastUtil.toast(p0!!, "断开连接中")
                    }
                }
            }

            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                ToastUtil.toast(p0!!, "蓝牙扫描结束")
                App.bluetoothScanning = false
            }

            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                ToastUtil.toast(p0!!, "蓝牙扫描中")
                App.bluetoothScanning = true
            }
            BluetoothDevice.ACTION_PAIRING_REQUEST -> {
                val btDevice: BluetoothDevice =
                    p1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) ?: return
                if (btDevice.name != null) {
                    val state: Int = p1.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
                    if (state == BluetoothDevice.BOND_NONE)
                        ToastUtil.toast(p0!!, "取消与${btDevice.name}配对成功")
                    if (state == BluetoothDevice.BOND_BONDED)
                        ToastUtil.toast(p0!!, "与${btDevice.name}配对成功")
                    if (state == BluetoothDevice.BOND_BONDING)
                        ToastUtil.toast(p0!!, "正在与${btDevice.name}配对")
                }

            }
        }
    }
}