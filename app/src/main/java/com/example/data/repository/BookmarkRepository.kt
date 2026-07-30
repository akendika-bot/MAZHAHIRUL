package com.example.data.repository

import com.example.data.database.BookmarkDao
import com.example.data.model.BookmarkEntity
import com.example.data.model.LastReadEntity
import kotlinx.coroutines.flow.Flow

class BookmarkRepository(private val dao: BookmarkDao) {
    val bookmarks: Flow<List<BookmarkEntity>> = dao.getAllBookmarks()
    val lastRead: Flow<LastReadEntity?> = dao.getLastRead()

    fun isBookmarked(pageNumber: Int): Flow<Boolean> = dao.isBookmarked(pageNumber)

    suspend fun addBookmark(pageNumber: Int, title: String, category: String) {
        dao.insertBookmark(BookmarkEntity(pageNumber = pageNumber, title = title, category = category))
    }

    suspend fun removeBookmark(pageNumber: Int) {
        dao.deleteBookmarkByPage(pageNumber)
    }

    suspend fun removeBookmarkById(id: Int) {
        dao.deleteBookmarkById(id)
    }

    suspend fun saveLastRead(pageNumber: Int, title: String) {
        dao.saveLastRead(LastReadEntity(pageNumber = pageNumber, title = title))
    }
}
