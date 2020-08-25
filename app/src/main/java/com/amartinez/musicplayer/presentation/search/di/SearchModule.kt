package com.amartinez.musicplayer.presentation.search.di

import androidx.fragment.app.Fragment
import com.amartinez.musicplayer.domain.usecase.SearchUseCase
import com.amartinez.musicplayer.domain.usecase.local.SaveSearchUseCase
import com.amartinez.musicplayer.domain.usecase.local.SearchLocalUseCase
import com.amartinez.musicplayer.presentation.search.SearchContract
import com.amartinez.musicplayer.presentation.search.presenter.SearchPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class SearchModule(private val owner: Fragment) {

    @Singleton
    @Provides
    fun provideViewModel(searchUseCase: SearchUseCase, saveSearchUseCase: SaveSearchUseCase,
                         searchLocalUseCase: SearchLocalUseCase
    ): SearchContract.Presenter {
        return SearchPresenter.getInstance(searchUseCase, saveSearchUseCase, searchLocalUseCase)
    }
}