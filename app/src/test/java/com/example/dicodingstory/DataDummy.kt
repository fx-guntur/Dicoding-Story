package com.example.dicodingstory

import com.example.dicodingstory.data.model.Story

object DataDummy {
    fun emailTest() = "test@bobi.com"
    fun passwordTest() = "testbobi"
    fun tokenDummy() =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXNVMHUzX0NTamVnbXN1eTciLCJpYXQiOjE3MTcwNjQxMDh9.y-dHP9e9Edv-vO2-Nlo5m2Nmoze4uKVXj4x4fjLmkc8"

    fun generateDummyQuoteResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                id = "story-cq9-kQSmfUv3GoVJ-$i",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1717065178874_480663eb99667de031c9.jpg",
                createdAt = "2024-05-30T10:32:58.877Z",
                name = "Username",
                description = "Dummy description $i",
                latitude = (-6.18013).toFloat(),
                longitude = (106.720855).toFloat()
            )
            items.add(story)
        }
        return items
    }

    fun emptyListStoryDummy(): List<Story> = emptyList()
}