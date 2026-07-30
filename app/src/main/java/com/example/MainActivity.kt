package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.*
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.MazhahirulKhairatTheme
import com.example.ui.viewmodel.*

class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val pdfViewerViewModel: PdfViewerViewModel by viewModels()
    private val bookmarkViewModel: BookmarkViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkMode by settingsViewModel.isDarkMode.collectAsState()

            MazhahirulKhairatTheme(darkTheme = isDarkMode) {
                var isSplashFinished by remember { mutableStateOf(false) }
                var selectedTab by remember { mutableIntStateOf(0) }
                var activePdfPage by remember { mutableStateOf<Int?>(null) }
                var isSearchActive by remember { mutableStateOf(false) }
                var isAboutActive by remember { mutableStateOf(false) }

                if (!isSplashFinished) {
                    SplashScreen(onSplashFinished = { isSplashFinished = true })
                } else if (activePdfPage != null) {
                    PdfViewerScreen(
                        viewModel = pdfViewerViewModel,
                        initialPage = activePdfPage ?: 1,
                        onBackClick = { activePdfPage = null }
                    )
                } else if (isSearchActive) {
                    SearchScreen(
                        onOpenPdfPage = { page ->
                            if (page > 0) {
                                activePdfPage = page
                            }
                            isSearchActive = false
                        },
                        onBackClick = { isSearchActive = false }
                    )
                } else if (isAboutActive) {
                    AboutScreen(
                        onBackClick = { isAboutActive = false }
                    )
                } else {
                    Scaffold(
                        bottomBar = {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(12.dp),
                                color = EmeraldPrimary
                            ) {
                                NavigationBar(
                                    containerColor = EmeraldPrimary,
                                    tonalElevation = 8.dp,
                                    modifier = Modifier
                                        .windowInsetsPadding(WindowInsets.navigationBars)
                                        .testTag("main_bottom_nav")
                                ) {
                                    NavigationBarItem(
                                        selected = selectedTab == 0,
                                        onClick = { selectedTab = 0 },
                                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                        label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = EmeraldPrimary,
                                            selectedTextColor = GoldPrimary,
                                            indicatorColor = GoldPrimary,
                                            unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                            unselectedTextColor = Color.White.copy(alpha = 0.7f)
                                        ),
                                        modifier = Modifier.testTag("nav_tab_home")
                                    )

                                    NavigationBarItem(
                                        selected = selectedTab == 1,
                                        onClick = { selectedTab = 1 },
                                        icon = { Icon(Icons.Default.List, contentDescription = "Daftar Isi") },
                                        label = { Text("Daftar Isi", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = EmeraldPrimary,
                                            selectedTextColor = GoldPrimary,
                                            indicatorColor = GoldPrimary,
                                            unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                            unselectedTextColor = Color.White.copy(alpha = 0.7f)
                                        ),
                                        modifier = Modifier.testTag("nav_tab_toc")
                                    )

                                    NavigationBarItem(
                                        selected = selectedTab == 2,
                                        onClick = { selectedTab = 2 },
                                        icon = { Icon(Icons.Default.Bookmark, contentDescription = "Bookmark") },
                                        label = { Text("Bookmark", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = EmeraldPrimary,
                                            selectedTextColor = GoldPrimary,
                                            indicatorColor = GoldPrimary,
                                            unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                            unselectedTextColor = Color.White.copy(alpha = 0.7f)
                                        ),
                                        modifier = Modifier.testTag("nav_tab_bookmark")
                                    )

                                    NavigationBarItem(
                                        selected = selectedTab == 3,
                                        onClick = { selectedTab = 3 },
                                        icon = { Icon(Icons.Default.Settings, contentDescription = "Pengaturan") },
                                        label = { Text("Pengaturan", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = EmeraldPrimary,
                                            selectedTextColor = GoldPrimary,
                                            indicatorColor = GoldPrimary,
                                            unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                            unselectedTextColor = Color.White.copy(alpha = 0.7f)
                                        ),
                                        modifier = Modifier.testTag("nav_tab_settings")
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            Crossfade(
                                targetState = selectedTab,
                                animationSpec = tween(300),
                                label = "TabSwitch"
                            ) { tab ->
                                when (tab) {
                                    0 -> HomeScreen(
                                        viewModel = homeViewModel,
                                        onOpenPdfPage = { page ->
                                            if (page == 0) {
                                                isSearchActive = true
                                            } else {
                                                activePdfPage = page
                                            }
                                        },
                                        onNavigateTab = { newTab -> selectedTab = newTab }
                                    )
                                    1 -> DaftarIsiScreen(
                                        onOpenPdfPage = { page -> activePdfPage = page }
                                    )
                                    2 -> BookmarkScreen(
                                        viewModel = bookmarkViewModel,
                                        onOpenPdfPage = { page -> activePdfPage = page }
                                    )
                                    3 -> SettingsScreen(
                                        viewModel = settingsViewModel,
                                        onNavigateToAbout = { isAboutActive = true }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
