package com.slapstick.newsboyremaster.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.slapstick.newsboyremaster.R
import com.slapstick.newsboyremaster.adapters.NewsAdapter
import com.slapstick.newsboyremaster.data.models.Article
import com.slapstick.newsboyremaster.databinding.FragmentNewsBinding
import com.slapstick.newsboyremaster.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news),
    NewsAdapter.OnArticleClickListener {

    private val viewModel: NewsViewModel by viewModels()
    private val newsAdapter = NewsAdapter(this)

    private lateinit var searchView: SearchView

    private var _binding: FragmentNewsBinding? = null
    private val binding: FragmentNewsBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewsBinding.bind(view)

        setupRecyclerView()
        handleApiData()
        setHasOptionsMenu(true)

        binding.categoryChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedCategory = chip.text.toString()
            if (selectedCategory == "Breaking News") {
                viewModel.getAllNews("")
            } else {
                searchForNews(selectedCategory)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery = viewModel.currentQuery.value
        if (pendingQuery != null && pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, true)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean =
                if (query != null) {
                    searchView.clearFocus()
                    searchForNews(query)
                    true
                } else false

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.bookmarks) {
            findNavController().navigate(R.id.action_newsFragment_to_bookmarksFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun searchForNews(query: String) {
        viewModel.searchNewsPaging(query)
        binding.rvNews.scrollToPosition(0)
    }

    private fun setupRecyclerView() = binding.rvNews.apply {
        binding.rvNews.adapter = newsAdapter
        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun handleApiData() {
        viewModel.articles.observe(viewLifecycleOwner) {
            showContent(newsAdapter.itemCount > 0)
            newsAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun showContent(contentAvailable: Boolean = false) {
        binding.apply {
            if (contentAvailable && newsAdapter.itemCount > 0) {
                rvNews.isVisible
                ivError.isInvisible
                tvError.isInvisible
            } else {
                rvNews.isInvisible
                ivError.isVisible
                tvError.isVisible
            }
        }
    }

    override fun onArticleClicked(article: Article) {
        val action = NewsFragmentDirections.actionNewsFragmentToWebFragment(article)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        searchView.setOnQueryTextListener(null)
    }

}