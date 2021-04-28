package com.slapstick.newsboyremaster.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.slapstick.newsboyremaster.R
import com.slapstick.newsboyremaster.data.models.Article
import com.slapstick.newsboyremaster.databinding.RecyclerNewsItemBinding

class NewsAdapter(private val onArticleClickListener: OnArticleClickListener) :
    PagingDataAdapter<Article, NewsAdapter.NewsViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding =
            RecyclerNewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentArticle: Article? = getItem(position)
        if (currentArticle != null) holder.bindData(currentArticle)
    }

    fun getCurrentItem(position: Int) = getItem(position)

    inner class NewsViewHolder(private val binding: RecyclerNewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentArticle = getItem(position)
                    if (currentArticle != null) onArticleClickListener.onArticleClicked(
                        currentArticle
                    )
                }
            }
        }

        fun bindData(article: Article) {
            binding.apply {
                tvNewsTitle.text = article.title
                tvNewsDescription.text = article.description
                tvNewsSource.text = article.source?.name
//                tvArticleDescription.text = article.description
//                tvArticlePublishedAt.text = article.publishedAt

                loadImage(article)
            }
        }

        private fun RecyclerNewsItemBinding.loadImage(article: Article) {
            Glide.with(this.root)
                .load(article.urlToImage)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(ivNewsImage)
        }

    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == oldItem.url
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnArticleClickListener {
        fun onArticleClicked(article: Article)
    }
}