package com.example.btremote.compose.waveDisplay

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.example.btremote.app.App
import com.example.btremote.database.protocol.Protocol
import com.example.btremote.tools.ToastUtil
import com.example.btremote.view.ColorPickerView
import com.example.btremote.viewmodel.WaveViewModel
import kotlinx.coroutines.flow.MutableStateFlow


@SuppressLint("RestrictedApi")
@Composable
fun ColorSelectDialog(iniColor: Int, onCancelClick: () -> Unit, onSureClick: (color: Int) -> Unit) {

    var value by remember {
        mutableStateOf(0f)
    }

    var nowColor by remember {
        mutableStateOf(iniColor)
    }

    var selectColor by remember {
        mutableStateOf(iniColor)
    }
    Dialog(onDismissRequest = onCancelClick) {
        Surface(
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .background(shape = RoundedCornerShape(20.dp), color = Color.White)
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(
                            Color.White,
                            RoundedCornerShape(10.dp)
                        )
                        .border(2.dp, Color.Blue, RoundedCornerShape(10.dp)), factory = { context ->
                        ColorPickerView(context).also {
                            it.setChoiceColorListener(object :
                                ColorPickerView.IChoiceColorListener {
                                override fun onChoiceColor(
                                    color: Int,
                                    a1: String?,
                                    r: Int,
                                    g: Int,
                                    b: Int,
                                    text: String?,
                                    hsl: FloatArray
                                ) {
                                    selectColor = color
                                    nowColor = ArgbEvaluator().evaluate(
                                        value,
                                        selectColor,
                                        android.graphics.Color.BLACK
                                    ) as Int
                                }
                            })
                        }
                    })
                Spacer(modifier = Modifier.height(10.dp))
                Slider(
                    value = value, onValueChange = {
                        value = it
                        nowColor = ArgbEvaluator().evaluate(
                            value,
                            selectColor,
                            android.graphics.Color.BLACK
                        ) as Int
                    }, valueRange = 0f..1f, modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .background(
                            Brush.linearGradient(listOf(Color(selectColor), Color.Black)),
                            shape = RoundedCornerShape(10.dp)
                        )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    Spacer(
                        modifier = Modifier
                            .width(50.dp)
                            .height(30.dp)
                            .background(Color(iniColor))
                    )
                    Spacer(
                        modifier = Modifier
                            .width(50.dp)
                            .height(30.dp)
                            .background(Color(nowColor))
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Button(
                        onClick = onCancelClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = null,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0x201a75ff),
                            //不可以点击的颜色
                            disabledBackgroundColor = Color.Gray
                        )
                    ) {
                        Text(text = "取消", color = Color(0xff1a75ff), fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Button(
                        onClick = {
                            onSureClick(nowColor)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = null,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0x201a75ff),
                            //不可以点击的颜色
                            disabledBackgroundColor = Color.Gray
                        )
                    ) {
                        Text(
                            text = "确认",
                            color = Color(0xff1a75ff),
                            fontSize = 18.sp
                        )
                    }
                }

            }
        }
    }
}


//private fun setDFProtocolDataColor(
//    protocol: Protocol,
//    data: Protocol.Data,
//    color: Int,
//    dfProtocolDataFlow: MutableStateFlow<List<DFProtocolData>>
//) {
//    App.DFProtocolList.forEach { defrost ->
//        if (defrost.name == protocol.name && defrost.frameType == protocol.frameType && defrost.ctrlType == protocol.ctrlType) {
//            defrost.dataList.forEach { defrostData ->
//                if (defrostData.dataName == data.dataName && defrostData.dataType == data.dataType) {
//                    defrostData.color = color
//                }
//            }
//        }
//    }
//    dfProtocolDataFlow.value.forEach { proa ->
//        if (proa.name == data.dataName) {
//            proa.color.value = color
//        }
//    }
//}