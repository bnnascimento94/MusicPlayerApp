package com.vullpes.musicplayerapp.di

import com.vullpes.musicplayerapp.data.local.ContentResolverHelper
import com.vullpes.musicplayerapp.data.repository.MusicRepositoryImpl
import com.vullpes.musicplayerapp.domain.ListMusicUseCase
import com.vullpes.musicplayerapp.domain.MusicRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun providesMusicRepository(contentResolver: ContentResolverHelper): MusicRepository{
        return MusicRepositoryImpl(contentResolver)
    }

    @Provides
    @Singleton
    fun providesListMusicUseCase(
        musicRepository: MusicRepository
    ): ListMusicUseCase{
        return ListMusicUseCase(musicRepository)
    }



}