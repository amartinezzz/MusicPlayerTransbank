package com.amartinez.musicplayer.presentation.detail.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.amartinez.musicplayer.R
import com.amartinez.musicplayer.databinding.ItemDetailBinding
import com.amartinez.musicplayer.domain.model.Result
import com.amartinez.musicplayer.presentation.player.controller.MediaController

class DetailAdapter() : RecyclerView.Adapter<DetailAdapter.DetailHolder>() {

    private var results: ArrayList<Result> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailHolder {
        val view: View
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_detail, parent, false
        )
        view = binding.root

        return DetailHolder(view)
    }

    fun addItems(list: List<Result>) {
        results.addAll(list)
    }

    override fun onBindViewHolder(holder: DetailHolder, position: Int) {
        holder.bind(position, results.get(position))
    }

    override fun getItemCount(): Int {
        return results.size
    }

    inner class DetailHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(position: Int, result: Result) {
            val binder = DataBindingUtil.getBinding<ItemDetailBinding>(view)

            binder?.tvSong?.text = result.trackName
            val onClickListener = View.OnClickListener {
                val bundle = bundleOf("result" to result)
                MediaController.instance?.playAudio(result)
            }
            binder?.tvSong?.setOnClickListener(onClickListener)
        }
    }
}