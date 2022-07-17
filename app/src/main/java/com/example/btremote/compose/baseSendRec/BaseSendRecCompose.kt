package com.example.btremote.compose.baseSendRec

import android.media.Image
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.UUIDUtil
import com.example.btremote.R
import com.example.btremote.app.App
import com.example.btremote.app.App.Companion.cmdSendDao
import com.example.btremote.database.cmd.CMDSend
import com.example.btremote.database.cmd.CMDSendDatabase
import com.example.btremote.tools.EasyDataStore
import com.example.btremote.tools.LogUtil
import com.example.btremote.tools.WindowManager.bytesToHexString
import com.example.btremote.ui.theme.roundedCorner10dp
import com.example.btremote.viewmodel.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.concurrent.thread
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.suspendCoroutine
import kotlin.math.round

const val baseRecSendScreen = 0
const val moreRecSendScreen = 1
const val advanceRecSendScreen = 2

@Composable
fun BaseSendRec() {

    var nowScreen by remember {
        mutableStateOf(baseRecSendScreen)
    }

    Column {
        Card(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp), shape = RoundedCornerShape(10.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .background(Color.White), horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = { nowScreen = baseRecSendScreen },
                        modifier = Modifier
                            .width(60.dp)
                            .height(35.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (nowScreen == baseRecSendScreen) Color(0xffffdacc) else Color.White,
                            contentColor = Color.Black
                        ),
                        elevation = null
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.recandsend),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Black
                        )
                    }
                    Text(text = "基本收发", fontSize = 11.sp)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = {
                            nowScreen = moreRecSendScreen

                        },
                        modifier = Modifier
                            .width(60.dp)
                            .height(35.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (nowScreen == moreRecSendScreen) Color(0xffffdacc) else Color.White,
                            contentColor = Color.Black
                        ),
                        elevation = null
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.advancerec),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Black
                        )
                    }
                    Text(text = "高级收发", fontSize = 11.sp)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = {
                            nowScreen = advanceRecSendScreen

                        },
                        modifier = Modifier
                            .width(60.dp)
                            .height(35.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (nowScreen == advanceRecSendScreen) Color(0xffffdacc) else Color.White,
                            contentColor = Color.Black
                        ),
                        elevation = null
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.protolrec),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Black
                        )
                    }
                    Text(text = "协议解析", fontSize = 11.sp)
                }
            }

        }
        if (nowScreen == baseRecSendScreen)
            BaseRecScreen()
        else if (nowScreen == moreRecSendScreen)
            MoreRecScreen()
        else
            AdvanceRecScreen()
    }
}

