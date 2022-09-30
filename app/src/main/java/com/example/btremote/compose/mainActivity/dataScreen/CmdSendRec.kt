package com.example.btremote.compose.mainActivity.dataScreen

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alibaba.fastjson.JSON
import com.example.btremote.R
import com.example.btremote.app.App
import com.example.btremote.connect.DataFormat
import com.example.btremote.database.cmd.Cmd
import com.example.btremote.protocol.string2Byte
import com.example.btremote.tools.SaveDataToLocalFile
import com.example.btremote.tools.ToastUtil
import com.example.btremote.ui.theme.roundedCorner10dp
import com.example.btremote.viewmodel.MainViewModel
import kotlinx.coroutines.flow.map

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun CmdSendRec(context: Context = LocalContext.current,model: MainViewModel = viewModel()) {

    val cmdList by model.cmdList.collectAsState(emptyList())

    var openDialog by remember {
        mutableStateOf(false)
    }
    var name by remember {
        mutableStateOf("")
    }

    var error by remember {
        mutableStateOf(false)
    }
    if (openDialog)
        TypeNameDialog(text = name, isError = error,onSureClick = {
            if (!error){
                model.insertCmd(Cmd(name, DataFormat.HEX, ""))
                openDialog = false
                name = ""
            }else{
                ToastUtil.toast(context = context, content = "已有相同的名字")
            }
        }, onCancelClick = {
            openDialog = false
            error = false
            name = ""
        }, onTextChange = { text->
            name = text
            error = false
            cmdList.forEach {
                if (it.name == name){
                    error = true
                    return@TypeNameDialog
                }
            }
        })

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
            items(cmdList, key = { item: Cmd -> item.name }) { item ->
                // 侧滑删除所需State
                val dismissState = rememberDismissState()
                // 按指定方向触发删除后的回调，在此处变更具体数据
                if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                    model.deleteCmd(item)
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
                    Row(
                        modifier = Modifier
                            .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var type by remember {
                            mutableStateOf(item.type)
                        }
                        var cmd by remember {
                            mutableStateOf(item.cmd)
                        }
                        Button(
                            onClick = {
                                type = if (type == DataFormat.HEX) DataFormat.STR else DataFormat.HEX
                                item.type =
                                    if (item.type == DataFormat.HEX) DataFormat.STR else DataFormat.HEX
                                model.updateCmd(item)
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
                                painter = painterResource(id = if (type == DataFormat.HEX) R.drawable.h else R.drawable.s),
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
                            value = cmd, // 显示文本
                            onValueChange = {
                                item.cmd = it
                                cmd = it
                                model.updateCmd(item)
                            }, // 文字改变时，就赋值给text
                            label = { Text(text = item.name) }, // label是Input
                            trailingIcon = @Composable {
                                Image(
                                    painter = painterResource(id = R.drawable.send), // 搜索图标
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .clickable {
                                            val data =
                                                if (type == DataFormat.HEX) cmd.toByteArray() else string2Byte(
                                                    cmd
                                                )
                                            App.bluetoothService.writeData(data)
                                        }) // 给图标添加点击事件，点击就吐司提示内容
                            },
                            leadingIcon = @Composable {
                                Image(imageVector = Icons.Filled.Clear, // 清除图标
                                    contentDescription = null,
                                    modifier = Modifier.clickable {
                                        item.cmd = ""
                                        model.updateCmd(item)
                                    }) // 给图标添加点击事件，点击就清空text
                            },
                        )
                    }
                }
            }

        }
    }
}