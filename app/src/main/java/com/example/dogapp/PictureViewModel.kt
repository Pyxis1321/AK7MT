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
    // Instance of repository
    private val repository = PictureRepository(RetrofitClient.instance)
    // We make a LiveData observable so FirstFragment can see changes made to this variable
    private val _picture = MutableLiveData<PictureModel>()
    // Public version of the variable for outside exposure, adds abstraction
    val picture: LiveData<PictureModel>
        get() = _picture

    fun getRandomDogImage() {
        // Uses coroutine to call the API
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
        // method for saving image Urls, we first call for a file named DogAppPrefs, we create an
        // editor for him
        // Then we retrieve a set of strings with the key "savedImages"
        // As SharedPreferences returns a set that can't be modified we cast it toMutableSet which
        // creates a copy we can add a string to
        _picture.value?.let { picture ->
            val sharedPref = context.getSharedPreferences("DogAppPrefs", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            val currentSet = sharedPref.getStringSet("savedImages", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
            currentSet.add(picture.message)
            editor.putStringSet("savedImages", currentSet)
            editor.apply()
        }
    }

    fun deleteImage(imageUrl: String) {
        val sharedPref = context.getSharedPreferences("DogAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val currentSet = sharedPref.getStringSet("savedImages", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        currentSet.remove(imageUrl)
        editor.putStringSet("savedImages", currentSet)
        editor.apply()
    }
}