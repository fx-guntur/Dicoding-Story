package com.example.dicodingstory.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dicodingstory.data.model.Story

@Dao
interface DicodingStoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStories(users: List<Story>)

    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, Story>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}