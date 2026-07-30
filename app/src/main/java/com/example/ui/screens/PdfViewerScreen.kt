package com.example.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.TocRepository
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldPrimary
import com.example.ui.viewmodel.PdfViewerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewerScreen(
    viewModel: PdfViewerViewModel,
    initialPage: Int = 1,
    onBackClick: () -> Unit
) {
    val currentPage by viewModel.currentPage.collectAsState()
    val currentBitmap by viewModel.currentBitmap.collectAsState()
    val isBookmarked by viewModel.isBookmarked.collectAsState()
    val showDialog by viewModel.showGoToPageDialog.collectAsState()
    val totalPages = viewModel.totalPages

    LaunchedEffect(initialPage) {
        if (initialPage in 1..totalPages) {
            viewModel.setPage(initialPage)
        }
    }

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 3.5f)
        if (scale > 1f) {
            offset += offsetChange
        } else {
            offset = Offset.Zero
        }
    }

    // Reset zoom state on page change
    LaunchedEffect(currentPage) {
        scale = 1f
        offset = Offset.Zero
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = TocRepository.getTitleForPage(currentPage),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary,
                            maxLines = 1
                        )
                        Text(
                            text = "Halaman $currentPage dari $totalPages",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("pdf_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = GoldPrimary
                        )
                    }
                },
                actions = {
                    // Go to Page
                    IconButton(
                        onClick = { viewModel.toggleGoToPageDialog(true) },
                        modifier = Modifier.testTag("pdf_jump_page_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tag,
                            contentDescription = "Lompat Halaman",
                            tint = GoldPrimary
                        )
                    }

                    // Toggle Bookmark
                    IconButton(
                        onClick = { viewModel.toggleBookmark() },
                        modifier = Modifier.testTag("pdf_bookmark_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) GoldPrimary else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EmeraldPrimary)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = EmeraldPrimary,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    // Page Navigation Controls Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { viewModel.previousPage() },
                            enabled = currentPage > 1,
                            modifier = Modifier.testTag("pdf_prev_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.NavigateBefore,
                                contentDescription = "Sebelumnya",
                                tint = if (currentPage > 1) GoldPrimary else Color.Gray,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        // Zoom Out / Reset / Zoom In
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    scale = (scale - 0.25f).coerceAtLeast(1f)
                                    if (scale == 1f) offset = Offset.Zero
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ZoomOut,
                                    contentDescription = "Zoom Out",
                                    tint = GoldAccent
                                )
                            }

                            TextButton(
                                onClick = {
                                    scale = 1f
                                    offset = Offset.Zero
                                }
                            ) {
                                Text(
                                    text = "${(scale * 100).toInt()}%",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                )
                            }

                            IconButton(
                                onClick = { scale = (scale + 0.25f).coerceAtMost(3.5f) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ZoomIn,
                                    contentDescription = "Zoom In",
                                    tint = GoldAccent
                                )
                            }
                        }

                        IconButton(
                            onClick = { viewModel.nextPage() },
                            enabled = currentPage < totalPages,
                            modifier = Modifier.testTag("pdf_next_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.NavigateNext,
                                contentDescription = "Selanjutnya",
                                tint = if (currentPage < totalPages) GoldPrimary else Color.Gray,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    // Page Slider
                    Slider(
                        value = currentPage.toFloat(),
                        onValueChange = { pageVal ->
                            viewModel.setPage(pageVal.toInt())
                        },
                        valueRange = 1f..totalPages.toFloat(),
                        steps = (totalPages - 2).coerceAtLeast(0),
                        colors = SliderDefaults.colors(
                            thumbColor = GoldPrimary,
                            activeTrackColor = GoldAccent,
                            inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF1E2825))
                .transformable(state = transformState),
            contentAlignment = Alignment.Center
        ) {
            val bitmap = currentBitmap
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Halaman $currentPage",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offset.x,
                            translationY = offset.y
                        )
                        .shadow(8.dp, RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
            } else {
                CircularProgressIndicator(color = GoldPrimary)
            }
        }
    }

    // Go To Page Dialog
    if (showDialog) {
        GoToPageDialog(
            currentPage = currentPage,
            totalPages = totalPages,
            onDismiss = { viewModel.toggleGoToPageDialog(false) },
            onConfirm = { page ->
                viewModel.setPage(page)
                viewModel.toggleGoToPageDialog(false)
            }
        )
    }
}

@Composable
private fun GoToPageDialog(
    currentPage: Int,
    totalPages: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var pageInput by remember { mutableStateOf(currentPage.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Lompat ke Halaman",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary
            )
        },
        text = {
            Column {
                Text(
                    text = "Masukkan nomor halaman (1 - $totalPages):",
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = pageInput,
                    onValueChange = { pageInput = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val pageNum = pageInput.toIntOrNull()
                    if (pageNum != null && pageNum in 1..totalPages) {
                        onConfirm(pageNum)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text("Buka", color = GoldPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
