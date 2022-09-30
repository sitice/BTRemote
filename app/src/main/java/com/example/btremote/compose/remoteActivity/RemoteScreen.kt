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
import com.example.btremote.compose.connect.ConnectBar
import com.example.btremote.compose.connect.ShowBluetoothDevice
import com.example.btremote.compose.waveDisplay.ColorSelectDialog
import com.example.btremote.database.remoteWidget.RemoteWidget
import com.example.btremote.tools.ToastUtil
import com.example.btremote.tools.readAssetsFile
import com.example.btremote.ui.theme.gradient3
import com.example.btremote.viewmodel.RemoteViewModel

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

    var selectWidget by remember {
        mutableStateOf<RemoteWidget?>(null)
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
            onCancelClick = { openNewWidget = false },
            onSureClick = { name, key, len ->
                Log.d("1", "1")
                openNewWidget = false
                model.insert(
                    RemoteWidget(
                        name,
                        selectWidgetType!!,
                        0f,
                        0f,
                        0f,
                        0f,
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
                model.deleteAll()
                val list = JSON.parseArray(
                    readAssetsFile(context, "remoteWidgets.json"),
                    RemoteWidget::class.java
                )
                model.insert(list)
                openConfirm = false
            },
            onCancelClick = {
                openConfirm = false
            })

    if (openWidgetDelete)
        ShowConfirmDialog(
            text = "你确定要删除此控件吗",
            onSureClick = {
                selectWidget?.let { model.delete(it) }
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
                if (selectWidget != null) {

                }
            },
            onResetClick = {
                openConfirm = true
            },
            onAddClick = {
                openWidgetSelect = true
            },
            onDeleteClick = {
                if (selectWidget != null)
                    openWidgetDelete = true
                else
                    ToastUtil.toast(context, "请选择一个控件")
            },
            onCancelClick = {
                editMode = false
                selectWidget = null
            },
            onOKClick = {
                editMode = false
                selectWidget = null
            })
        Widgets(editMode = editMode, selectWidget = selectWidget, onSelectClick = {
            if (editMode) {
                selectWidget = it
                Log.d("1", "1")
            }
        })

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