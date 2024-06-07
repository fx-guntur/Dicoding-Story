package com.example.dicodingstory.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodingstory.databinding.LoadingPopupBinding

class HomeLoadStateAdapter(
    private val onRetry: () -> Unit
) : LoadStateAdapter<HomeLoadStateAdapter.LoadingStateViewHolder>() {

    class LoadingStateViewHolder(
        private val binding: LoadingPopupBinding,
        onRetry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.refreshBtn.setOnClickListener { onRetry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMessageTv.text = loadState.error.localizedMessage
            }

            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.refreshBtn.isVisible = loadState is LoadState.Error
            binding.errorMessageTv.isVisible = loadState is LoadState.Error
        }
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val binding =
            LoadingPopupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding, onRetry)
    }
}