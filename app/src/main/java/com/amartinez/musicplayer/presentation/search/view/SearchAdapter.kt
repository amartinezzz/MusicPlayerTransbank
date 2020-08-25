package com.amartinez.musicplayer.presentation.search.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.amartinez.musicplayer.R
import com.amartinez.musicplayer.databinding.ItemSearchBinding
import com.amartinez.musicplayer.domain.model.Result
import com.amartinez.musicplayer.presentation.search.view.SearchAdapter.SearchHolder
import com.bumptech.glide.Glide

class SearchAdapter(private val context: Context,
                private val navController: NavController) : RecyclerView.Adapter<SearchHolder>() {

    private var results: ArrayList<Result> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val view: View
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_search, parent, false
        )
        view = binding.root

        return SearchHolder(view)
    }

    fun addItems(list: List<Result>) {
        results.addAll(list)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        holder.bind(position, results.get(position))
    }

    override fun getItemCount(): Int {
        return results.size
    }

    inner class SearchHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(position: Int, result: Result) {
            val binder = DataBindingUtil.getBinding<ItemSearchBinding>(view)

            binder?.ivAlbum?.let {
                Glide.with(context)
                    .load(result.artworkUrl30)
                    .placeholder(R.drawable.placeholder)
                    .into(it)
            }
            binder?.tvArtist?.text = result.artistName
            binder?.tvSong?.text = result.trackName
            val onClickListener = View.OnClickListener {
                val bundle = bundleOf("result" to result)
                    navController.navigate(R.id.action_searchFragment_to_detailFragment, bundle)
                }
            binder?.ivAlbum?.setOnClickListener(onClickListener)
            binder?.tvArtist?.setOnClickListener(onClickListener)
            binder?.tvSong?.setOnClickListener(onClickListener)
        }
    }
}