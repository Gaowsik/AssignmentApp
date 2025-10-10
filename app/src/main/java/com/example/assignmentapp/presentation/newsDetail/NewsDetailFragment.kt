package com.example.assignmentapp.presentation.newsDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.assignmentapp.R
import com.example.assignmentapp.databinding.FragmentNewsDetailBinding
import com.example.assignmentapp.domain.model.NewsItem
import com.example.assignmentapp.presentation.core.BaseFragment
import com.example.assignmentapp.presentation.extention.collectLatestLifeCycleFlow
import com.example.assignmentapp.presentation.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailFragment : BaseFragment() {
    private lateinit var binding: FragmentNewsDetailBinding
    private val viewModel: HomeViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()


    }

    fun init() {
        this.collectLatestLifeCycleFlow(viewModel.selectedNewsItem) {
            if (it != null) {
                bindValuesToUI(it)
            }
        }
    }

    private fun bindValuesToUI(news: NewsItem) {
        with(binding) {
            textTitle.text = news.title
            textAuthor.text = news.author ?: getString(R.string.title_unknown_author)
            textDate.text = news.publishedAt
            textDescription.text = news.description

            Glide.with(this@NewsDetailFragment)
                .load(news.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageNews)

            // updateFavoriteIcon(news.isFavorite)
        }
    }



}