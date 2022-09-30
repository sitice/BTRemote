package com.example.btremote.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btremote.app.App
import com.example.btremote.compose.waveDisplay.DFProtocolData
import com.example.btremote.database.protocol.Protocol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class WaveViewModel : ViewModel() {

    fun insert(list: List<Protocol>) {
        viewModelScope.launch(Dispatchers.IO) {
            App.protocolDao.insert(list)
        }
    }

    fun update(vararg i: Protocol) {
        viewModelScope.launch(Dispatchers.IO) {
            App.protocolDao.update(*i)
        }
    }

    val protocols = App.protocolDao.getAllProtocolFlow()

    val dfProtocolDataFlow = MutableStateFlow(listOf<DFProtocolData>())

    fun setWaveVisible(wave: DFProtocolData, visible: Boolean) {
        wave.visible.value = visible
    }

    var selectData: Protocol.Data? = null
    var selectProtocol: Protocol? = null
}