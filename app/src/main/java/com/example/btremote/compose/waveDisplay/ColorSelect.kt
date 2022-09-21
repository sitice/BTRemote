package com.example.btremote.compose.waveDisplay

import android.annotation.SuppressLint
import android.graphics.drawable.LayerDrawable
import android.widget.SeekBar
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
import com.alibaba.fastjson.JSON
import com.example.btremote.app.App
import com.example.btremote.database.protocol.Protocol
import com.example.btremote.tools.SaveDataToLocalFile
import com.example.btremote.view.ColorPickerView
import com.example.btremote.viewmodel.WaveViewModel
import com.example.btremote.wave.LineChart


@SuppressLint("RestrictedApi")
@Composable
fun ColorSelect(
    data: Protocol.Data?,
    protocol: Protocol?,
    open: MutableState<Boolean>,
    model: WaveViewModel = viewModel()
) {
    if (open.value && data != null && protocol != null) {
        var selectColor by remember {
            mutableStateOf(data.color)
        }
        var nowColor by remember {
            mutableStateOf(data.color)
        }

        var value by remember {
            mutableStateOf(0f)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp)
                .background(shape = RoundedCornerShape(20.dp), color = Color.White)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp)
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
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {

                Spacer(
                    modifier = Modifier
                        .width(50.dp)
                        .height(30.dp)
                        .background(Color(data.color))
                )
                Spacer(
                    modifier = Modifier
                        .width(50.dp)
                        .height(30.dp)
                        .background(Color(nowColor))
                )
            }
            Row(modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 10.dp)) {
                Button(
                    onClick = {
                        open.value = false
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = null,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                        //不可以点击的颜色
                        disabledBackgroundColor = Color.Gray
                    )
                ) {
                    Text(text = "取消", color = Color.Black, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(30.dp))
                Button(
                    onClick = {
                        data.color = nowColor
                        model.update(protocol)
                        setDFProtocolDataColor(protocol,data,nowColor)
                        open.value = false
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = null,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xff1a75ff),
                        //不可以点击的颜色
                        disabledBackgroundColor = Color.Gray
                    )
                ) {
                    Text(
                        text = "确定",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }

        }
    }

}

private fun setDFProtocolDataColor(protocol:Protocol,data: Protocol.Data,color: Int)
{
    App.DFProtocolList.forEach { defrost ->
        if (defrost.name == protocol.name && defrost.frameType == protocol.frameType && defrost.ctrlType == protocol.ctrlType) {
            defrost.dataList.forEach { defrostData ->
                if (defrostData.dataName == data.dataName && defrostData.dataType == data.dataType) {
                    defrostData.color = color
                }
            }
        }
    }
}