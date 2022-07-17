package com.example.btremote.compose.tab

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.example.btremote.R
import com.example.btremote.compose.Screens.mainScreen
import com.example.btremote.compose.Screens.moreScreen
import com.example.btremote.compose.Screens.recSendScreen

sealed class Screen(
    val id:String,
    val title:String,
    val icon:ImageVector
){
    object HomeScreen:Screen(mainScreen,"首页", Icons.Default.Home)
    object DataScreen:Screen(moreScreen,"高级", Icons.Default.Star)
    object ViewScreen:Screen(recSendScreen,"收发", Icons.Default.Send)

    object Item{
        val list = listOf(
            HomeScreen,ViewScreen,DataScreen
        )
    }
}
