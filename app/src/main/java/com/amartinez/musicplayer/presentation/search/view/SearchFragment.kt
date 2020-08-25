package com.amartinez.musicplayer.presentation.search.view

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amartinez.musicplayer.R
import com.amartinez.musicplayer.databinding.FragmentSearchBinding
import com.amartinez.musicplayer.domain.model.Result
import com.amartinez.musicplayer.presentation.search.SearchContract
import com.amartinez.musicplayer.presentation.search.di.DaggerSearchComponent
import com.amartinez.musicplayer.presentation.search.di.SearchModule
import javax.inject.Inject

class SearchFragment : Fragment(), SearchContract.View {
    @Inject
    lateinit var presenter: SearchContract.Presenter
    private lateinit var binder: FragmentSearchBinding
    private lateinit var adapter: SearchAdapter
    private var isLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        retainInstance = true
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.initialize(this)
        initAdapter()
        binder.bSearch.setOnClickListener {
            if (binder.etSearch.text.toString().isNotEmpty()) {
                presenter.search(binder.etSearch.text.toString(), isNetworkConnected())
                hideSoftKeyboard(binder.bSearch)
            } else {
                binder.etSearch.error = getString(R.string.search_error)
            }
        }
    }

    private fun injectDependencies() {
        DaggerSearchComponent.builder().searchModule(SearchModule(this)).build().inject(this)
    }

    private fun initAdapter() {
        adapter =
            SearchAdapter(
                requireContext(),
                findNavController()
            )
        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binder.rvSearch.layoutManager = linearLayoutManager
        binder.rvSearch.adapter = adapter
        binder.rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                        presenter.loadMore(isNetworkConnected())
                        isLoading = true
                    }
                }
            }
        })

        displaySearchResults(presenter.lastSearch())
    }

    override fun displaySearchResults(results: List<Result>) {
        adapter.addItems(results)
        adapter.notifyDataSetChanged()
        isLoading = false
    }

    override fun showError() {
        Toast.makeText(context, getString(R.string.search_no_result), Toast.LENGTH_LONG).show()
    }

    private fun hideSoftKeyboard(view: View?) {
        if (view != null) {
            val imm = (view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun isNetworkConnected() : Boolean {
        val cm =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }
}