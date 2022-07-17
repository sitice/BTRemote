package com.example.btremote.viewmodel

import androidx.lifecycle.ViewModel
import com.example.btremote.database.viewpos.ViewPosAndGesture
import kotlinx.coroutines.flow.MutableStateFlow


class RemoteViewModel :ViewModel(){
    private val viewPosAndGestures = listOf<ViewPosAndGesture>()
    val viewPosAndGesturesFlow = MutableStateFlow(viewPosAndGestures)
}