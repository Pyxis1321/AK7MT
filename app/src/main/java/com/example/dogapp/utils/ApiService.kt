package com.example.dogapp.utils

import com.example.dogapp.model.PictureModel
import retrofit2.http.GET

interface ApiService {
    @GET("api/breeds/image/random")
    suspend fun getRandomDogImage(): PictureModel
}