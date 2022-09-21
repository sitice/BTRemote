package com.example.btremote.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.btremote.app.App
import com.example.btremote.database.protocol.Protocol
import com.example.btremote.wave.LineChartPoints
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class WaveViewModel: ViewModel() {

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
}