package com.mvi.sample

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.navigation.compose.rememberNavController
import com.mvi.home.ui.HomeScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var homeScreen: HomeScreen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            homeScreen.createScreen(navController = rememberNavController())
            Button(onClick = {
                buildAndInstall()
            }) {
                Text(text = "Build")
            }
        }
    }

    fun buildAndInstall() {
        val intent = Intent(this, ApkMakerService::class.java)
        startService(intent)
    }
}