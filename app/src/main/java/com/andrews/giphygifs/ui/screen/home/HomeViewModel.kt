package com.andrews.giphygifs.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.andrews.giphygifs.domain.GifsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: GifsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val gifsPagingFlow =
        _state
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { state ->
                if (state.isSearchActive) {
                    repository.getGifs(state.searchQuery)
                } else {
                    repository.getTrendingGifs()
                }
            }
            .cachedIn(viewModelScope)

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query.trim()) }
    }

    fun onClearSearch() {
        _state.update {
            it.copy(
                searchQuery = "",
            )
        }
    }

    fun onDeleteGif(gifId: String) {
        viewModelScope.launch {
            try {
                repository.deleteGif(gifId)
            } catch (e: Exception) {
                handleError(e.message ?: "Error deleting gif")
            }
        }
    }

    private fun handleError(error: String) {
        viewModelScope.launch {
            _errorEvent.emit(error)
        }
    }
}