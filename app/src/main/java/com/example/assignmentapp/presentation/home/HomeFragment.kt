package com.example.assignmentapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignmentapp.databinding.FragmentHomeBinding
import com.example.assignmentapp.domain.model.NewsItem
import com.example.assignmentapp.presentation.core.BaseFragment
import com.example.assignmentapp.presentation.extention.collectLatestLifeCycleFlow
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setLatestNewsRecycleView()
        setNewsFeedRecycleView()
        setUpObservers()
        setUpListener()
        setupChips()
        fetchData()
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

        this.collectLatestLifeCycleFlow(viewModel.latestNews) {
            setDataLatestNewsAdapter(it)
        }

        this.collectLatestLifeCycleFlow(viewModel.newsFeedPagination) {
            setDataAllNewsAdapter(it)
        }


    }

    private fun setUpListener() {
        binding.searchBar.setOnClickListener {
            navigateToAllNewsFragment(false)
        }

        binding.tvSeeAll.setOnClickListener {
            navigateToAllNewsFragment(true)
        }
    }

    private fun setupChips() {
        binding.chipGroupFilters.setOnCheckedStateChangeListener { group, checkedIds ->
            val chip = checkedIds.firstOrNull()?.let { group.findViewById<Chip>(it) }
            val category = chip?.text?.toString()?.lowercase()
            viewModel.updateCategory(category)
        }
    }

    private fun setLatestNewsRecycleView() {
        binding.rvLatestNews.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val latestNewsRecycleAdapter = NewsAdapter { news ->
            viewModel.selectNewsItem(news)
            navigateToNewsDetailFragment()
        }
        binding.rvLatestNews.adapter = latestNewsRecycleAdapter
    }

    private fun setNewsFeedRecycleView() {
        binding.rvNewsFeed.layoutManager = LinearLayoutManager(context)
        val feedNewsRecycleAdapter = NewsPagingAdapter { news ->
            viewModel.selectNewsItem(news)
            navigateToNewsDetailFragment()
        }
        binding.rvNewsFeed.adapter = feedNewsRecycleAdapter
    }

    private fun setDataLatestNewsAdapter(newsList: List<NewsItem>) {
        binding.rvLatestNews.adapter?.let {
            (it as NewsAdapter).setData(newsList)
        }
    }


    private fun setDataAllNewsAdapter(pagingData: PagingData<NewsItem>) {
        binding.rvNewsFeed.adapter?.let {
            (it as NewsPagingAdapter).submitData(lifecycle, pagingData)
        }
    }


    private fun navigateToAllNewsFragment(isSeeAllSelected: Boolean) {
        val action = HomeFragmentDirections.actionHomeFragmentToAllNewsFragment(isSeeAllSelected)
        findNavController().navigate(action)
    }

    private fun navigateToNewsDetailFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToNewsDetailFragment()
        findNavController().navigate(action)
    }

    private fun fetchData() {
        viewModel.fetchLatestNews(true)
    }


}