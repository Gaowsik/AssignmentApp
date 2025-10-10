package com.example.assignmentapp.presentation.newsDetail

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
    private var isFavorite = false
    private var currentNewsItem: NewsItem? = null
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

    private fun init() {
        setUpObservers()
        setUpListener()
    }

    private fun setUpObservers() {
        this.collectLatestLifeCycleFlow(viewModel.selectedNewsItem) {
            if (it != null) {
                currentNewsItem = it
                bindValuesToUI(it)
                isFavorite = it.isFavorite
            }
        }

        collectLatestLifeCycleFlow(viewModel.isFavoriteSuccessful) {
            isFavorite = true
            updateFavoriteIcon(true)
        }

        collectLatestLifeCycleFlow(viewModel.isFavoriteRemoved) {
            isFavorite = false
            updateFavoriteIcon(false)
        }


    }

    private fun setUpListener() {
        binding.btnBack.setOnClickListener {
            navigateToHomeFragment()
        }

        binding.btnFavorite.setOnClickListener {
            currentNewsItem?.let { news ->
                if (isFavorite) {
                    viewModel.removeFavorite(news)
                } else {
                    viewModel.addFavorite(news)
                }
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

            updateFavoriteIcon(news.isFavorite)
        }
    }

    private fun navigateToHomeFragment() {
        val action = NewsDetailFragmentDirections.actionNewsDetailFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    private fun updateFavoriteIcon(isFav: Boolean) {
        val colorRes = if (isFav) {
            android.R.color.holo_red_light
        } else {
            android.R.color.darker_gray
        }
        val color = ContextCompat.getColor(requireContext(), colorRes)
        binding.btnFavorite.imageTintList = ColorStateList.valueOf(color)
    }


}