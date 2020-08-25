package com.amartinez.musicplayer.presentation.search.di

import com.amartinez.musicplayer.di.ProviderModule
import com.amartinez.musicplayer.presentation.search.view.SearchFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ProviderModule::class, SearchModule::class])
interface SearchComponent {
    fun inject(searchFragment: SearchFragment)
}