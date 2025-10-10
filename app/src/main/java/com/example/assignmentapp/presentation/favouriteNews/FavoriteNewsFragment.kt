package com.example.assignmentapp.presentation.favouriteNews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignmentapp.databinding.FragmentFavoriteNewsBinding
import com.example.assignmentapp.domain.model.NewsItem
import com.example.assignmentapp.presentation.core.BaseFragment
import com.example.assignmentapp.presentation.extention.collectLatestLifeCycleFlow
import com.example.assignmentapp.presentation.home.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteNewsFragment : BaseFragment() {
    private lateinit var binding: FragmentFavoriteNewsBinding
    private val viewModel: FavoriteViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setFavoriteNewsRecycleView()
        setUpObservers()
        getFavoriteNews()
    }


    private fun setUpObservers() {
        this.collectLatestLifeCycleFlow(viewModel.errorMessage) {
            showMessageDialogWithOkAction(it)
        }

        this.collectLatestLifeCycleFlow(viewModel.isLoading) { isLoading ->
            if (isLoading) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        this.collectLatestLifeCycleFlow(viewModel.favoriteNews) {
            setDataFavoriteNewsRecycleAdapter(it)
        }
    }


    private fun setFavoriteNewsRecycleView() {
        binding.rvFavoriteNews.layoutManager = LinearLayoutManager(context)
        val favoriteNewsRecycleAdapter = NewsAdapter { news ->
        }
        binding.rvFavoriteNews.adapter = favoriteNewsRecycleAdapter
    }


    private fun setDataFavoriteNewsRecycleAdapter(newsList: List<NewsItem>) {
        binding.rvFavoriteNews.adapter?.let {
            (it as NewsAdapter).setData(newsList)
        }
    }

    private fun getFavoriteNews() {
        viewModel.fetchFavoriteNews()
    }

}