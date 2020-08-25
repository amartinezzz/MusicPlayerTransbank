package com.amartinez.musicplayer.presentation.detail.di

import com.amartinez.musicplayer.di.ProviderModule
import com.amartinez.musicplayer.presentation.detail.view.DetailFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ProviderModule::class, DetailModule::class])
interface DetailComponent {
    fun inject(detailFragment: DetailFragment)
}