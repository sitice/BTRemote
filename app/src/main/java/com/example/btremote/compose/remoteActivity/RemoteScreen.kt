package com.example.btremote.compose.remoteActivity

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alibaba.fastjson.JSON
import com.example.btremote.Orientation
import com.example.btremote.app.App
import com.example.btremote.compose.connect.ConnectBar
import com.example.btremote.compose.connect.ShowBluetoothDevice
import com.example.btremote.compose.waveDisplay.ColorSelectDialog
import com.example.btremote.database.remoteWidget.RemoteWidget
import com.example.btremote.tools.ToastUtil
import com.example.btremote.tools.readAssetsFile
import com.example.btremote.ui.theme.gradient3
import com.example.btremote.viewmodel.RemoteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RemoteCompose(context: Context = LocalContext.current, model: RemoteViewModel = viewModel()) {

    val orientation = Orientation.instance(context)
    var openBluetoothDialog by remember {
        mutableStateOf(false)
    }
    var openConfirm by remember {
        mutableStateOf(false)
    }

    var editMode by remember {
        mutableStateOf(false)
    }
    var gyroMode by remember {
        mutableStateOf(false)
    }

    var openWidgetSelect by remember {
        mutableStateOf(false)
    }

    var openWidgetDelete by remember {
        mutableStateOf(false)
    }


    var openNewWidget by remember {
        mutableStateOf(false)
    }

    var selectWidgetType by remember {
        mutableStateOf<Int?>(null)
    }

    var selectColor by remember {
        mutableStateOf(0x03a9f4)
    }

    var openSelectColor by remember {
        mutableStateOf(false)
    }
    val widgets by model.widgets.collectAsState(initial = emptyList())
    if (openSelectColor) {
        ColorSelectDialog(
            iniColor = 0x03a9f4,
            onCancelClick = {
                openSelectColor = false
                openNewWidget = true
            },
            onSureClick = { color ->
                selectColor = color
                openSelectColor = false
                openNewWidget = true
            })
    }

    if (openNewWidget && selectWidgetType != null) {
        NewWidgetDialog(
            widgetType = selectWidgetType!!,
            color = selectColor,
            onOpenColorSelectClick = {
                openSelectColor = true
                openNewWidget = false
            },
            onCancelClick = {
                openNewWidget = false
                model.name = ""
                model.key = ""
                model.len = ""
                model.nameError = false
                model.keyError = false
                model.lenError = false
            },
            onSureClick = { name, key, len ->
                openNewWidget = false
                openNewWidget = false
                model.name = ""
                model.key = ""
                model.len = ""
                model.nameError = false
                model.keyError = false
                model.lenError = false
                model.widgetsTemp.add(
                    RemoteWidget(
                        name,
                        selectWidgetType!!,
                        0f,
                        -50f,
                        0f,
                        1f,
                        selectColor,
                        if (selectWidgetType == WidgetType.ROCKER) 0 else if (selectWidgetType == WidgetType.SLIDER) len else key,
                        0
                    )
                )
            }
        )
    }


    if (openConfirm)
        ShowConfirmDialog(
            text = "你确定要恢复初始布局吗",
            onSureClick = {
                val list = JSON.parseArray(
                    readAssetsFile(context, "remoteWidgets.json"),
                    RemoteWidget::class.java
                )
                model.widgetsTemp.clear()
                model.widgetsTemp.addAll(list)
                openConfirm = false
            },
            onCancelClick = {
                openConfirm = false
            })

    if (openWidgetDelete)
        ShowConfirmDialog(
            text = "你确定要删除此控件吗",
            onSureClick = {
                model.widgetsTemp.remove(model.selectWidget)
                openWidgetDelete = false
            },
            onCancelClick = {
                openWidgetDelete = false
            })

    if (openBluetoothDialog)
        ShowBluetoothDevice({
            openBluetoothDialog = false
        }, {
            //蓝牙开关回调
            if (it) {
                model.openBluetooth(context)
            } else {
                model.closeBluetooth()
            }
        })

    if (openWidgetSelect) {
        WidgetListDialog(onCancelClick = {
            openWidgetSelect = false
        }, onWidgetSelect = {
            openNewWidget = true
            openWidgetSelect = false
            selectWidgetType = it
        })
    }

    Box(
        Modifier
            .background(gradient3)
            .fillMaxSize()
    ) {
        RemoteToolBar(
            editModel = editMode,
            gyroMode = gyroMode,
            onSettingClick = {

            },
            onDebugClick = {

            },
            onEditClick = {
                model.widgetsTemp.clear()
                widgets.forEach {
                    model.widgetsTemp.add(it.copy())//这里采用深度复制，浅复制是复制引用，会导致源数据修改
                }
                editMode = true
            },
            onRemoteModeClick = {
                gyroMode = !gyroMode
                if (gyroMode) {
                    orientation.init()
                } else {
                    orientation.unregister()
                }
            })

        WidgetToolBar(editMode = editMode, modifier = Modifier.align(Alignment.TopCenter),
            onRotateLeftClick = {},
            onRotateRightClick = {},
            onEnlargeClick = {},
            onReduceClick = {
                if (model.selectWidget != null) {

                }
            },
            onResetClick = {
                openConfirm = true
            },
            onAddClick = {
                openWidgetSelect = true
            },
            onDeleteClick = {
                if (model.selectWidget != null)
                    openWidgetDelete = true
                else
                    ToastUtil.toast(context, "请选择一个控件")
            },
            onCancelClick = {
                editMode = false
                model.selectWidget = null
            },
            onOKClick = {
                editMode = false
                model.deleteAll()
                runBlocking {
                    delay(2)//延时等待上一次数据库操作完成
                }
                model.insert(model.widgetsTemp.toList())
                model.selectWidget = null
            })

        if (editMode) {
            WidgetsTemp()
        } else {
            Widgets(widgets = widgets)
        }

        RemoteRecData(
            editModel = editMode,
            gyroMode = gyroMode,
            modifier = Modifier.align(Alignment.TopEnd)
        )
        AnimatedVisibility(
            visible = !editMode,
            enter = scaleIn(),
            exit = scaleOut(),
            modifier = Modifier
                .align(Alignment.TopStart),
        )
        {
            Row(
                modifier = Modifier
                    .padding(start = 40.dp)
            )
            {
                ConnectBar(
                    onOpenBluetoothButtonClick = { openBluetoothDialog = true },
                    onOpenWIFIButtonClick = { },
                    onOpenUSBButtonClick = { },
                    isDark = true
                )
            }
        }
    }
}