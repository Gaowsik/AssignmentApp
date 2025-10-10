package com.example.assignmentapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentapp.databinding.FragmentAllNewsBinding
import com.example.assignmentapp.domain.model.NewsItem
import com.example.assignmentapp.presentation.core.BaseFragment
import com.example.assignmentapp.presentation.extention.collectLatestLifeCycleFlow
import com.example.assignmentapp.utils.AppConstants.DEFAULT_REQUEST_PAGE_SIZE
import com.example.assignmentapp.utils.AppConstants.SEARCH_BAR_TIME_DELAY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllNewsFragment : BaseFragment() {

    private lateinit var binding: FragmentAllNewsBinding
    val args: AllNewsFragmentArgs by navArgs()
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllNewsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()


    }

    fun init() {
        getAllNewsBasedOnNav()
        setAllNewsRecycleView()
        setUpObservers()
        setupPagination()
    }

    private fun getAllNewsBasedOnNav() {
        viewModel.fetchLatestNews(true, DEFAULT_REQUEST_PAGE_SIZE)

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
            setDataAllNewsAdapter(it)
        }

        this.collectLatestLifeCycleFlow(viewModel.newsFeed) {
            setDataAllNewsAdapter(it)
        }

        binding.editSearch.addTextChangedListener { editable ->
            val query = editable?.toString()?.trim() ?: ""
            lifecycleScope.launch {
                delay(SEARCH_BAR_TIME_DELAY)
                if (query.isNotEmpty()) {
                    viewModel.searchNews(query)
                } else {
                    viewModel.fetchNewsFeed(refresh = true)
                }
            }
        }

    }


    private fun setAllNewsRecycleView() {
        binding.rvAllNews.layoutManager =
            LinearLayoutManager(context)
        val allNewsRecycleAdapter = NewsAdapter { news ->
            // TODO: ()
        }
        binding.rvAllNews.adapter = allNewsRecycleAdapter
    }

    private fun setDataAllNewsAdapter(newsList: List<NewsItem>) {
        binding.rvAllNews.adapter?.let {
            (it as NewsAdapter).setData(newsList)
        }
    }

    private fun setupPagination() {
        binding.rvAllNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleCount = layoutManager.childCount
                    val totalCount = layoutManager.itemCount
                    val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                    if (!viewModel.isLoading.value && (visibleCount + firstVisibleItem) >= totalCount - 2) {
                        viewModel.loadMoreLatestNews()
                    }
                }
            }
        })
    }


}