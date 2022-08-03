package com.mvi.sample

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Button
import androidx.compose.material.Text
import com.mvi.home.ui.HomeScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var homeScreen: HomeScreen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val getPermission = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) {
                if (
                    it[Manifest.permission.READ_EXTERNAL_STORAGE] == true &&
                    it[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
                ) {
                    buildAndInstall()
                } else {
                    Toast.makeText(this, "NEED PERMISSION", Toast.LENGTH_SHORT).show()
                }
            }
            Button(onClick = {
                getPermission.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }) {
                Text(text = "Build")
            }
        }
    }

    private fun buildAndInstall() {
        val intent = Intent(this, ApkMakerService::class.java)
        startService(intent)
    }
}