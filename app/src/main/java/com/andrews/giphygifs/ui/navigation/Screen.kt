package com.andrews.giphygifs.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Home: Screen
    @Serializable
    data class Details(val gifIndex: Int): Screen
}