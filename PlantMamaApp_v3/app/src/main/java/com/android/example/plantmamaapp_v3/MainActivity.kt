package com.android.example.plantmamaapp_v3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.android.example.plantmamaapp_v3.ui.theme.PLantMamaTheme
import com.android.example.plantmamaapp_v3.ui.PlantMamaApp


    class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                PLantMamaTheme {
                    PlantMamaApp()
                }
            }
        }
    }
