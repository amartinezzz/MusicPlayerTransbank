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
import androidx.recyclerview.widget.LinearLayoutManager
import com.amartinez.musicplayer.R
import com.amartinez.musicplayer.databinding.FragmentDetailBinding
import com.amartinez.musicplayer.domain.model.Result
import com.amartinez.musicplayer.presentation.detail.DetailContract
import com.amartinez.musicplayer.presentation.detail.di.DaggerDetailComponent
import com.amartinez.musicplayer.presentation.detail.di.DetailModule
import com.amartinez.musicplayer.presentation.player.controller.EventHandler
import com.amartinez.musicplayer.presentation.player.controller.MediaController
import com.bumptech.glide.Glide
import javax.inject.Inject

class DetailFragment : Fragment(), EventHandler, DetailContract.View {

    @Inject
    lateinit var presenter: DetailContract.Presenter
    private lateinit var binder: FragmentDetailBinding
    private lateinit var adapter: DetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        retainInstance = true
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MediaController.instance?.setEventHandler(this)
        initAdapter()

        val result: Result = (requireArguments().getSerializable("result") as Result)
        presenter.initialize(this)
        presenter.loadAlbum(result.collectionId, isNetworkConnected())

        Glide.with(requireContext())
            .load(result.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .into(binder.ivAlbum)
        binder.tvAlbumName.text = getString(R.string.detail_album, result.collectionName)
        binder.tvBandName.text = getString(R.string.detail_artist, result.artistName)
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
                    Toast.makeText(context, resources.getString(R.string.detail_no_internet),
                        Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun injectDependencies() {
        DaggerDetailComponent.builder().detailModule(DetailModule(this)).build().inject(this)
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

    override fun displaySongs(results: List<Result>) {
        adapter.addItems(results)
        adapter.notifyDataSetChanged()
    }

    override fun showError() {
        binder.tvSongsTitle.visibility = View.GONE
    }

    private fun initAdapter() {
        adapter = DetailAdapter()
        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binder.rvSongs.layoutManager = linearLayoutManager
        binder.rvSongs.adapter = adapter
    }
}
