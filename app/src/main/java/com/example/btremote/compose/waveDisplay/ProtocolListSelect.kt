package com.example.btremote.compose.waveDisplay

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btremote.R
import com.example.btremote.app.App
import com.example.btremote.database.protocol.Protocol
import com.example.btremote.viewmodel.WaveViewModel
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun ProtocolListSelect(
    list: State<List<Protocol>>,
    open: MutableState<Boolean>,
) {

    val colorOpen = remember {
        mutableStateOf(false)
    }

    val selectData: Protocol.Data? = null
    val selectProtocol: Protocol? = null
    val selectDataState = remember {
        mutableStateOf(selectData)
    }
    val selectProtocolState = remember {
        mutableStateOf(selectProtocol)
    }

    if (open.value) {
        Dialog(onDismissRequest = { open.value = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 30.dp)
            ) {
                if (!colorOpen.value)
                    ProtocolList(list, colorOpen, selectDataState, selectProtocolState)
                else
                    ColorSelect(
                        data = selectDataState.value,
                        protocol = selectProtocolState.value,
                        open = colorOpen
                    )
            }
        }
    }

}


