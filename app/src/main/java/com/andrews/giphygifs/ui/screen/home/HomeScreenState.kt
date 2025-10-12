package com.andrews.giphygifs.ui.screen.home

data class HomeScreenState(
    val error: String? = null,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false
)
