package com.amartinez.musicplayer.presentation.detail.view

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.amartinez.musicplayer.R
import com.amartinez.musicplayer.databinding.FragmentDetailBinding
import com.amartinez.musicplayer.domain.model.Result
import com.amartinez.musicplayer.presentation.player.controller.EventHandler
import com.amartinez.musicplayer.presentation.player.controller.MediaController
import com.bumptech.glide.Glide

class DetailFragment : Fragment(), EventHandler {

    private lateinit var binder: FragmentDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        retainInstance = true
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MediaController.instance?.setEventHandler(this)
        val result: Result = (requireArguments().getSerializable("result") as Result)
        Glide.with(requireContext())
            .load(result.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .into(binder.ivAlbum)
        binder.tvAlbumName.text = getString(R.string.song_detail_album, result.collectionName)
        binder.tvBandName.text = getString(R.string.song_detail_artist, result.artistName)
        binder.ivPlayPause.setOnClickListener(object : View.OnClickListener {
            var isPlaying = false
            override fun onClick(view: View) {
                if (isNetworkConnected()) {
                    if (!isPlaying)
                        MediaController.instance?.playAudio(result)
                    else
                        MediaController.instance?.pauseAudio()

                    isPlaying = !isPlaying
                } else {
                    Toast.makeText(context, resources.getString(R.string.song_detail_no_internet),
                        Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun playPauseEvent(play: Boolean) {
        if (play) {
            binder.ivPlayPause.setImageResource(R.drawable.pause_ic)
        } else {
            binder.ivPlayPause.setImageResource(R.drawable.play_ic)
        }
    }

    private fun isNetworkConnected() : Boolean {
        val cm =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }
}
