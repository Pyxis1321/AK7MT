package com.example.dogapp.utils

class PictureRepository(private val apiService: ApiService) {
    suspend fun getRandomDogImage() = apiService.getRandomDogImage()
}