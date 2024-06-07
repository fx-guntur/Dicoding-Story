package com.example.dicodingstory.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dicodingstory.data.local.dao.DicodingStoryDao
import com.example.dicodingstory.data.local.dao.RemoteKeyDao
import com.example.dicodingstory.data.model.RemoteKeys
import com.example.dicodingstory.data.model.Story

@Database(
    entities = [Story::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)
abstract class DicodingStoryDatabase : RoomDatabase() {
    abstract fun getDicodingStoryDao(): DicodingStoryDao
    abstract fun getRemoteKeysDao(): RemoteKeyDao
}