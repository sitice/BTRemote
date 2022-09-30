package com.example.btremote.compose.mainActivity.dataScreen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import com.example.btremote.compose.Screens
import com.example.btremote.connect.DataFormat
import com.example.btremote.protocol.bytesToHexString
import com.example.btremote.ui.theme.roundedCorner10dp
import com.example.btremote.viewmodel.*

const val baseRecSendScreen = 0
const val moreRecSendScreen = 1

@Composable
fun DataScreen(model: MainViewModel = viewModel()) {

    val recType by model.baseRecType.collectAsState(initial = 0)

    val scrollState = rememberScrollState()

    LaunchedEffect(model.baseRecText.length) {
        App.bluetoothRecData.collect { bytes ->
            if (bytes.isNotEmpty()) {
                val string = if (recType == DataFormat.STR) String(
                    bytes
                ).replace("\\n", "\n")
                    .replace("\\t", "\t") else bytesToHexString(bytes)
                if (string != null && !model.isPause) {
                    model.baseRecText += string
                    if (model.baseRecText.length > 10000) {
                        model.baseRecText = model.baseRecText.removeRange(
                            0,
                            model.baseRecText.length - 5000
                        )
                    }
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
                        onClick = { model.dataScreenType = baseRecSendScreen },
                        modifier = Modifier
                            .width(60.dp)
                            .height(35.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (model.dataScreenType == baseRecSendScreen) Color(0xffffdacc) else Color.White,
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
                            model.dataScreenType = moreRecSendScreen
                        },
                        modifier = Modifier
                            .width(60.dp)
                            .height(35.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (model.dataScreenType == moreRecSendScreen) Color(0xffffdacc) else Color.White,
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
        if (model.dataScreenType == baseRecSendScreen)
            BaseSendRec()
        else if (model.dataScreenType == moreRecSendScreen)
            CmdSendRec()

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
                    text = model.baseRecText,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(state = scrollState, reverseScrolling = true)
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
                    if (recType == DataFormat.HEX) model.changeBaseRecType(DataFormat.STR) else model.changeBaseRecType(
                        DataFormat.HEX
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
                    painter = painterResource(id = if (recType == DataFormat.STR) R.drawable.s else R.drawable.h),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
            Button(
                onClick = {
                    model.isPause = !model.isPause
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
                    painter = painterResource(id = if (model.isPause) R.drawable.baseline_pause_black_48dp else R.drawable.baseline_play_arrow_black_48dp),
                    contentDescription = null,
                    tint = Color.Black
                )
            }
            Button(
                onClick = {
                    model.baseRecText = ""
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