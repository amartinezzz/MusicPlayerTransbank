package com.amartinez.musicplayer.presentation.detail.di

import androidx.fragment.app.Fragment
import com.amartinez.musicplayer.domain.usecase.LoadAlbumUseCase
import com.amartinez.musicplayer.presentation.detail.DetailContract
import com.amartinez.musicplayer.presentation.detail.presenter.DetailPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class DetailModule(private val owner: Fragment) {

    @Singleton
    @Provides
    fun provideViewModel(loadAlbumUseCase: LoadAlbumUseCase): DetailContract.Presenter {
        return DetailPresenter.getInstance(loadAlbumUseCase)
    }
}