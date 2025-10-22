package com.andrews.giphygifs.ui.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.andrews.giphygifs.domain.GifsRepository

class DetailsViewModel(
    repository: GifsRepository,
) : ViewModel() {

    val gifsPagingFlow = repository.getCachedGifsPaged()
        .cachedIn(viewModelScope)
}
