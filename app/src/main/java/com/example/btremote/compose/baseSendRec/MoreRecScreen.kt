package com.example.btremote.compose.baseSendRec

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.alibaba.fastjson.JSON
import com.example.btremote.R
import com.example.btremote.tools.SaveDataToLocalFile
import com.example.btremote.tools.ToastUtil
import com.example.btremote.ui.theme.roundedCorner10dp

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun MoreRecScreen(context: Context = LocalContext.current) {

    var array = ArrayList<Cmd>()
    val data = SaveDataToLocalFile.load(context, "CMDSend")
    if (data == null) {
        repeat(3) {
            array.add(Cmd("cmd$it", "", "hex"))
        }
        SaveDataToLocalFile.save(context, "CMDSend", JSON.toJSONString(array))
    } else {
        array = JSON.parseArray(data, Cmd::class.java) as ArrayList<Cmd>
    }


    val cmdList = remember {
        mutableStateOf(array.toList())
    }
    var openDialog by remember {
        mutableStateOf(false)
    }

    if (openDialog)
        Dialog(
            onDismissRequest = { openDialog = false }
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(20.dp)
                        .fillMaxWidth()
                        .height(180.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var text by remember {
                        mutableStateOf("")
                    }

                    Text(text = "请输入指令名字", fontWeight = FontWeight(700), fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(30.dp))
                    TextField(
                        textStyle = TextStyle(fontSize = 11.sp),
                        // 指定下划线颜色
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent, // 有焦点时的颜色，透明
                            unfocusedIndicatorColor = Color.Transparent, // 无焦点时的颜色，绿色
                            errorIndicatorColor = Color.Red, // 错误时的颜色，红色
                            disabledIndicatorColor = Color.Gray // 不可用时的颜色，灰色
                        ),
                        isError = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = roundedCorner10dp,
                        value = text, // 显示文本
                        onValueChange = {
                            text = it
                        }, // 文字改变时，就赋值给text
                        label = { Text(text = "名字") }, // label是Input
                        leadingIcon = @Composable {
                            Image(imageVector = Icons.Filled.Clear, // 清除图标
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    text = ""
                                }) // 给图标添加点击事件，点击就清空text

                        },
                        placeholder = @Composable {
                            Text(
                                text = "请输入名字",
                                fontSize = 11.sp
                            )
                        }) // 不输入内容时的占位符
                    Spacer(modifier = Modifier.height(30.dp))
                    Row {
                        Button(
                            onClick = { openDialog = false },
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
                                var isOk = true
                                cmdList.value.forEach {
                                    if (it.name == text) {
                                        ToastUtil.toast(context, "名字重复")
                                        isOk = false
                                    }
                                }
                                if (isOk) {
                                    cmdList.value = cmdList.value.toMutableList().also {
                                        it.add(Cmd(text, "", "hex"))
                                    }
                                    SaveDataToLocalFile.save(
                                        context,
                                        "CMDSend",
                                        JSON.toJSONString(cmdList.value)
                                    )
                                    openDialog = false
                                }

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

                            Text(text = "确定", color = Color.White, fontSize = 18.sp)
                        }
                    }
                }
            }
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
                openDialog = true

            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_playlist_add_black_24dp),
                    contentDescription = null
                )
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxWidth(), rememberLazyListState()) {
            items(cmdList.value, key = { item: Cmd ->
                item.name
            }) { item ->
                // 侧滑删除所需State
                val dismissState = rememberDismissState()
                // 按指定方向触发删除后的回调，在此处变更具体数据
                if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                    cmdList.value = cmdList.value.toMutableList().also {
                        it.remove(item)
                    }
                    SaveDataToLocalFile.save(context, "CMDSend", JSON.toJSONString(cmdList.value))
                }
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
                    var name by remember {
                        mutableStateOf(item.name)
                    }
                    var text by remember {
                        mutableStateOf(item.text)
                    }
                    var type by remember {
                        mutableStateOf(item.type)
                    }
                    Row(
                        modifier = Modifier
                            .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (type == "hex") {
                                    type = "str"
                                    item.type = "str"
                                } else {
                                    type = "hex"
                                    item.type = "hex"
                                }
                                SaveDataToLocalFile.save(
                                    context,
                                    "CMDSend",
                                    JSON.toJSONString(cmdList.value)
                                )
                            },
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
                                painter = painterResource(id = if (type == "hex") R.drawable.h else R.drawable.s),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color.Black
                            )
                        }
                        TextField(
                            textStyle = TextStyle(fontSize = 11.sp),
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
                            onValueChange = {
                                text = it
                                item.text = it
                                SaveDataToLocalFile.save(
                                    context,
                                    "CMDSend",
                                    JSON.toJSONString(cmdList.value)
                                )
                            }, // 文字改变时，就赋值给text
                            label = { Text(text = name) }, // label是Input
                            trailingIcon = @Composable {
                                Image(
                                    painter = painterResource(id = R.drawable.send), // 搜索图标
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .clickable { /*model.sendStringFlow.value += item.text*/ }) // 给图标添加点击事件，点击就吐司提示内容
                            },
                            leadingIcon = @Composable {
                                Image(imageVector = Icons.Filled.Clear, // 清除图标
                                    contentDescription = null,
                                    modifier = Modifier.clickable {
                                        text = ""
                                        item.text = ""
                                        SaveDataToLocalFile.save(
                                            context,
                                            "CMDSend",
                                            JSON.toJSONString(cmdList.value)
                                        )
                                    }) // 给图标添加点击事件，点击就清空text
                            },
                            // placeholder = @Composable { Text(text = "This is placeholder") }, // 不输入内容时的占位符
                        )
                    }
                }
            }

        }
    }
}