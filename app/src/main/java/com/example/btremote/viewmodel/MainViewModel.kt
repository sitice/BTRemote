package com.example.btremote.viewmodel

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat.startActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btremote.MainActivity
import com.example.btremote.app.App
import com.example.btremote.compose.Screens
import com.example.btremote.connect.Esp32cam
import com.example.btremote.database.cmd.Cmd
import com.example.btremote.lifecycle.REQUEST_ENABLE_BT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {
    lateinit var requestLauncher: ActivityResultLauncher<Intent>
    var baseSendText by mutableStateOf("")
    var baseRecText by mutableStateOf("")
    var nowScreen by mutableStateOf(Screens.mainScreen)
    var dataScreenType by mutableStateOf(0)

    lateinit var esp32cam: Esp32cam

    var bitmap by mutableStateOf<Bitmap?>(null)


    var esp32camState by mutableStateOf(false)

    val handler = Handler(Looper.getMainLooper()) {
        when (it.what) {
            Esp32cam.REC_DATA -> {
                bitmap = BitmapFactory.decodeFile(esp32cam.dir)
            }
            Esp32cam.CONNECT_SUCCESS -> {
                esp32camState = true
            }
            Esp32cam.CONNECT_FAIL -> {
                esp32camState = false
            }
        }
        true
    }

    var isPause by mutableStateOf(false)

    val protocols = App.protocolDao.getAllProtocolFlow()

    val cmdList = App.cmdDao.getAllCmdFlow()

    fun insertCmd(cmd: Cmd) {
        viewModelScope.launch(Dispatchers.IO) {
            App.cmdDao.insert(cmd)
        }
    }

    fun deleteCmd(cmd: Cmd) {
        viewModelScope.launch(Dispatchers.IO) {
            App.cmdDao.delete(cmd)
        }
    }

    fun updateCmd(cmd: Cmd) {
        viewModelScope.launch(Dispatchers.IO) {
            App.cmdDao.update(cmd)
        }
    }

    private val BASE_SEND_TYPE = intPreferencesKey("base_send_type")
    var baseSendType = App.myDataStore.data.map {
        it[BASE_SEND_TYPE] ?: 0
    }

    fun changeBaseSendType(type: Int) {
        viewModelScope.launch {
            App.myDataStore.edit {
                it[BASE_SEND_TYPE] = type
            }
        }
    }

    private val BASE_REC_TYPE = intPreferencesKey("base_rec_type")
    var baseRecType = App.myDataStore.data.map {
        it[BASE_REC_TYPE] ?: 0
    }

    fun changeBaseRecType(type: Int) {
        viewModelScope.launch {
            App.myDataStore.edit {
                it[BASE_REC_TYPE] = type
            }
        }
    }


    private val ESP32_CAM_IP = stringPreferencesKey("esp32_cam_ip")
    var esp32camIp = App.myDataStore.data.map {
        it[ESP32_CAM_IP] ?: "192.168.92.6"
    }

    fun changeEsp32camIp(ip: String) {
        viewModelScope.launch {
            App.myDataStore.edit {
                it[ESP32_CAM_IP] = ip
            }
        }
    }

    fun openBluetooth(context: Context) {
        val bluetoothAdapter: BluetoothAdapter = App.bluetoothService.mManager.adapter
        if (!bluetoothAdapter.isEnabled) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            val activity = context as MainActivity
            requestLauncher.launch(intent)
            activity.setResult(REQUEST_ENABLE_BT)
        }
    }

    fun closeBluetooth() {
        val bluetoothAdapter: BluetoothAdapter = App.bluetoothService.mManager.adapter
        if (bluetoothAdapter.isEnabled) {
            bluetoothAdapter.disable()
        }
    }

    val stairwayToHeaven = "https://music.163.com/#/song?id=29719536"
    val theDarkSideOfTheMoon = "https://music.163.com/#/song?id=31738245"
    val strawberryFieldForever = "https://music.163.com/#/song?id=1374990343"
    val qinHuangDao = "https://music.163.com/#/song?id=386835"

    fun openUri(uri: String, context: Context) {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        intent.data = Uri.parse(uri)
        context.startActivity(intent)
    }
}