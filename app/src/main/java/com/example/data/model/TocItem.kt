package com.example.data.model

data class TocItem(
    val id: Int,
    val title: String,
    val arabicTitle: String,
    val pageNumber: Int,
    val category: String,
    val description: String = ""
)
