package com.example.btremote.compose.waveDisplay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btremote.R
import com.example.btremote.app.App
import com.example.btremote.database.protocol.Protocol
import com.example.btremote.viewmodel.WaveViewModel
import com.example.btremote.wave.LineChartPoints
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ProtocolList(
    colorOpen: MutableState<Boolean>,
    model: WaveViewModel = viewModel()
) {
//    val list = model.protocols.collectAsState(initial = listOf())
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(20.dp)
//            .verticalScroll(rememberScrollState()),
//    ) {
//        Text(
//            text = "协议",
//            fontSize = 20.sp,
//            fontWeight = FontWeight(700)
//        )
//        Spacer(modifier = Modifier.height(20.dp))
//        list.value.forEach { protocol ->
//            var open by remember {
//                mutableStateOf(protocol.isOpen)
//            }
//            val animateOriginal = animateFloatAsState(
//                targetValue = if (open) 180f else 90f,
//                animationSpec = tween(
//                    durationMillis = 200,
//                    delayMillis = 0,
//                    easing = LinearEasing
//                )
//            )
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text(text = "协议名字:${protocol.name}")
//                IconButton(onClick = {
//                    protocol.isOpen = !protocol.isOpen
//                    open = !open
//                    model.update(protocol)
//                }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.sanjiao),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .rotate(animateOriginal.value)
//                            .size(20.dp)
//                    )
//                }
//            }
//            Divider(modifier = Modifier.fillMaxWidth())
//            AnimatedVisibility(
//                visible = protocol.isOpen,
//            ) {
//                Column {
//                    Spacer(modifier = Modifier.height(5.dp))
//                    Text(text = String.format("发送地址:0X%x", protocol.mAddress))
//                    Spacer(modifier = Modifier.height(5.dp))
//                    Text(text = String.format("接收地址:0X%x", protocol.targetAddress))
//                    Spacer(modifier = Modifier.height(5.dp))
//                    Text(text = String.format("帧类型:0X%x", protocol.frameType))
//                    Spacer(modifier = Modifier.height(5.dp))
//                    Text(text = String.format("控制类型:0X%x", protocol.ctrlType))
//
//                    protocol.dataList.forEach { data ->
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(top = 5.dp, bottom = 5.dp),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            var check by remember {
//                                mutableStateOf(data.select)
//                            }
//                            Column {
//                                Text(text = "数据名字:${data.dataName}")
//                                Text(
//                                    text = "数据类型:${data.dataType}",
//                                    fontSize = 10.sp,
//                                    color = Color.Gray
//                                )
//                            }
//                            Column(modifier = Modifier.clickable {
//                                model.selectData = data
//                                model.selectProtocol = protocol
//                                colorOpen.value = true
//                            }) {
//                                Spacer(
//                                    modifier = Modifier
//                                        .height(25.dp)
//                                        .width(85.dp)
//                                        .background(color = Color(data.color))
//                                )
//                                Text(
//                                    text = "波形颜色(点击修改)",
//                                    fontSize = 10.sp,
//                                    color = Color.Gray
//                                )
//                            }
//
//                            Box(
//                                modifier = Modifier
//                                    .width(45.dp)
//                                    .height(42.dp)
//                            ) {
//                                Switch(
//                                    checked = check,
//                                    onCheckedChange = {
//                                        check = it
//                                        data.select = check
//                                        model.update(protocol)
//                                        controlData(check,data,protocol,model.dfProtocolDataFlow)
//                                    },
//                                    modifier = Modifier
//                                        .align(Alignment.TopEnd)
//                                        .offset(y = (-8).dp)
//                                )
//                                Text(
//                                    text = "波形显示", fontSize = 10.sp,
//                                    color = Color.Gray,
//                                    modifier = Modifier.align(Alignment.BottomEnd)
//                                )
//                            }
//
//                        }
//                    }
//                }
//            }
//
//        }
//    }
}

//private fun controlData(boolean: Boolean, data: Protocol.Data, protocol: Protocol, dfProtocolDataFlow:MutableStateFlow<List<DFProtocolData>>){
//    var selectDFProtocolData:DFProtocol.Data? = null
//    App.DFProtocolList.forEach { pro ->
//        if (protocol.name == pro.name && protocol.ctrlType == pro.ctrlType && protocol.frameType == pro.frameType) {
//            pro.dataList.forEach { prodata ->
//                if (prodata.dataName == data.dataName) {
//                    prodata.isSelect = boolean
//                    selectDFProtocolData = prodata
//                }
//            }
//        }
//    }
//
//    var same: DFProtocolData? = null
//    if (selectDFProtocolData != null){
//        dfProtocolDataFlow.value.forEach { proa ->
//            if (proa.name == data.dataName) {
//                same = proa
//            }
//        }
//        dfProtocolDataFlow.value =
//            dfProtocolDataFlow.value.toMutableList()
//                .apply {
//                    if (boolean && same == null) {
//                        add(DFProtocolData(
//                            selectDFProtocolData!!.dataName,MutableStateFlow(
//                                selectDFProtocolData!!.color),
//                            MutableStateFlow(true),
//                            selectDFProtocolData!!.dataFlow , LineChartPoints(mutableListOf()),
//                            selectDFProtocolData!!.setData
//                        ))
//                    } else if(!boolean && same != null) {
//                        remove(same)
//                    }
//                }
//    }
//}