package com.vullpes.musicplayerapp.di

import com.vullpes.musicplayerapp.data.remote.EndpointDataService
import com.vullpes.musicplayerapp.data.repository.MusicRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    fun providesCatalogRepository(endpointDataService: EndpointDataService):MusicRepositoryImpl{
        return MusicRepositoryImpl(endpointDataService)
    }

    @Provides
    @Singleton
    fun provideRemoteApi(): EndpointDataService {
        return Retrofit.Builder()
            .baseUrl("https://storage.googleapis.com/uamp/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EndpointDataService::class.java)
    }


}