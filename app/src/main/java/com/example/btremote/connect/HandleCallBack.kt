package com.example.btremote.connect

import android.content.Context
import android.os.Handler
import android.os.Message
import com.example.btremote.app.App
import com.example.btremote.protocol.readUartData
import com.example.btremote.tools.ToastUtil

const val MESSAGE_READ = 1
const val MESSAGE_WRITE = 2

const val CONNECT_SUCCESS = 3
const val CONNECT_FAIL = 5
const val GET_SOCKET_FAIL = 6
const val CLOSE_CONNECT_FAIL = 7
const val SEND_FAIL = 8
const val REC_FAIL = 9

const val USB_OPEN_FAIL = 10
const val USB_INIT_FAIL = 11
const val USB_OPEN_SUCCESS = 12
const val USB_READ = 13
const val USB_WRITE = 14
const val NO_PERMISSION = 15

const val TCP_CONNECT_FAIL = 16
const val TCP_CONNECT_SUCCESS = 17
const val TCP_READ = 18
const val TCP_CLOSE_FAIL = 19
const val TCP_READ_FAIL = 20
const val TCP_SEND_FAIL = 21

class HandleCallBack(private val context: Context) : Handler.Callback {
    override fun handleMessage(p0: Message): Boolean {
        when (p0.what) {
            MESSAGE_READ -> {
                App.bluetoothRecData.value = (p0.obj as ByteArray).copyOfRange(0, p0.arg1)
                App.bluetoothRecDataLength += p0.arg1
                readUartData(App.bluetoothRecData.value, 0)
            }
            MESSAGE_WRITE -> {
            }
            CONNECT_SUCCESS -> {
                App.bluetoothState = BluetoothStatus.CONNECTED
                ToastUtil.toast(context, "连接成功")
            }
            CONNECT_FAIL -> {
                App.bluetoothRecData.value = byteArrayOf()
                App.bluetoothState = BluetoothStatus.DISCONNECTED
                App.connectedBluetoothDevice = null
            }
            GET_SOCKET_FAIL -> {
                App.connectedBluetoothDevice = null
            }
            CLOSE_CONNECT_FAIL -> {
                App.connectedBluetoothDevice = null
            }
            SEND_FAIL -> {
                App.bluetoothRecData.value = byteArrayOf()
                App.bluetoothState = BluetoothStatus.DISCONNECTED
                App.connectedBluetoothDevice = null
            }
            REC_FAIL -> {
                App.bluetoothRecData.value = byteArrayOf()
                App.bluetoothState = BluetoothStatus.DISCONNECTED
                App.connectedBluetoothDevice = null
            }
            USB_OPEN_FAIL -> {
                App.usbState = false
            }
            USB_INIT_FAIL -> {
                App.usbState = false
            }
            USB_OPEN_SUCCESS -> {
                App.usbState = true
            }
            USB_READ -> {
                App.usbRecData.value = (p0.obj as ByteArray).copyOfRange(0, p0.arg1)
                App.usbRecDataLength += p0.arg1
                readUartData(App.usbRecData.value, 1)
            }
            USB_WRITE -> {
            }
            NO_PERMISSION -> {
                App.usbState = false
            }
            TCP_CONNECT_FAIL -> {
                App.wifiConnectState = false
            }
            TCP_CONNECT_SUCCESS -> {
                App.tcpRecData.value = byteArrayOf()
                App.wifiConnectState = true
                ToastUtil.toast(context, "连接成功")
            }
            TCP_READ -> {
                App.tcpRecData.value = (p0.obj as ByteArray).copyOfRange(0, p0.arg1)
                App.wifiRecDataLength += p0.arg1
                readUartData(App.tcpRecData.value, 2)
            }
            TCP_CLOSE_FAIL -> {
                App.tcpRecData.value = byteArrayOf()
                App.wifiConnectState = false
            }
            TCP_READ_FAIL -> {
                App.tcpRecData.value = byteArrayOf()
                App.wifiConnectState = false
            }
            TCP_SEND_FAIL -> {
            }
        }
        return true
    }
}