package com.example.dicodingstory.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingstory.data.model.Story
import com.example.dicodingstory.databinding.StoryItemRowBinding

class HomeAdapter(private val onItemClickCallback: (String) -> Unit) :
    PagingDataAdapter<Story, HomeAdapter.StoryDataViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryDataViewHolder {
        val binding =
            StoryItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryDataViewHolder, position: Int) {
        val storyData = getItem(position)
        if (storyData != null) {
            holder.bind(storyData)
        }
    }

    inner class StoryDataViewHolder(private val binding: StoryItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            with(binding) {
                textView.text = story.name
                textView2.text = story.description
                Glide.with(root.context)
                    .load(story.photoUrl)
                    .into(imageView2)
                root.setOnClickListener {
                    onItemClickCallback(story.id)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(
                oldItem: Story,
                newItem: Story
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Story,
                newItem: Story
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}