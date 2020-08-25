package com.amartinez.musicplayer.presentation.player.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amartinez.musicplayer.R
import io.realm.Realm


class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        Realm.init(this)
    }
}