package com.example.btremote.compose.remoteActivity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.btremote.R
import kotlin.math.*

@Composable
fun WidgetListDialog(onCancelClick: () -> Unit, onWidgetSelect: (id: Int) -> Unit) {
    Dialog(onDismissRequest = onCancelClick) {
        Surface(shape = RoundedCornerShape(20.dp), color = Color.Black) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .background(Color.Black)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "控件选择",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W700,
                    color = Color.White
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 20.dp), color = Color.White
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "摇杆   ", color = Color.White)
                        Spacer(modifier = Modifier.width(20.dp))
                        Box(contentAlignment = Alignment.Center)
                        {
                            Image(
                                painter = painterResource(id = R.drawable.xuniyaogan),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)

                            )
                            Image(
                                painter = painterResource(id = R.drawable.xuniyaogfan1),
                                contentDescription = null,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }

                    Button(
                        onClick = { onWidgetSelect(WidgetType.ROCKER) },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .width(70.dp)
                            .height(35.dp),
                        elevation = null
                    ) {
                        Text(text = "添加", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "上按键", color = Color.White)
                        Spacer(modifier = Modifier.width(20.dp))
                        Image(
                            painter = painterResource(id = R.drawable.up),
                            contentDescription = null
                        )
                    }

                    Button(
                        onClick = { onWidgetSelect(WidgetType.UPPER_BUTTON) },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .width(70.dp)
                            .height(35.dp),
                        elevation = null
                    ) {
                        Text(text = "添加", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "下按键", color = Color.White)
                        Spacer(modifier = Modifier.width(20.dp))
                        Image(
                            painter = painterResource(id = R.drawable.under),
                            contentDescription = null
                        )
                    }
                    Button(
                        onClick = { onWidgetSelect(WidgetType.LOWER_BUTTON) },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .width(70.dp)
                            .height(35.dp),
                        elevation = null
                    ) {
                        Text(text = "添加", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "左按键", color = Color.White)
                        Spacer(modifier = Modifier.width(20.dp))
                        Image(
                            painter = painterResource(id = R.drawable.left),
                            contentDescription = null
                        )
                    }
                    Button(
                        onClick = { onWidgetSelect(WidgetType.LEFT_BUTTON) },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .width(70.dp)
                            .height(35.dp),
                        elevation = null
                    ) {
                        Text(text = "添加", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "右按键", color = Color.White)
                        Spacer(modifier = Modifier.width(20.dp))
                        Image(
                            painter = painterResource(id = R.drawable.right),
                            contentDescription = null
                        )
                    }
                    Button(
                        onClick = { onWidgetSelect(WidgetType.RIGHT_BUTTON) },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .width(70.dp)
                            .height(35.dp),
                        elevation = null
                    ) {
                        Text(text = "添加", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Y        ", color = Color.White)
                        Spacer(modifier = Modifier.width(20.dp))
                        Image(
                            painter = painterResource(id = R.drawable.xboxy),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                    Button(
                        onClick = { onWidgetSelect(WidgetType.XBOX_Y) },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .width(70.dp)
                            .height(35.dp),
                        elevation = null
                    ) {
                        Text(text = "添加", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "X        ", color = Color.White)
                        Spacer(modifier = Modifier.width(20.dp))
                        Image(
                            painter = painterResource(id = R.drawable.xboxx),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                    Button(
                        onClick = { onWidgetSelect(WidgetType.XBOX_X) },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .width(70.dp)
                            .height(35.dp),
                        elevation = null
                    ) {
                        Text(text = "添加", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "A        ", color = Color.White)
                        Spacer(modifier = Modifier.width(20.dp))
                        Image(
                            painter = painterResource(id = R.drawable.xboxa),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                    Button(
                        onClick = { onWidgetSelect(WidgetType.XBOX_A) },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .width(70.dp)
                            .height(35.dp),
                        elevation = null
                    ) {
                        Text(text = "添加", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "B        ", color = Color.White)
                        Spacer(modifier = Modifier.width(20.dp))
                        Image(
                            painter = painterResource(id = R.drawable.xboxb),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                    Button(
                        onClick = { onWidgetSelect(WidgetType.XBOX_B) },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .width(70.dp)
                            .height(35.dp),
                        elevation = null
                    ) {
                        Text(text = "添加", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "按键  ", color = Color.White)
                        Spacer(modifier = Modifier.width(20.dp))
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .width(60.dp)
                                .height(30.dp),
                            shape = RoundedCornerShape(15.dp)
                        ) {}
                    }
                    Button(
                        onClick = { onWidgetSelect(WidgetType.BUTTON) },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .width(70.dp)
                            .height(35.dp),
                        elevation = null
                    ) {
                        Text(text = "添加", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "滑块    ", color = Color.White)
                        Spacer(modifier = Modifier.width(20.dp))
                        Slider(
                            value = 0f,
                            onValueChange = {},
                            onValueChangeFinished = {},
                            enabled = true,
                            valueRange = 0f..2f,
                            steps = 1,
                            colors = SliderDefaults.colors(
                                inactiveTickColor = Color.Yellow,
                                activeTickColor = Color.Yellow,
                                thumbColor = Color.White,
                                inactiveTrackColor = Color(0xff3399ff),
                                activeTrackColor = Color(0xff3399ff)
                            ), modifier = Modifier.size(50.dp)
                        )
                    }
                    Button(
                        onClick = { onWidgetSelect(WidgetType.SLIDER) },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .width(70.dp)
                            .height(35.dp),
                        elevation = null
                    ) {
                        Text(text = "添加", color = Color.White)
                    }
                }
            }
        }
    }
}