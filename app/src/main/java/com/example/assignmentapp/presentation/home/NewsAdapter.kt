package com.example.assignmentapp.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.assignmentapp.R
import com.example.assignmentapp.databinding.ItemRowNewsBinding
import com.example.assignmentapp.domain.model.NewsItem

class NewsAdapter(
    var onItemClicked: (NewsItem) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var newsList = mutableListOf<NewsItem>()

    inner class NewsViewHolder(private val binding: ItemRowNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(newsItem: NewsItem) {
            bindDataToUi(newsItem)

            itemView.setOnClickListener {
                this@NewsAdapter.onItemClicked.invoke(newsItem)
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
        }
    }

    fun setData(newsList: List<NewsItem>) {
        this.newsList.clear()
        this.newsList.addAll(newsList)
        notifyDataSetChanged()
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
        holder.onBind(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size
}