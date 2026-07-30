package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.BookmarkEntity
import com.example.data.model.LastReadEntity
import com.example.data.repository.BookmarkRepository
import com.example.data.repository.TocRepository
import com.example.service.PdfManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PdfViewerViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = BookmarkRepository(db.bookmarkDao())
    val pdfManager = PdfManager(application)

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _currentBitmap = MutableStateFlow<Bitmap?>(null)
    val currentBitmap: StateFlow<Bitmap?> = _currentBitmap.asStateFlow()

    private val _zoomScale = MutableStateFlow(1.0f)
    val zoomScale: StateFlow<Float> = _zoomScale.asStateFlow()

    private val _isBookmarked = MutableStateFlow(false)
    val isBookmarked: StateFlow<Boolean> = _isBookmarked.asStateFlow()

    private val _showGoToPageDialog = MutableStateFlow(false)
    val showGoToPageDialog: StateFlow<Boolean> = _showGoToPageDialog.asStateFlow()

    val totalPages: Int get() = pdfManager.totalPages

    init {
        loadPage(1)
    }

    fun setPage(pageNumber: Int) {
        val page = pageNumber.coerceIn(1, totalPages)
        _currentPage.value = page
        _zoomScale.value = 1.0f
        loadPage(page)
        saveLastReadPage(page)
        checkBookmarkStatus(page)
    }

    fun nextPage() {
        if (_currentPage.value < totalPages) {
            setPage(_currentPage.value + 1)
        }
    }

    fun previousPage() {
        if (_currentPage.value > 1) {
            setPage(_currentPage.value - 1)
        }
    }

    fun zoomIn() {
        _zoomScale.value = (_zoomScale.value + 0.25f).coerceAtMost(3.0f)
    }

    fun zoomOut() {
        _zoomScale.value = (_zoomScale.value - 0.25f).coerceAtLeast(1.0f)
    }

    fun resetZoom() {
        _zoomScale.value = 1.0f
    }

    fun toggleGoToPageDialog(show: Boolean) {
        _showGoToPageDialog.value = show
    }

    fun toggleBookmark() {
        viewModelScope.launch {
            val page = _currentPage.value
            if (_isBookmarked.value) {
                repository.removeBookmark(page)
                _isBookmarked.value = false
            } else {
                val title = TocRepository.getTitleForPage(page)
                val category = TocRepository.getCategoryForPage(page)
                repository.addBookmark(page, title, category)
                _isBookmarked.value = true
            }
        }
    }

    private fun loadPage(pageNumber: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val bitmap = pdfManager.renderPage(pageNumber - 1)
            withContext(Dispatchers.Main) {
                _currentBitmap.value = bitmap
            }
        }
    }

    private fun checkBookmarkStatus(pageNumber: Int) {
        viewModelScope.launch {
            repository.isBookmarked(pageNumber).collect { isBooked ->
                _isBookmarked.value = isBooked
            }
        }
    }

    private fun saveLastReadPage(pageNumber: Int) {
        viewModelScope.launch {
            val title = TocRepository.getTitleForPage(pageNumber)
            repository.saveLastRead(pageNumber, title)
        }
    }

    override fun onCleared() {
        super.onCleared()
        pdfManager.close()
    }
}
