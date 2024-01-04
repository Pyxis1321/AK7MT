package com.example.dogapp.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dogapp.PictureViewModel

class PictureViewModelFactory(private val context: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PictureViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PictureViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}