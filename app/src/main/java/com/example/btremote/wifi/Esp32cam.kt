package com.example.btremote.wifi

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import com.example.btremote.lifecycle.permissionWrite
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class Esp32cam {

    private var bitmap: Bitmap? = null
    val bitmapFlow = MutableStateFlow(bitmap)
    private var connectThread: ConnectThread? = null

    inner class ConnectThread(private val url: String) : Thread() {
        private var bufferedInputStream: BufferedInputStream? = null
        private val outputStream: FileOutputStream? = null

        @SuppressLint("SdCardPath")
        override fun run() {
            super.run()
            val downloadUrl = "http://$url/stream"
            val savePath = "/sdcard/pic.jpg"

            val file = File(savePath)
            if (file.exists()) {
                file.delete()
            }
            if (!permissionWrite) {
                return
            }
            try {
                val url = URL(downloadUrl)
                try {
                    val httpURLConnection = url.openConnection() as HttpURLConnection
                    httpURLConnection.requestMethod = "GET"
                    httpURLConnection.connectTimeout = 1000 * 5
                    httpURLConnection.readTimeout = 1000 * 5
                    httpURLConnection.doInput = true
                    httpURLConnection.connect()

                    if (httpURLConnection.responseCode == 200) {
                        val input = httpURLConnection.inputStream

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

                                bytesToImageFile(buffer, "0A.jpg")

                                bitmapFlow.value = BitmapFactory.decodeFile("sdcard/0A.jpg")
                            }


                        }
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
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

    private fun bytesToImageFile(bytes: ByteArray, fileName: String) {
        try {
            val file = File(
                Environment.getExternalStorageDirectory().absolutePath + "/" + fileName
            )
            val fos = FileOutputStream(file)
            fos.write(bytes, 0, bytes.size)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}