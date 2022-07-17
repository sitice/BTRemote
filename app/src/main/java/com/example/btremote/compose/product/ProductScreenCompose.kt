package com.example.btremote.compose.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.btremote.R
import com.example.btremote.compose.Screens

@Composable
fun ProductScreen(navController:NavController) {

    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp), color = Color.Transparent
        ) {

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 50.dp, top = 10.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .padding(top = 35.dp)
                    .height(50.dp)
                    .width(3.dp)
                    .background(color = Color(255, 102, 0))
            )
            Column {
                Text(text = "Product", modifier = Modifier.padding(top = 30.dp, start = 30.dp), fontSize = 20.sp, color = Color.White)
                Text(text = "description", fontSize = 11.sp, modifier = Modifier.padding(start = 30.dp, top = 10.dp), color = Color(0xfff2f2f2))
            }
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .padding(top = 30.dp)
        ) {
            Button(
                onClick = { navController.navigate(Screens.remoteScreen){
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = null,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xff1a75ff),
                    //不可以点击的颜色
                    disabledBackgroundColor = Color.Gray
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.game), contentDescription = null, tint = Color(0xffff8533))
                Spacer(modifier = Modifier.width(30.dp))
                Text(text = "remote", color = Color.White, fontSize = 18.sp)
            }
            Button(
                onClick = { navController.navigate(Screens.settingScreen){
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                } },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = null,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xff1a75ff),
                    //不可以点击的颜色
                    disabledBackgroundColor = Color.Gray
                )
            ) {
                Image(painter = painterResource(id = R.drawable.setting), contentDescription = null)
                Spacer(modifier = Modifier.width(30.dp))
                Text(text = "setting", color = Color.White, fontSize = 18.sp)
            }
            Button(
                onClick = { navController.navigate(Screens.debugScreen){
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                } },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = null,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xff1a75ff),
                    //不可以点击的颜色
                    disabledBackgroundColor = Color.Gray
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.debugger),
                    contentDescription = null,
                    tint = Color.Green,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text(text = "debug", color = Color.White, fontSize = 18.sp)
            }
        }

    }

}

//@Composable
//@Preview
//fun ProductPreview() {
//    Product()
//}
