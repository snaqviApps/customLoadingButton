package com.udacity.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.main.ui.LoadingButtonViewModel
import java.lang.IllegalArgumentException

class LoadingButtonViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoadingButtonViewModel::class.java)){
            return LoadingButtonViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
