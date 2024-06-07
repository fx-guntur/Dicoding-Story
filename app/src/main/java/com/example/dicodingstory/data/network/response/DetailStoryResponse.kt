package com.example.dicodingstory.data.network.response

import com.example.dicodingstory.data.model.Story
import com.google.gson.annotations.SerializedName

data class DetailStoryResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("story")
    val story: Story
)

