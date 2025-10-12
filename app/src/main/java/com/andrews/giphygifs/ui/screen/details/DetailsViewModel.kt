package com.andrews.giphygifs.ui.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.andrews.giphygifs.domain.MainRepository

class DetailsViewModel(
    repository: MainRepository,
) : ViewModel() {

    val gifsPagingFlow = repository.getCachedGifsPaged()
        .cachedIn(viewModelScope)
}
