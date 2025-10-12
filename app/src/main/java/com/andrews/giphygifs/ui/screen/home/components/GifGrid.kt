package com.andrews.giphygifs.ui.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.andrews.giphygifs.R
import com.andrews.giphygifs.domain.model.Gif

@Composable
fun GifGrid(
    gifsPagingItems: LazyPagingItems<Gif>,
    onGifClick: (index: Int) -> Unit,
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                count = gifsPagingItems.itemCount,
                key = gifsPagingItems.itemKey { it.id }
            ) { index ->
                gifsPagingItems[index]?.let { gif ->
                    GifItem(
                        gif = gif,
                        onClick = { onGifClick(index) },
                        onDeleteClick = { onDeleteClick(gif.id) }
                    )
                }
            }
        }
        when (gifsPagingItems.loadState.append) {
            is LoadState.Loading -> {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoadState.Error -> {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.error_loading_more_gifs),
                        modifier = Modifier.padding(16.dp),
                        color = Color.White
                    )
                }
            }

            else -> {}
        }

        if (gifsPagingItems.loadState.refresh is LoadState.Loading && gifsPagingItems.itemCount == 0) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (gifsPagingItems.loadState.refresh is LoadState.NotLoading && gifsPagingItems.itemCount == 0) {
            Text(
                text = stringResource(R.string.no_gifs),
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (gifsPagingItems.loadState.refresh is LoadState.Error && gifsPagingItems.itemCount == 0) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.error_loading_gifs),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}