package com.example.btremote.compose.remoteActivity

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btremote.Orientation
import com.example.btremote.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RemoteRecData(
    editModel: Boolean,
    gyroMode: Boolean,
    modifier: Modifier,
    context: Context = LocalContext.current
) {
    val orientation = Orientation.instance(context)
    val eulerAngle = orientation.angles.collectAsState()
    AnimatedVisibility(
        visible = !editModel,
        enter = scaleIn(),
        exit = scaleOut(),
        modifier = modifier,
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ) {
            AnimatedVisibility(
                visible = gyroMode,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(25.dp),
                        painter = painterResource(id = R.drawable.gyro),
                        contentDescription = null,
                        tint = Color.White
                    )
                    Text(
                        text = "pitch:", modifier = Modifier
                            .padding(start = 5.dp), color = Color.White, fontSize = 10.sp
                    )
                    Text(
                        text = String.format("%.1f", eulerAngle.value[0]), modifier = Modifier
                            .padding(start = 5.dp)
                            .width(30.dp), color = Color.White, fontSize = 10.sp
                    )
                    Text(
                        text = "roll:", modifier = Modifier
                            .padding(start = 0.dp), color = Color.White, fontSize = 10.sp
                    )
                    Text(
                        text = String.format("%.1f", eulerAngle.value[1]), modifier = Modifier
                            .padding(start = 5.dp)
                            .width(30.dp), color = Color.White, fontSize = 10.sp
                    )
                    Text(
                        text = "yaw:", modifier = Modifier
                            .padding(start = 0.dp), color = Color.White, fontSize = 10.sp
                    )
                    Text(
                        text = String.format("%.1f", eulerAngle.value[2]), modifier = Modifier
                            .padding(start = 5.dp)
                            .width(30.dp), color = Color.White, fontSize = 10.sp
                    )
                }
            }
            Icon(
                modifier = Modifier
                    .padding()
                    .size(20.dp),
                painter = painterResource(id = R.drawable.distance),
                contentDescription = null,
                tint = Color.White
            )

            Text(
                text = "x:", modifier = Modifier
                    .padding(start = 10.dp), color = Color.White, fontSize = 11.sp
            )
            Text(
                text = "null", modifier = Modifier
                    .padding(start = 10.dp)
                    .width(35.dp), color = Color.White, fontSize = 11.sp
            )
            Text(
                text = "y:", modifier = Modifier
                    .padding(start = 0.dp), color = Color.White, fontSize = 11.sp
            )
            Text(
                text = "null", modifier = Modifier
                    .padding(start = 5.dp)
                    .width(35.dp), color = Color.White, fontSize = 11.sp
            )
            Icon(
                modifier = Modifier
                    .padding()
                    .size(25.dp),
                painter = painterResource(id = R.drawable.euler_yaw),
                contentDescription = null,
                tint = Color.White
            )
            Text(
                text = "yaw:", modifier = Modifier
                    .padding(start = 5.dp), color = Color.White, fontSize = 11.sp
            )
            Text(
                text = "null", modifier = Modifier
                    .padding(start = 10.dp)
                    .width(35.dp), color = Color.White, fontSize = 11.sp
            )

            Icon(
                modifier = Modifier
                    .padding()
                    .size(20.dp),
                painter = painterResource(id = R.drawable.vot),
                contentDescription = null,
                tint = Color.White
            )
            Text(
                text = "voltage:", modifier = Modifier
                    .padding(start = 5.dp), color = Color.White, fontSize = 11.sp
            )
            Text(
                text = "null", modifier = Modifier
                    .padding(start = 10.dp)
                    .width(35.dp), color = Color.White, fontSize = 11.sp
            )
        }

    }
}