@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.patrickhoette.pokemon.ui.list

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.patrickhoette.core.presentation.model.GenericError
import com.patrickhoette.core.ui.animation.AnimationDefaults.tween
import com.patrickhoette.core.ui.atom.fab.Fab
import com.patrickhoette.core.ui.grid.AdaptiveGridWithMax
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.Theme.typography
import com.patrickhoette.core.ui.theme.definition.Spacing
import com.patrickhoette.pokemon.presentation.list.PokemonListViewModel
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEntryUIModel.*
import com.patrickhoette.pokemon.presentation.list.model.PokemonListUIModel
import com.patrickhoette.pokemon.ui.R
import com.patrickhoette.pokemon.ui.list.PokemonListScreenTokens.IdBaseEnd
import com.patrickhoette.pokemon.ui.list.PokemonListScreenTokens.IdBaseEntry
import com.patrickhoette.pokemon.ui.list.PokemonListScreenTokens.IdBaseError
import com.patrickhoette.pokemon.ui.list.PokemonListScreenTokens.IdBaseLoading
import com.patrickhoette.pokemon.ui.list.PokemonListScreenTokens.LoadingRowThreshold
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.ceil
import kotlin.time.Duration.Companion.milliseconds

private object PokemonListScreenTokens {

    const val IdBaseEnd = "PokemonListEnd"
    const val IdBaseLoading = "PokemonListLoading"
    const val IdBaseEntry = "PokemonListEntry"
    const val IdBaseError = "PokemonListError"

    const val LoadingRowThreshold = 2
}

@Composable
fun PokemonListScreen(
    modifier: Modifier = Modifier,
    viewmodel: PokemonListViewModel = koinViewModel(),
) {
    val list by viewmodel.list.collectAsStateWithLifecycle()
    PokemonListScreen(
        onEntry = viewmodel::onPokemon,
        onLoadMore = viewmodel::onGetMorePokemon,
        onRetry = viewmodel::onGetMorePokemon,
        list = list,
        modifier = modifier,
    )
}

@Composable
fun PokemonListScreen(
    onEntry: (Entry) -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    list: PokemonListUIModel,
    modifier: Modifier = Modifier,
) {
    val orientation = LocalConfiguration.current.orientation
    val paddingInsets = WindowInsets(
        left = Spacing.x2,
        top = Spacing.x2,
        right = Spacing.x2,
        bottom = Spacing.x2,
    )
    val insets = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        WindowInsets.systemBars
            .union(WindowInsets.displayCutout.only(WindowInsetsSides.End))
            .add(paddingInsets)
    } else {
        WindowInsets.statusBars
            .union(WindowInsets.displayCutout)
            .add(paddingInsets)
    }

    val state = rememberLazyGridState()
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo }
            .mapLatest { layoutInfo ->
                val visibleItems = layoutInfo.visibleItemsInfo
                if (visibleItems.isEmpty()) return@mapLatest false

                val lastVisibleItem = visibleItems.last()
                val lastRow = lastVisibleItem.row
                // Use second to last row as last row might not be full
                val secondToLastRow = (lastRow - 1).coerceAtLeast(0)
                val secondToLastRowItemCount = visibleItems
                    .dropLastWhile { it.row == lastRow }
                    .takeLastWhile { it.row == secondToLastRow }
                    .size

                if (secondToLastRowItemCount > 0) {
                    val rows = ceil(layoutInfo.totalItemsCount / secondToLastRowItemCount.toFloat()).toInt()
                    (rows - lastRow) <= LoadingRowThreshold
                } else {
                    false
                }
            }
            .distinctUntilChanged()
            .collectLatest { if (it) onLoadMore() }
    }

    val showFab by remember(state) {
        derivedStateOf { state.canScrollBackward }
    }
    Box(modifier) {
        val shimmer = rememberShimmer(ShimmerBounds.View)
        LazyVerticalGrid(
            columns = AdaptiveGridWithMax(minSize = 150.dp, maxColumns = 6),
            modifier = Modifier
                .matchParentSize()
                .background(colors.background.base),
            state = state,
            contentPadding = insets.asPaddingValues(),
            verticalArrangement = Arrangement.spacedBy(Spacing.x2),
            horizontalArrangement = Arrangement.spacedBy(Spacing.x2),
        ) {
            item(
                span = { GridItemSpan(maxLineSpan) },
            ) {
                Text(
                    text = stringResource(R.string.pokemon_list_title),
                    modifier = Modifier.semantics { heading() },
                    style = typography.headings.large,
                )
            }

            items(
                count = list.entries.size,
                key = {
                    val base = when (list.entries[it]) {
                        End -> IdBaseEnd
                        is Entry -> IdBaseEntry
                        is Error -> IdBaseError
                        Loading -> IdBaseLoading
                    }
                    "$base#$it"
                },
                span = {
                    val item = list.entries[it]
                    when (item) {
                        is Entry, Loading -> GridItemSpan(1)
                        End, is Error -> GridItemSpan(maxLineSpan)
                    }
                },
                contentType = { list.entries[it]::class.qualifiedName }
            ) {
                val animationModifier = Modifier.animateItem(
                    fadeInSpec = tween(),
                    placementSpec = tween(600.milliseconds),
                    fadeOutSpec = tween(),
                )
                when (val item = list.entries[it]) {
                    End -> PokemonListEnd(animationModifier)
                    is Entry -> PokemonListEntry(
                        onClick = { onEntry(item) },
                        model = item,
                        modifier = animationModifier,
                    )
                    is Error -> PokemonListError(
                        cause = item.cause,
                        onRetry = onRetry,
                        modifier = animationModifier,
                    )
                    Loading -> PokemonListEntryLoading(
                        shimmer = shimmer,
                        modifier = animationModifier,
                    )
                }
            }
        }

        val scope = rememberCoroutineScope()
        AnimatedVisibility(
            visible = showFab,
            modifier = Modifier.align(Alignment.BottomEnd),
            enter = fadeIn(tween()),
            exit = fadeOut(tween()),
            label = "FAB visibility",
        ) {
            Fab(
                onClick = {
                    scope.launch { state.animateScrollToItem(0) }
                },
                icon = Icons.Filled.KeyboardArrowUp,
                contentDescription = stringResource(R.string.accessibility_label_scroll_up),
            )
        }
    }
}

@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL, showSystemUi = true)
@Preview(name = "Light", showSystemUi = true)
@Composable
private fun PreviewPokemonListScreen() = AppTheme {
    val list = remember {
        PokemonListUIModel(
            hasNext = true,
            entries = buildList {
                addAll(PokemonListEntryFactory.createList(30))
                repeat(10) { add(Loading) }
                add(Error(GenericError.Network))
                add(Error(GenericError.Server))
                add(Error(GenericError.Unknown))
                add(End)
            }.toImmutableList()
        )
    }

    PokemonListScreen(
        onEntry = {},
        onLoadMore = {},
        onRetry = {},
        list = list,
        modifier = Modifier.fillMaxSize(),
    )
}
