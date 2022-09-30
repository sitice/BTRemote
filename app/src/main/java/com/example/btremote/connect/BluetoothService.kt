package com.example.btremote.connect

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.os.*
import androidx.annotation.RequiresApi
import com.example.btremote.app.App
import com.example.btremote.tools.ToastUtil
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

private const val MY_UUID = "00001101-0000-1000-8000-00805F9B34FB"

private const val STATE_NONE = 1
private const val STATE_CONNECTING = 2
private const val STATE_CONNECTED = 3


class BluetoothService(private val context: Context, private val bluetoothDataHandler: Handler) {
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

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @SuppressLint("MissingPermission")
    fun startScan() {
        if (mManager.adapter.isEnabled) {
            if (mManager.adapter.isDiscovering) {
                mManager.adapter.cancelDiscovery()
            }
            val a = mManager.adapter.startDiscovery()
        } else {
            ToastUtil.toast(context, "蓝牙未打开")
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
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
    private fun connected(socket: BluetoothSocket, socketType: String,device: BluetoothDevice) {
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
        mConnectedThread = ConnectedThread(socket, socketType,device)
        mConnectedThread?.start()
    }

    @SuppressLint("MissingPermission")
    private inner class ConnectThread(val device: BluetoothDevice, secure: Boolean) : Thread() {
        private lateinit var socket: BluetoothSocket
        private var socketType = if (secure) "Secure" else "Insecure"

        init {
            try {
                socket = if (secure)
                    device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID))
                else
                    device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(MY_UUID))
            } catch (e: IOException) {
                bluetoothDataHandler.obtainMessage(GET_SOCKET_FAIL).sendToTarget()
            }
            connectState = STATE_CONNECTING
        }

        @OptIn(InternalCoroutinesApi::class)
        override fun run() {
            mManager.adapter.cancelDiscovery()
            try {
                socket.connect()
            } catch (e1: IOException) {
                connectState = STATE_NONE
                bluetoothDataHandler.obtainMessage(CONNECT_FAIL).sendToTarget()
                try {
                    socket.close()
                } catch (e2: IOException) {
                    bluetoothDataHandler.obtainMessage(CLOSE_CONNECT_FAIL).sendToTarget()
                }
                return
            }

            synchronized(this@BluetoothService) { mConnectThread = null }

            connected(socket, socketType,device)
        }

        fun cancel() {
            try {
                socket.close()
            } catch (e2: IOException) {
                bluetoothDataHandler.obtainMessage(CLOSE_CONNECT_FAIL).sendToTarget()
            }
        }
    }

    private inner class ConnectedThread(private val socket: BluetoothSocket, socketType: String,device: BluetoothDevice) : Thread() {
        private lateinit var mInStream: InputStream
        private lateinit var mOutStream: OutputStream

        init {
            try {
                mInStream = socket.inputStream
                mOutStream = socket.outputStream
            } catch (e: IOException) {
                bluetoothDataHandler.obtainMessage(CLOSE_CONNECT_FAIL).sendToTarget()
            }
            connectState = STATE_CONNECTED
            bluetoothDataHandler.obtainMessage(CONNECT_SUCCESS).sendToTarget()
            App.connectedBluetoothDevice = device
        }

        @SuppressLint("MissingPermission")
        override fun run() {
            val buffer = ByteArray(1024)
            var bytes: Int
            while (connectState == STATE_CONNECTED) {
                try {
                    bytes = mInStream.read(buffer)
                    bluetoothDataHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget()
                } catch (e: IOException) {
                    try {
                        socket.close()
                    } catch (e: IOException) {
                        bluetoothDataHandler.obtainMessage(CLOSE_CONNECT_FAIL).sendToTarget()
                    }
                    connectState = STATE_NONE
                    bluetoothDataHandler.obtainMessage(REC_FAIL).sendToTarget()
                }
            }
        }

        fun write(buffer: ByteArray) {
            try {
                mOutStream.write(buffer)
            } catch (e: IOException) {
                bluetoothDataHandler.obtainMessage(SEND_FAIL).sendToTarget()
            }
        }

        fun cancel() {
            try {
                socket.close()
            } catch (e: IOException) {
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