package com.example.assignmentapp.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.assignmentapp.R
import com.example.assignmentapp.databinding.ItemRowNewsBinding
import com.example.assignmentapp.domain.model.NewsItem

class NewsPagingAdapter(
    var onItemClicked: (NewsItem) -> Unit
) : PagingDataAdapter<NewsItem, NewsPagingAdapter.NewsViewHolder>(NewsDiffCallback()) {

    inner class NewsViewHolder(private val binding: ItemRowNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(newsItem: NewsItem) {
            bindDataToUi(newsItem)

            itemView.setOnClickListener {
                this@NewsPagingAdapter.onItemClicked.invoke(newsItem)
            }
        }

        private fun bindDataToUi(newsItem: NewsItem) {

            Glide.with(binding.root.context)
                .load(newsItem.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageThumbnail)

            binding.textTitle.text = newsItem.title

            val favColor = if (newsItem.isFavorite) {
                android.R.color.holo_red_light
            } else {
                android.R.color.white
            }

            binding.icFavourite.setColorFilter(
                binding.root.context.getColor(favColor),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsViewHolder {
        val inflate = ItemRowNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = getItem(position)
        newsItem?.let { holder.onBind(newsItem) }
    }

    class NewsDiffCallback : DiffUtil.ItemCallback<NewsItem>() {
        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem == newItem
        }
    }
}