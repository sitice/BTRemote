package com.example.btremote.viewmodel

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.example.btremote.MainActivity
import com.example.btremote.RemoteActivity
import com.example.btremote.app.App
import com.example.btremote.compose.Screens
import com.example.btremote.compose.mainActivity.bottomTab.Screen
import com.example.btremote.compose.waveDisplay.DFProtocol
import com.example.btremote.database.remoteWidget.RemoteWidget
import com.example.btremote.lifecycle.REQUEST_ENABLE_BT
import com.example.btremote.tools.readAssetsFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RemoteViewModel : ViewModel() {

    val widgets = App.remoteWidgetDao.getAllWidgetFlow()

    fun change(remoteWidget: RemoteWidget) {
        viewModelScope.launch(Dispatchers.IO) {
            App.remoteWidgetDao.update(remoteWidget)
        }
    }

    fun insert(remoteWidget: RemoteWidget) {
        viewModelScope.launch(Dispatchers.IO) {
            App.remoteWidgetDao.insert(remoteWidget)
        }
    }

    fun delete(remoteWidget: RemoteWidget) {
        viewModelScope.launch(Dispatchers.IO) {
            App.remoteWidgetDao.delete(remoteWidget)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            App.remoteWidgetDao.deleteAll()
        }
    }

    fun insert(list: List<RemoteWidget>) {
        viewModelScope.launch(Dispatchers.IO) {
            App.remoteWidgetDao.insert(list)
        }
    }

    fun openBluetooth(context: Context) {
        val bluetoothAdapter: BluetoothAdapter = App.bluetoothService.mManager.adapter
        if (!bluetoothAdapter.isEnabled) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            val activity = context as RemoteActivity
            val requestLauncher =
                activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        //根据广播可以知道蓝牙状态
                        //App.bluetoothStateFlow.value = true
                    }
                }
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
}