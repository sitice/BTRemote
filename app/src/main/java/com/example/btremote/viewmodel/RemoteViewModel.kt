package com.example.btremote.viewmodel

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.example.btremote.app.App
import com.example.btremote.compose.waveDisplay.DFProtocol
import kotlinx.coroutines.flow.MutableStateFlow

class RemoteViewModel : ViewModel() {
    lateinit var requestLauncher: ActivityResultLauncher<Intent>

    lateinit var distanceXFlow: MutableStateFlow<String?>
    lateinit var distanceYFlow: MutableStateFlow<String?>
    lateinit var yawFlow: MutableStateFlow<String?>
    lateinit var voltageFlow: MutableStateFlow<String?>

    fun getChariotFlow() {
        App.DFProtocolList.forEach { dfProtocol ->
            if (dfProtocol.name == "dfChariotRemoteVal") {
                distanceXFlow = dfProtocol.dataList[0].dataFlow
                distanceYFlow = dfProtocol.dataList[1].dataFlow
                yawFlow = dfProtocol.dataList[2].dataFlow
                voltageFlow = dfProtocol.dataList[3].dataFlow
            }
        }
    }
}