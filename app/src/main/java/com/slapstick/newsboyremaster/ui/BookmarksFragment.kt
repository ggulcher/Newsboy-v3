package com.slapstick.newsboyremaster.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.slapstick.newsboyremaster.R
import com.slapstick.newsboyremaster.adapters.NewsAdapter
import com.slapstick.newsboyremaster.data.models.Article
import com.slapstick.newsboyremaster.databinding.FragmentBookmarksBinding
import com.slapstick.newsboyremaster.util.snackbarAction
import com.slapstick.newsboyremaster.viewmodels.BookmarksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarksFragment : Fragment(R.layout.fragment_bookmarks), NewsAdapter.OnArticleClickListener {

    private val viewModel: BookmarksViewModel by viewModels()
    private val newsAdapter = NewsAdapter(this)

    private lateinit var binding: FragmentBookmarksBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBookmarksBinding.bind(view)

        setupRecyclerView()
        newsObserver()
        applySwipeToDelete()
    }

    private fun setupRecyclerView() = binding.rvBookmarks.apply {
        adapter = newsAdapter
        layoutManager = LinearLayoutManager(activity)
    }

    private fun newsObserver() {
        viewModel.getSavedNews().observe(viewLifecycleOwner) {
            newsAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.from(it))
        }
    }

    private fun applySwipeToDelete() {
        val touchHelper =
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.bindingAdapterPosition
                    val currentItem = newsAdapter.getCurrentItem(position)
                    currentItem?.let {
                        viewModel.deleteArticle(it)
                        binding.root.snackbarAction("Article deleted", "Undo") {
                            viewModel.saveArticle(it)
                        }
                    }
                }
            }
        ItemTouchHelper(touchHelper).attachToRecyclerView(binding.rvBookmarks)
    }

    override fun onArticleClicked(article: Article) {
        val action = BookmarksFragmentDirections.actionBookmarksFragmentToWebFragment(article)
        findNavController().navigate(action)
    }

}