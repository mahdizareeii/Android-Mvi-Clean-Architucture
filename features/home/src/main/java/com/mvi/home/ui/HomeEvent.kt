package com.mvi.home.ui

import com.mvi.core.base.BaseIntent

sealed class HomeEvent : BaseIntent {

    object ShowToast : HomeEvent()

}