@Composable
fun BaseRecScreen() {
    val model: MainViewModel = viewModel()
    val rec = model.recDataFlow.collectAsState()
    val send = model.sendStringFlow.collectAsState()
    val recType = model.recTypeFlow.collectAsState()
    val sendType = model.sendTypeFlow.collectAsState()
    // 定义一个可观测的text，用来在TextField中展示
    var text by remember {
        mutableStateOf("")
    }
    Column {
        Row(
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { model.sendTypeFlow.value = if (model.sendTypeFlow.value == HEX_DATA) STRING_DATA else HEX_DATA }, modifier = Modifier
                    .width(80.dp)
                    .height(40.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xA05e71f6),
                    contentColor = Color.Black
                ),
                elevation = null
            ) {
                Icon(
                    painter = painterResource(id = if (sendType.value == STRING_DATA) R.drawable.s else R.drawable.h),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
            TextField(
                // 指定下划线颜色
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent, // 有焦点时的颜色，透明
                    unfocusedIndicatorColor = Color.Transparent, // 无焦点时的颜色，绿色
                    errorIndicatorColor = Color.Red, // 错误时的颜色，红色
                    disabledIndicatorColor = Color.Gray // 不可用时的颜色，灰色
                ),
                isError = false,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = roundedCorner10dp,
                value = text, // 显示文本
                onValueChange = { text = it }, // 文字改变时，就赋值给text
                label = { Text(text = "Input") }, // label是Input
                trailingIcon = @Composable {
                    Image(
                        painter = painterResource(id = R.drawable.send), // 搜索图标
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp)
                            .clickable { model.sendStringFlow.value += text }) // 给图标添加点击事件，点击就吐司提示内容
                },
                leadingIcon = @Composable {
                    Image(imageVector = Icons.Filled.Clear, // 清除图标
                        contentDescription = null,
                        modifier = Modifier.clickable { text = "" }) // 给图标添加点击事件，点击就清空text
                },
                // placeholder = @Composable { Text(text = "This is placeholder") }, // 不输入内容时的占位符
            )
        }


        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .padding(start = 30.dp, end = 30.dp, top = 10.dp)
                .border(2.dp, color = Color.Blue, shape = roundedCorner10dp), shape = roundedCorner10dp
        )
        {
            Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
                Text(text = "Receive", modifier = Modifier.padding(10.dp), fontSize = 12.sp)
                Divider(modifier = Modifier.fillMaxWidth())
                (if (recType.value == STRING_DATA) String(rec.value, StandardCharsets.UTF_8) else bytesToHexString(rec.value))?.let {
                    Text(
                        text = it, modifier = Modifier
                            .padding(top = 10.dp)
                            .verticalScroll(rememberScrollState())
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { model.recTypeFlow.value = if (model.recTypeFlow.value == HEX_DATA) STRING_DATA else HEX_DATA }, modifier = Modifier
                    .padding(start = 40.dp, top = 10.dp, bottom = 10.dp, end = 40.dp)
                    .width(80.dp)
                    .height(40.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xA05e71f6),
                    contentColor = Color.Black
                ),
                elevation = null
            ) {
                Icon(
                    painter = painterResource(id = if (recType.value == STRING_DATA) R.drawable.s else R.drawable.h),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
            Button(
                onClick = { model.recDataFlow.value = byteArrayOf() }, modifier = Modifier
                    .padding(start = 40.dp, top = 10.dp, bottom = 10.dp, end = 40.dp)
                    .width(80.dp)
                    .height(40.dp), shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xa0fc5531),
                    contentColor = Color.Black
                ),
                elevation = null
            ) {
                Image(painter = painterResource(id = R.drawable.round_delete_forever_black_48dp), contentDescription = null)
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun MoreRecScreen() {
    val model: MainViewModel = viewModel()
    val rec = model.recDataFlow.collectAsState()
    val send = model.sendStringFlow.collectAsState()
    val recType = model.recTypeFlow.collectAsState()
    val sendType = model.sendTypeFlow.collectAsState()

    val cmdList = remember {
        mutableStateOf(listOf<CMDSend>())
    }

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "多条发送")
                Text(text = "Tip:右滑可删除", fontSize = 9.sp)
            }
            IconButton(onClick = {
                val data = CMDSend()
                cmdList.value = cmdList.value.toMutableList().also {
                    data.name = UUID.randomUUID().toString()
                    it.add(data)
                }
                thread {
                    cmdSendDao.insert(data)
                }
            }) {
                Icon(painter = painterResource(id = R.drawable.baseline_playlist_add_black_24dp), contentDescription = null)
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(cmdList.value, key = { item: CMDSend -> item.name }) { item ->
                // 侧滑删除所需State
                val dismissState = rememberDismissState()
                // 按指定方向触发删除后的回调，在此处变更具体数据
                if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                    cmdList.value = cmdList.value.toMutableList().also {
                        it.remove(item)
                    }
                    thread {
                        cmdSendDao.delete(item)
                    }
                }
                if (cmdList.value.size > 1) {
                    SwipeToDismiss(
                        state = dismissState,
                        // animateItemPlacement() 此修饰符便添加了动画
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement(),
                        // 下面这个参数为触发滑动删除的移动阈值
                        dismissThresholds = { direction ->
                            FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
                        },
                        // 允许滑动删除的方向
                        directions = setOf(DismissDirection.StartToEnd),
                        // "背景 "，即原来显示的内容被划走一部分时显示什么
                        background = {
                            /*保证观看体验，省略此处内容*/
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { model.sendTypeFlow.value = if (model.sendTypeFlow.value == HEX_DATA) STRING_DATA else HEX_DATA },
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(40.dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xA05e71f6),
                                    contentColor = Color.Black
                                ),
                                elevation = null
                            ) {
                                Icon(
                                    painter = painterResource(id = if (sendType.value == STRING_DATA) R.drawable.s else R.drawable.h),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.Black
                                )
                            }
                            TextField(
                                // 指定下划线颜色
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = Color.Transparent, // 有焦点时的颜色，透明
                                    unfocusedIndicatorColor = Color.Transparent, // 无焦点时的颜色，绿色
                                    errorIndicatorColor = Color.Red, // 错误时的颜色，红色
                                    disabledIndicatorColor = Color.Gray // 不可用时的颜色，灰色
                                ),
                                isError = false,
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = roundedCorner10dp,
                                value = item.text ?: "", // 显示文本
                                onValueChange = {
                                    item.text = it
                                }, // 文字改变时，就赋值给text
                                label = { Text(text = item.name) }, // label是Input
                                trailingIcon = @Composable {
                                    Image(
                                        painter = painterResource(id = R.drawable.send), // 搜索图标
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(25.dp)
                                            .clickable { model.sendStringFlow.value += item.text }) // 给图标添加点击事件，点击就吐司提示内容
                                },
                                leadingIcon = @Composable {
                                    Image(imageVector = Icons.Filled.Clear, // 清除图标
                                        contentDescription = null,
                                        modifier = Modifier.clickable { item.text = "" }) // 给图标添加点击事件，点击就清空text
                                },
                                // placeholder = @Composable { Text(text = "This is placeholder") }, // 不输入内容时的占位符
                            )
                        }
                    }
                }else{
                    Row(
                        modifier = Modifier
                            .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { model.sendTypeFlow.value = if (model.sendTypeFlow.value == HEX_DATA) STRING_DATA else HEX_DATA },
                            modifier = Modifier
                                .width(80.dp)
                                .height(40.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xA05e71f6),
                                contentColor = Color.Black
                            ),
                            elevation = null
                        ) {
                            Icon(
                                painter = painterResource(id = if (sendType.value == STRING_DATA) R.drawable.s else R.drawable.h),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color.Black
                            )
                        }
                        TextField(
                            // 指定下划线颜色
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent, // 有焦点时的颜色，透明
                                unfocusedIndicatorColor = Color.Transparent, // 无焦点时的颜色，绿色
                                errorIndicatorColor = Color.Red, // 错误时的颜色，红色
                                disabledIndicatorColor = Color.Gray // 不可用时的颜色，灰色
                            ),
                            isError = false,
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = roundedCorner10dp,
                            value = item.text ?: "", // 显示文本
                            onValueChange = {
                                item.text = it
                            }, // 文字改变时，就赋值给text
                            label = { Text(text = item.name) }, // label是Input
                            trailingIcon = @Composable {
                                Image(
                                    painter = painterResource(id = R.drawable.send), // 搜索图标
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .clickable { model.sendStringFlow.value += item.text }) // 给图标添加点击事件，点击就吐司提示内容
                            },
                            leadingIcon = @Composable {
                                Image(imageVector = Icons.Filled.Clear, // 清除图标
                                    contentDescription = null,
                                    modifier = Modifier.clickable { item.text = "" }) // 给图标添加点击事件，点击就清空text
                            },
                            // placeholder = @Composable { Text(text = "This is placeholder") }, // 不输入内容时的占位符
                        )
                    }
                }
            }
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .padding(start = 30.dp, end = 30.dp, top = 10.dp)
                .border(2.dp, color = Color.Blue, shape = roundedCorner10dp), shape = roundedCorner10dp
        )
        {
            Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
                Text(text = "Receive", modifier = Modifier.padding(10.dp), fontSize = 12.sp)
                Divider(modifier = Modifier.fillMaxWidth())
                (if (recType.value == STRING_DATA) String(rec.value, StandardCharsets.UTF_8) else bytesToHexString(rec.value))?.let {
                    Text(
                        text = it, modifier = Modifier
                            .padding(top = 10.dp)
                            .verticalScroll(rememberScrollState())
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { model.recTypeFlow.value = if (model.recTypeFlow.value == HEX_DATA) STRING_DATA else HEX_DATA }, modifier = Modifier
                    .padding(start = 40.dp, top = 10.dp, bottom = 10.dp, end = 40.dp)
                    .width(80.dp)
                    .height(40.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xA05e71f6),
                    contentColor = Color.Black
                ),
                elevation = null
            ) {
                Icon(
                    painter = painterResource(id = if (recType.value == STRING_DATA) R.drawable.s else R.drawable.h),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
            Button(
                onClick = { model.recDataFlow.value = byteArrayOf() }, modifier = Modifier
                    .padding(start = 40.dp, top = 10.dp, bottom = 10.dp, end = 40.dp)
                    .width(80.dp)
                    .height(40.dp), shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xa0fc5531),
                    contentColor = Color.Black
                ),
                elevation = null
            ) {
                Image(painter = painterResource(id = R.drawable.round_delete_forever_black_48dp), contentDescription = null)
            }
        }

    }
    thread {
        val data = cmdSendDao.getAllCMDSend().toMutableList()
        if (data.isEmpty()) {
            repeat(3) { time ->
                data.toMutableList().also {
                    val i = CMDSend()
                    i.name = "CMD$time"
                    it.add(i)
                    cmdSendDao.insert(i)
                }
            }
        }
        cmdList.value = data
    }
}

