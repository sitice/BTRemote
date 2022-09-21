package com.example.btremote.compose.baseSendRec

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btremote.R
import com.example.btremote.app.App
import com.example.btremote.protocol.bytesToHexString
import com.example.btremote.tools.EasyDataStore
import com.example.btremote.ui.theme.roundedCorner10dp
import com.example.btremote.viewmodel.*

const val baseRecSendScreen = 0
const val moreRecSendScreen = 1

const val HEX_DATA = 0
const val STRING_DATA = 1

@Composable
fun BaseSendRec(model: MainViewModel = viewModel()) {

    var nowScreen by remember {
        mutableStateOf(baseRecSendScreen)
    }

    var recType by remember {
        mutableStateOf(EasyDataStore.getSyncData("Base_rec_type", 0))
    }

    val isPause by model.isPauseFlow.collectAsState()

    var rec by remember {
        mutableStateOf("")
    }

    val scrollState = rememberScrollState()
    LaunchedEffect(rec.length) {
        App.bluetoothRecDataFlow.collect { bytes ->
            if (bytes.isNotEmpty()) {
                val string = if (recType == STRING_DATA) String(
                    bytes
                ).replace("\\n", "\n")
                    .replace("\\t", "\t") else bytesToHexString(bytes)
                if (string != null && !isPause) {
                    rec += string
                    if (rec.length > 10000) {
                        rec = rec.removeRange(0, rec.length - 5000)
                    }
                    Log.d("1","1")
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            }
        }
    }

    Column {
        Card(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
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

            }

        }
        if (nowScreen == baseRecSendScreen)
            BaseRecScreen()
        else if (nowScreen == moreRecSendScreen)
            MoreRecScreen()

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .padding(start = 30.dp, end = 30.dp, top = 10.dp)
                .border(2.dp, color = Color.Blue, shape = roundedCorner10dp),
            shape = roundedCorner10dp
        )
        {
            Column(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxSize()
            ) {
                Text(text = "Receive", modifier = Modifier.padding(10.dp), fontSize = 12.sp)
                Divider(modifier = Modifier.fillMaxWidth())
                Text(
                    text = rec,
                    modifier = Modifier.fillMaxSize().verticalScroll(state = scrollState, reverseScrolling = true)
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    recType =
                        if (recType == HEX_DATA) STRING_DATA else HEX_DATA
                    EasyDataStore.putSyncData("Base_rec_type", recType)
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
                    painter = painterResource(id = if (recType == STRING_DATA) R.drawable.s else R.drawable.h),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
            Button(
                onClick = {
                    if (model.isPauseFlow.value) model.playArrow() else model.pause()
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
                    painter = painterResource(id = if (isPause) R.drawable.baseline_pause_black_48dp else R.drawable.baseline_play_arrow_black_48dp),
                    contentDescription = null,
                    tint = Color.Black
                )
            }
            Button(
                onClick = {
                    rec = ""
                }, modifier = Modifier
                    .width(80.dp)
                    .height(40.dp), shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xa0fc5531),
                    contentColor = Color.Black
                ),
                elevation = null
            ) {
                Image(
                    painter = painterResource(id = R.drawable.round_delete_forever_black_48dp),
                    contentDescription = null
                )
            }
        }
    }
}