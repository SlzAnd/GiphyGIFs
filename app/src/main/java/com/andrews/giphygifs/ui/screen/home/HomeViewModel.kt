package com.andrews.giphygifs.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.andrews.giphygifs.domain.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MainRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
val gifsPagingFlow =
        _searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    repository.getTrendingGifs()
                }
                else repository.getGifs(query)
            }
            .cachedIn(viewModelScope)

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun onSearchSubmit() {
        val query = _state.value.searchQuery.trim()
        if (query.isNotEmpty()) {
            _searchQuery.value = query
            _state.update { it.copy(isSearchActive = true) }
        }
    }

    fun onClearSearch() {
        _searchQuery.value = ""
        _state.update {
            it.copy(
                searchQuery = "",
                isSearchActive = false
            )
        }
    }

    fun onDeleteGif(gifId: String) {
        viewModelScope.launch {
            try {
                repository.deleteGif(gifId)
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    fun onErrorShown() {
        _state.update { it.copy(error = null) }
    }
}