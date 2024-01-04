package com.example.dogapp

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogapp.model.PictureModel
import com.example.dogapp.utils.PictureRepository
import com.example.dogapp.utils.RetrofitClient
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PictureViewModel(private val context: Context) : ViewModel() {
    private val repository = PictureRepository(RetrofitClient.instance)

    private val _picture = MutableLiveData<PictureModel>()
    val picture: LiveData<PictureModel>
        get() = _picture

    fun getRandomDogImage() {
        viewModelScope.launch {
            try {
                val response = repository.getRandomDogImage()
                _picture.value = response
            } catch (e: Exception) {
                Log.e("PictureViewModel", "Error fetching picture", e)
            }
        }
    }

    fun saveCurrentImage() {
        _picture.value?.let { picture ->
            val sharedPref = context.getSharedPreferences("DogAppPrefs", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            val currentSet = sharedPref.getStringSet("savedImages", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
            currentSet.add(picture.message)
            editor.putStringSet("savedImages", currentSet)
            editor.apply()
        }
    }
}