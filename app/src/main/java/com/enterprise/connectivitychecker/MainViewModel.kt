package com.enterprise.connectivitychecker

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel: ViewModel() {

    val isInternetAvailable = MutableStateFlow(false)
    val showBackOnline = MutableStateFlow(false)

}