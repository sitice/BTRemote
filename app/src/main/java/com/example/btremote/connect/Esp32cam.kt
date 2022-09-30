package com.example.btremote.connect

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import com.example.btremote.lifecycle.permissionWrite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class Esp32cam(val context: Context, private val handler: Handler) {

    companion object {
        const val REC_DATA = 1
        const val CONNECT_FAIL = 2
        const val CONNECT_SUCCESS = 3
    }

    private var connectThread: ConnectThread? = null
    val dir = ContextCompat.getExternalFilesDirs(
        context,
        Environment.DIRECTORY_PICTURES
    )[0].absolutePath + File.separator + "AO1.png"

    inner class ConnectThread(private val url: String) : Thread() {
        private var bufferedInputStream: BufferedInputStream? = null
        private val outputStream: FileOutputStream? = null
        private var httpURLConnection: HttpURLConnection? = null

        @SuppressLint("SdCardPath")
        override fun run() {
            super.run()
            val downloadUrl = "http://$url:81/stream"

            if (!permissionWrite) {
                return
            }
            try {
                val url = URL(downloadUrl)
                try {
                    httpURLConnection = url.openConnection() as HttpURLConnection
                    httpURLConnection!!.requestMethod = "GET"
                    httpURLConnection!!.connectTimeout = 1000 * 5
                    httpURLConnection!!.readTimeout = 1000 * 5
                    httpURLConnection!!.doInput = true
                    httpURLConnection!!.connect()
                    if (httpURLConnection!!.responseCode == 200) {
                        handler.obtainMessage(CONNECT_SUCCESS).sendToTarget()
                        val input = httpURLConnection!!.inputStream

                        val isr = InputStreamReader(input)
                        val bufferedReader = BufferedReader(isr)

                        var line: String?

                        var len: Int

                        line = bufferedReader.readLine()
                        while (line != null) {
                            line = bufferedReader.readLine()
                            if (line.contains("Content-Type:")) {
                                line = bufferedReader.readLine()
                                len = Integer.parseInt(line.split(":")[1].trim())

                                bufferedInputStream = BufferedInputStream(input)
                                val buffer = ByteArray(len)
                                var t = 0
                                while (t < len) {
                                    t += bufferedInputStream!!.read(buffer, t, len - t)
                                }
                                bytesToImageFile(buffer)
                            }
                        }
                        handler.obtainMessage(CONNECT_FAIL).sendToTarget()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    handler.obtainMessage(CONNECT_FAIL).sendToTarget()
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                handler.obtainMessage(CONNECT_FAIL).sendToTarget()
            } finally {
                try {
                    bufferedInputStream?.close()
                    outputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        fun cancel() {
            try {
                httpURLConnection?.disconnect()
                bufferedInputStream?.close()
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    fun connect(url: String) {
        if (connectThread != null) {
            connectThread?.cancel()
            connectThread = null
        }
        connectThread = ConnectThread(url)
        connectThread?.start()
    }

    fun disconnect() {
        if (connectThread != null) {
            connectThread?.cancel()
            connectThread = null
        }
    }

    private fun bytesToImageFile(bytes: ByteArray) {
        try {
            val file = File(dir)
            val fos = FileOutputStream(file)
            fos.write(bytes, 0, bytes.size)
            fos.flush()
            fos.close()
            handler.obtainMessage(REC_DATA).sendToTarget()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}