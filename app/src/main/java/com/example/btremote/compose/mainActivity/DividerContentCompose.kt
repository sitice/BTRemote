package com.example.btremote.compose.mainActivity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btremote.R
import com.example.btremote.compose.Screens
import com.example.btremote.ui.theme.Blue
import com.example.btremote.ui.theme.LightBlue
import com.example.btremote.viewmodel.MainViewModel

@Composable
fun DividerContentCompose(
    onOpenNewScreenClick:(id:String)->Unit,
    model: MainViewModel = viewModel()
) {
//    Text("已连接设备", modifier = Modifier.padding(16.dp))
//    Divider()
//    Spacer(modifier = Modifier.height(10.dp))
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(40.dp)
//            .padding(start = 16.dp, end = 16.dp)
//            .background(
//                color = if (model.nowScreen == Screens.productScreen) LightBlue else Color.White,
//                shape = RoundedCornerShape(5.dp)
//            )
//            .clickable {
//                onOpenNewScreenClick(Screens.productScreen)
//            },
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            modifier = Modifier
//                .size(30.dp)
//                .padding(start = 10.dp),
//            painter = painterResource(R.drawable.df_car),
//            contentDescription = null, tint = if (model.nowScreen == Screens.productScreen) Blue else Color.Black
//        )
//        Spacer(modifier = Modifier.width(30.dp))
//        Text(text = "点浮战车", color = if (model.nowScreen == Screens.productScreen) Blue else Color.Black)
//    }


    Spacer(modifier = Modifier.height(10.dp))
    Divider()
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 16.dp, end = 16.dp)
            .background(
                color = if (model.nowScreen == Screens.mainScreen) LightBlue else Color.White,
                shape = RoundedCornerShape(5.dp)
            )
            .clickable {
                onOpenNewScreenClick(Screens.mainScreen)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(30.dp)
                .padding(start = 10.dp),
            painter = painterResource(R.drawable.baseline_home_black_36dp),
            contentDescription = null, tint = if (model.nowScreen == Screens.mainScreen) Blue else Color.Black
        )
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = "首页", color = if (model.nowScreen == Screens.mainScreen) Blue else Color.Black)
    }

    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 16.dp, end = 16.dp)
            .background(
                color = if (model.nowScreen == Screens.recSendScreen) LightBlue else Color.White,
                shape = RoundedCornerShape(5.dp)
            )
            .clickable {
                onOpenNewScreenClick(Screens.recSendScreen)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(30.dp)
                .padding(start = 10.dp),
            painter = painterResource(R.drawable.recandsend),
            contentDescription = null, tint = if (model.nowScreen == Screens.recSendScreen) Blue else Color.Black
        )
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = "基本收发", color = if (model.nowScreen == Screens.recSendScreen) Blue else Color.Black)
    }

    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 16.dp, end = 16.dp)
            .background(
                color = if (model.nowScreen == Screens.remoteScreen) LightBlue else Color.White,
                shape = RoundedCornerShape(5.dp)
            )
            .clickable {
                onOpenNewScreenClick(Screens.remoteScreen)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(30.dp)
                .padding(start = 10.dp),
            painter = painterResource(R.drawable.baseline_games_black_24dp),
            contentDescription = null, tint = if (model.nowScreen == Screens.remoteScreen) Blue else Color.Black
        )
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = "遥控", color = if (model.nowScreen == Screens.remoteScreen) Blue else Color.Black)
    }


//    Spacer(modifier = Modifier.height(10.dp))
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(40.dp)
//            .padding(start = 16.dp, end = 16.dp)
//            .background(
//                color = if (model.nowScreen == Screens.waveDisplayScreen) LightBlue else Color.White,
//                shape = RoundedCornerShape(5.dp)
//            )
//            .clickable {
//                onOpenNewScreenClick(Screens.waveDisplayScreen)
//            },
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            modifier = Modifier
//                .size(30.dp)
//                .padding(start = 10.dp),
//            painter = painterResource(R.drawable.baseline_microwave_black_24dp),
//            contentDescription = null, tint = if (model.nowScreen == Screens.waveDisplayScreen) Blue else Color.Black
//        )
//        Spacer(modifier = Modifier.width(30.dp))
//        Text(text = "波形显示", color = if (model.nowScreen == Screens.waveDisplayScreen) Blue else Color.Black)
//    }


//    Spacer(modifier = Modifier.height(10.dp))
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(40.dp)
//            .padding(start = 16.dp, end = 16.dp)
//            .background(
//                color = if (model.nowScreen == Screens.settingScreen) LightBlue else Color.White,
//                shape = RoundedCornerShape(5.dp)
//            )
//            .clickable {
//                onOpenNewScreenClick(Screens.settingScreen)
//            },
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            modifier = Modifier
//                .size(30.dp)
//                .padding(start = 10.dp),
//            painter = painterResource(R.drawable.baseline_settings_black_24dp),
//            contentDescription = null, tint = if (model.nowScreen == Screens.settingScreen) Blue else Color.Black
//        )
//        Spacer(modifier = Modifier.width(30.dp))
//        Text(text = "设置", color = if (model.nowScreen == Screens.settingScreen) Blue else Color.Black)
//    }

//    Spacer(modifier = Modifier.height(10.dp))
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(40.dp)
//            .padding(start = 16.dp, end = 16.dp)
//            .background(
//                color = if (model.nowScreen == Screens.protocolScreen) LightBlue else Color.White,
//                shape = RoundedCornerShape(5.dp)
//            )
//            .clickable {
//                onOpenNewScreenClick(Screens.protocolScreen)
//            },
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            modifier = Modifier
//                .size(30.dp)
//                .padding(start = 10.dp),
//            painter = painterResource(R.drawable.baseline_list_alt_black_24dp),
//            contentDescription = null, tint = if (model.nowScreen == Screens.protocolScreen) Blue else Color.Black
//        )
//        Spacer(modifier = Modifier.width(30.dp))
//        Text(text = "通信协议", color = if (model.nowScreen == Screens.protocolScreen) Blue else Color.Black)
//    }


    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 16.dp, end = 16.dp)
            .background(
                color = if (model.nowScreen == Screens.aboutScreen) LightBlue else Color.White,
                shape = RoundedCornerShape(5.dp)
            )
            .clickable {
                onOpenNewScreenClick(Screens.aboutScreen)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(30.dp)
                .padding(start = 10.dp),
            painter = painterResource(R.drawable.baseline_info_black_24dp),
            contentDescription = null, tint = if (model.nowScreen == Screens.aboutScreen) Blue else Color.Black
        )
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = "关于", color = if (model.nowScreen == Screens.aboutScreen) Blue else Color.Black)
    }
}



