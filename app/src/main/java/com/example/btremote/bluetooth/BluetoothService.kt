package com.example.btremote.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Log
import com.example.btremote.app.App
import com.example.btremote.tools.LogUtil
import com.example.btremote.tools.ToastUtil
import com.example.btremote.viewmodel.MainViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

private const val MY_UUID = "00001101-0000-1000-8000-00805F9B34FB"

const val MESSAGE_READ = 2
private const val MESSAGE_WRITE = 3

private const val CONNECT_FAIL = 4
private const val CLOSE_CONNECT_FAIL = 4
private const val SEND_FAIL = 4
private const val REC_FAIL = 4

private const val STATE_CLOSE = 0
private const val STATE_OPEN = 1
private const val STATE_LISTEN = 2
private const val STATE_NONE = 3
private const val STATE_CONNECTING = 4
private const val STATE_CONNECTED = 5


class BluetoothService (private val context : Context,private val bluetoothDataHandler:Handler ) {


    val mManager: BluetoothManager =
        context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
    private var mConnectThread: ConnectThread? = null
    private var mConnectedThread: ConnectedThread? = null
    private var connectState = STATE_NONE


    @Synchronized
    fun startConnect(device: BluetoothDevice, secure: Boolean) {
        if (mConnectThread != null) {
            mConnectThread?.cancel()
            mConnectThread = null
        }
        if (mConnectedThread != null) {
            mConnectedThread?.cancel()
            mConnectedThread = null
        }
        mConnectThread = ConnectThread(device, secure)
        mConnectThread?.start()
    }

    @Synchronized
    fun stopConnect() {
        if (mConnectThread != null) {
            mConnectThread?.cancel()
            mConnectThread = null
        }
        if (mConnectedThread != null) {
            mConnectedThread?.cancel()
            mConnectedThread = null
        }
    }

    @SuppressLint("MissingPermission")
    fun startScan() {
        if (mManager.adapter.isEnabled) {
            if (mManager.adapter.isDiscovering) {
                mManager.adapter.cancelDiscovery()
            }
            val a = mManager.adapter.startDiscovery()
        }else{
            ToastUtil.toast(context,"蓝牙未打开")
        }
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        if (mManager.adapter.isEnabled) {
            mManager.adapter.cancelDiscovery()
        }
    }

    @OptIn(InternalCoroutinesApi::class)
    fun writeData(out: ByteArray) {
        synchronized(this) {
            if (connectState != STATE_CONNECTED) return
        }
        mConnectedThread?.write(out)
        bluetoothDataHandler.obtainMessage(MESSAGE_WRITE, out.size, -1, out).sendToTarget()
    }

