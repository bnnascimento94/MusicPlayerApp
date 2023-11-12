package com.vullpes.musicplayerapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.vullpes.musicplayerapp.data.player.service.JetAudioService
import com.vullpes.musicplayerapp.ui.audio.AudioViewModel
import com.vullpes.musicplayerapp.ui.audio.HomeScreen
import com.vullpes.musicplayerapp.ui.audio.UIEvents
import com.vullpes.musicplayerapp.ui.theme.MusicPlayerAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel:AudioViewModel by viewModels()
    private var isServiceRunning = false

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerAppTheme {
                // A surface container using the 'background' color from the theme
                val permissionState = rememberPermissionState(permission = android.Manifest.permission.READ_EXTERNAL_STORAGE)
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(key1 = lifecycleOwner){
                    val observer = LifecycleEventObserver{_,event ->
                        if(event == Lifecycle.Event.ON_RESUME){
                            permissionState.launchPermissionRequest()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(
                        progress = viewModel.progress,
                        progressTimeString = viewModel.progressString,
                        onProgress = {viewModel.onUiEvents(UIEvents.SeekTo(it))},
                        isAudioPlaying = viewModel.isPlaying,
                        currentPlaying = viewModel.currentSelectedAudio,
                        audiList = viewModel.audioList,
                        onStart = {
                            viewModel.onUiEvents(UIEvents.PlayPause)
                        },
                        onItemClick ={
                            viewModel.onUiEvents(UIEvents.SelectedAudioChange(it))
                            startService()
                        },
                        onNext = {
                            viewModel.onUiEvents(UIEvents.SeekToNext)
                        },
                        onBack = {
                            viewModel.onUiEvents(UIEvents.SeekToBack)
                        }
                    )
                }
            }
        }
    }

    private fun startService(){
        if(!isServiceRunning){
            val intent = Intent(this, JetAudioService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            }else{
                startService()
            }
            isServiceRunning = true
        }
    }
}

