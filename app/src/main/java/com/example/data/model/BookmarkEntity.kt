package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pageNumber: Int,
    val title: String,
    val category: String,
    val dateAdded: Long = System.currentTimeMillis()
)

@Entity(tableName = "last_read")
data class LastReadEntity(
    @PrimaryKey val id: Int = 1,
    val pageNumber: Int,
    val title: String,
    val timestamp: Long = System.currentTimeMillis()
)
