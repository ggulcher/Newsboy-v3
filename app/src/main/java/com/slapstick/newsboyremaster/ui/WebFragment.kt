package com.slapstick.newsboyremaster.ui

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.slapstick.newsboyremaster.R
import com.slapstick.newsboyremaster.databinding.FragmentWebBinding
import com.slapstick.newsboyremaster.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebFragment : Fragment(R.layout.fragment_web) {

    private val viewModel: NewsViewModel by viewModels()
    private val navigationArgs: WebFragmentArgs by navArgs()

    private lateinit var binding: FragmentWebBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWebBinding.bind(view)

        val article = navigationArgs.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        binding.floatingActionButton.setOnClickListener {
            viewModel.saveArticleClicked(article)
            Snackbar.make(
                view,
                "Article saved successfully",
                Snackbar.LENGTH_SHORT).show()
        }
    }

}