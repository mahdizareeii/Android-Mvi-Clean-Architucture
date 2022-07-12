package com.mvi.home.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvi.core.base.BaseScreen
import javax.inject.Inject

class HomeScreen @Inject constructor() : BaseScreen() {

    @Composable
    override fun onScreenCreated() {
        val homeViewModel = hiltViewModel<HomeViewModel>()
        homeViewModel.showToast.value?.let {
            Toast.makeText(getContext(), it, Toast.LENGTH_SHORT).show()
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    homeViewModel.onTriggerEvent(HomeEvent.ShowToast)
                }
            ) {
                Text(text = "show toast")
            }
        }
    }
}