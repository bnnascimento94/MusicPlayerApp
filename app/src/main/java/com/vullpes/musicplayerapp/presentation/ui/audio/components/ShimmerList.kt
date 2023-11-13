package com.vullpes.musicplayerapp.presentation.ui.audio.components

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vullpes.musicplayerapp.presentation.ui.theme.ShimmerDarkGray
import com.vullpes.musicplayerapp.presentation.ui.theme.ShimmerMediumGray

@Composable
fun ShimmerList(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "")
    val alphaAnim = transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500,
                delayMillis = 0,
                easing = FastOutLinearInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )


    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
        ) {
        repeat(10){
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top =4.dp, start = 8.dp, end = 8.dp, bottom = 0.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = if (isSystemInDarkTheme()) Color.Black else Color.LightGray)
                    ,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Surface(
                    modifier = Modifier
                        .alpha(alpha = alphaAnim.value)
                        .padding(10.dp)
                        .clip(CircleShape)
                        .size(50.dp),
                    color = if (isSystemInDarkTheme()) ShimmerDarkGray else ShimmerMediumGray
                ) {}

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.size(4.dp))
                    Surface(
                        modifier = Modifier
                            .alpha(alpha = alphaAnim.value)
                            .fillMaxWidth()
                            .height(20.dp),
                        color = if (isSystemInDarkTheme()) ShimmerDarkGray else ShimmerMediumGray
                    ) {}
                    Spacer(modifier = Modifier.size(4.dp))
                    Surface(
                        modifier = Modifier
                            .alpha(alpha = alphaAnim.value)
                            .fillMaxWidth()
                            .height(20.dp),
                        color = if (isSystemInDarkTheme()) ShimmerDarkGray else ShimmerMediumGray
                    ) {}
                }

                Surface(
                    modifier = Modifier
                        .alpha(alpha = alphaAnim.value)
                        .height(20.dp)
                        .weight(0.1f),
                    color = if (isSystemInDarkTheme()) ShimmerDarkGray else ShimmerMediumGray
                ) {}

                Spacer(modifier = Modifier.size(8.dp))


            }
        }

    }



}

@Preview(showBackground = true)
@Composable
fun ShimmerListPrev() {
    ShimmerList()
}