@Composable
fun AdvanceRecScreen() {
    val model: MainViewModel = viewModel()
    val rec = model.recDataFlow.collectAsState()
    val send = model.sendStringFlow.collectAsState()
    val recType = model.recTypeFlow.collectAsState()
    val sendType = model.sendTypeFlow.collectAsState()
    // 定义一个可观测的text，用来在TextField中展示
    var text by remember {
        mutableStateOf("")
    }
    Column {
        Row(
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { model.sendTypeFlow.value = if (model.sendTypeFlow.value == HEX_DATA) STRING_DATA else HEX_DATA }, modifier = Modifier
                    .width(80.dp)
                    .height(40.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xA05e71f6),
                    contentColor = Color.Black
                ),
                elevation = null
            ) {
                Icon(
                    painter = painterResource(id = if (sendType.value == STRING_DATA) R.drawable.s else R.drawable.h),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
            TextField(
                // 指定下划线颜色
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent, // 有焦点时的颜色，透明
                    unfocusedIndicatorColor = Color.Transparent, // 无焦点时的颜色，绿色
                    errorIndicatorColor = Color.Red, // 错误时的颜色，红色
                    disabledIndicatorColor = Color.Gray // 不可用时的颜色，灰色
                ),
                isError = false,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = roundedCorner10dp,
                value = text, // 显示文本
                onValueChange = { text = it }, // 文字改变时，就赋值给text
                label = { Text(text = "Input") }, // label是Input
                trailingIcon = @Composable {
                    Image(
                        painter = painterResource(id = R.drawable.send), // 搜索图标
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp)
                            .clickable { model.sendStringFlow.value += text }) // 给图标添加点击事件，点击就吐司提示内容
                },
                leadingIcon = @Composable {
                    Image(imageVector = Icons.Filled.Clear, // 清除图标
                        contentDescription = null,
                        modifier = Modifier.clickable { text = "" }) // 给图标添加点击事件，点击就清空text
                },
                // placeholder = @Composable { Text(text = "This is placeholder") }, // 不输入内容时的占位符
            )
        }


        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                .border(2.dp, color = Color.Blue, shape = roundedCorner10dp), shape = roundedCorner10dp
        )
        {
            Column(modifier = Modifier.padding(start = 2.dp, end = 2.dp)) {
                Text(text = "Receive", modifier = Modifier.padding(10.dp), fontSize = 12.sp)
                Divider(modifier = Modifier.fillMaxWidth())
                Row(modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp, end = 10.dp)) {
                    Text(text = "帧头", fontSize = 9.sp, modifier = Modifier.padding(end = 5.dp))
                    Text(text = "#data1", fontSize = 9.sp, modifier = Modifier.padding(end = 5.dp))
                    Text(text = "#data2", fontSize = 9.sp, modifier = Modifier.padding(end = 5.dp))
                    Text(text = "#data3", fontSize = 9.sp, modifier = Modifier.padding(end = 5.dp))
                    Text(text = "#data4", fontSize = 9.sp, modifier = Modifier.padding(end = 5.dp))
                    Text(text = "#data5", fontSize = 9.sp, modifier = Modifier.padding(end = 5.dp))
                    Text(text = "#data6", fontSize = 9.sp, modifier = Modifier.padding(end = 5.dp))
                    Text(text = "#data7", fontSize = 9.sp, modifier = Modifier.padding(end = 5.dp))
                    Text(text = "#data8", fontSize = 9.sp, modifier = Modifier.padding(end = 5.dp))
                    Text(text = "帧尾", fontSize = 9.sp, modifier = Modifier.padding(end = 5.dp))
                    Text(text = "acc", fontSize = 9.sp)
                }
                (if (recType.value == STRING_DATA) String(rec.value, StandardCharsets.UTF_8) else bytesToHexString(rec.value))?.let {
                    Text(
                        text = it, modifier = Modifier
                            .padding(top = 10.dp)
                            .verticalScroll(rememberScrollState())
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { model.recTypeFlow.value = if (model.recTypeFlow.value == HEX_DATA) STRING_DATA else HEX_DATA }, modifier = Modifier
                    .padding(start = 40.dp, top = 10.dp, bottom = 10.dp, end = 40.dp)
                    .width(80.dp)
                    .height(40.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xA05e71f6),
                    contentColor = Color.Black
                ),
                elevation = null
            ) {
                Icon(
                    painter = painterResource(id = if (recType.value == STRING_DATA) R.drawable.s else R.drawable.h),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
            Button(
                onClick = { model.recDataFlow.value = byteArrayOf() }, modifier = Modifier
                    .padding(start = 40.dp, top = 10.dp, bottom = 10.dp, end = 40.dp)
                    .width(80.dp)
                    .height(40.dp), shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xa0fc5531),
                    contentColor = Color.Black
                ),
                elevation = null
            ) {
                Image(painter = painterResource(id = R.drawable.round_delete_forever_black_48dp), contentDescription = null)
            }
        }
    }

}