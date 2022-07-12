package com.mvi.home.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.mvi.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeEvent>() {
    private val _showToast: MutableState<String?> = mutableStateOf(null)
    val showToast: State<String?> get() = _showToast

    override fun onTriggerEvent(eventType: HomeEvent) {
        when (eventType) {
            HomeEvent.ShowToast -> showToast()
        }
    }

    private fun showToast() {
        _showToast.value = "test mvi"
        _showToast.value = null
    }
}