    @Synchronized
    private fun connected(socket: BluetoothSocket, socketType: String) {
        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread!!.cancel()
            mConnectThread = null
        }
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread!!.cancel()
            mConnectedThread = null
        }
        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = ConnectedThread(socket, socketType)
        mConnectedThread?.start()
    }

    @SuppressLint("MissingPermission")
    private inner class ConnectThread(device: BluetoothDevice, secure: Boolean) : Thread() {
        private lateinit var socket: BluetoothSocket
        private var socketType = if (secure) "Secure" else "Insecure"

        init {
            try {
                socket = if (secure)
                    device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID))
                else
                    device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(MY_UUID))
            } catch (e: IOException) {
                LogUtil.log("ConnectThread:init", "获取socket失败")
                bluetoothDataHandler.obtainMessage(CLOSE_CONNECT_FAIL).sendToTarget()
            }
            connectState = STATE_CONNECTING
        }

        @OptIn(InternalCoroutinesApi::class)
        override fun run() {
            mManager.adapter.cancelDiscovery()
            try {
                socket.connect()
            } catch (e1: IOException) {
                LogUtil.log("ConnectThread:run", "连接socket失败")
                bluetoothDataHandler.obtainMessage(CONNECT_FAIL).sendToTarget()
                try {
                    socket.close()
                } catch (e2: IOException) {
                    LogUtil.log("ConnectThread:run", "关闭socket失败")
                    bluetoothDataHandler.obtainMessage(CLOSE_CONNECT_FAIL).sendToTarget()
                }
                return
            }

            synchronized(this@BluetoothService) { mConnectThread = null }

            connected(socket, socketType)
        }

        fun cancel() {
            try {
                socket.close()
            } catch (e2: IOException) {
                LogUtil.log("ConnectThread", "关闭socket失败")
                bluetoothDataHandler.obtainMessage(CLOSE_CONNECT_FAIL).sendToTarget()
            }
        }
    }

    private inner class ConnectedThread(private val socket: BluetoothSocket, socketType: String) : Thread() {
        private lateinit var mInStream: InputStream
        private lateinit var mOutStream: OutputStream

        init {
            LogUtil.log("socketType:", socketType)
            try {
                mInStream = socket.inputStream
                mOutStream = socket.outputStream
            } catch (e: IOException) {
                LogUtil.log("ConnectedThread:init", "获取IO失败")
                bluetoothDataHandler.obtainMessage(CLOSE_CONNECT_FAIL).sendToTarget()
            }
            connectState = STATE_CONNECTED
        }

        @SuppressLint("MissingPermission")
        override fun run() {
            val buffer = ByteArray(1024)
            var bytes: Int
            while (connectState == STATE_CONNECTED) {
                try {
                    bytes = mInStream.read(buffer)
                    LogUtil.log("data", bytes.toString())
                    bluetoothDataHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget()
                } catch (e: IOException) {
                    LogUtil.log("ConnectedThread:run", "读取数据失败")
                    bluetoothDataHandler.obtainMessage(REC_FAIL).sendToTarget()
                    try {
                        socket.close()
                    } catch (e: IOException) {
                        LogUtil.log("ConnectedThread:cancel", "关闭socket失败")
                        bluetoothDataHandler.obtainMessage(CLOSE_CONNECT_FAIL).sendToTarget()
                    }
                }
            }
        }

        fun write(buffer: ByteArray) {
            try {
                mOutStream.write(buffer)
            } catch (e: IOException) {
                LogUtil.log("ConnectedThread:write", "写入数据失败")
                bluetoothDataHandler.obtainMessage(SEND_FAIL).sendToTarget()
            }
        }

        fun cancel() {
            try {
                socket.close()
            } catch (e: IOException) {
                LogUtil.log("ConnectedThread:cancel", "关闭socket失败")
                bluetoothDataHandler.obtainMessage(CLOSE_CONNECT_FAIL).sendToTarget()
            }
        }
    }
//    @SuppressLint("MissingPermission")
//    private inner class AcceptThread(secure: Boolean) : Thread() {
//        private lateinit var mServerSocket: BluetoothServerSocket
//        private val mSocketType = if (secure) "Secure" else "Insecure"
//
//        init {
//            var tmp: BluetoothServerSocket? = null
//            try {
//                tmp = if (secure) {
//                    mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID_SECURE)
//                } else {
//                    mAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME_INSECURE, MY_UUID_INSECURE)
//                }
//            } catch (e: IOException) {
//                LogUtil.log("AcceptThread:init", "获取socket失败")
//            }
//            if (tmp != null) {
//                mServerSocket = tmp
//            }
//        }
//
//        @OptIn(InternalCoroutinesApi::class)
//        override fun run() {
//            var socket: BluetoothSocket
//            while (connectState != STATE_CONNECTED) {
//                try {
//                    socket = mServerSocket.accept()
//                } catch (e: IOException) {
//                    LogUtil.log("AcceptThread:run", "accept fail")
//                    break
//                }
//
//                if (socket != null) {
//                    synchronized(this@BluetoothService) {
//                        when (connectState) {
//                            STATE_LISTEN -> {
//                            }
//                            STATE_CONNECTING -> {
//                                // Situation normal. Start the connected thread.
//                                connected(socket, socket.remoteDevice, mSocketType)
//                            }
//                            STATE_NONE -> {
//
//                            }
//                            STATE_CONNECTED -> {
//                                // Either not ready or already connected. Terminate new socket.
//                                try {
//                                    socket.close()
//                                } catch (e: IOException) {
//
//                                }
//                            }
//
//                        }
//                    }
//                }
//            }
//        }
//
//        fun cancel() {
//            try {
//                mServerSocket.close()
//            } catch (e: IOException) {
//
//            }
//        }
//    }



}