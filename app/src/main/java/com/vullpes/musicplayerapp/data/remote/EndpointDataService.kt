package com.vullpes.musicplayerapp.data.remote

import com.vullpes.musicplayerapp.data.local.model.Music
import retrofit2.Response
import retrofit2.http.GET

interface EndpointDataService {

    @GET("catalog.json")
    suspend fun getMusicCatalog(): Response<Music>
}