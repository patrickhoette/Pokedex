package com.patrickhoette.core.ui.atom.image

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter.Companion.DefaultTransform
import coil3.compose.AsyncImagePainter.State
import coil3.request.ImageRequest
import com.patrickhoette.core.ui.R
import com.patrickhoette.core.ui.animation.AnimationDefaults.tween
import com.patrickhoette.core.ui.atom.image.StateAwareAsyncImageState.*
import com.patrickhoette.core.ui.atom.image.StateAwareAsyncImageTokens.ErrorIconMaxSize
import com.patrickhoette.core.ui.atom.image.StateAwareAsyncImageTokens.ErrorIconMinSize
import com.patrickhoette.core.ui.extension.shimmerItem
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.valentinilk.shimmer.shimmer
import io.github.aakira.napier.Napier

private object StateAwareAsyncImageTokens {

    val ErrorIconMinSize = 24.dp
    val ErrorIconMaxSize = 72.dp
}

@Composable
fun StateAwareAsyncImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    transform: (State) -> State = DefaultTransform,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    clipToBounds: Boolean = true,
) {
    var state by remember { mutableStateOf(PreStart) }

    val asyncImageAlpha by animateFloatAsState(
        targetValue = if (state == Success) 1F else 0F,
        animationSpec = tween(),
        label = "Async Image Alpha",
    )

    Box(modifier) {
        Crossfade(
            targetState = state,
            modifier = Modifier.matchParentSize(),
            animationSpec = tween(),
            label = "State Crossfade"
        ) {
            when (it) {
                PreStart, Success -> Spacer(Modifier.fillMaxSize())
                Loading -> LoadingState(Modifier.fillMaxSize())
                Error -> ErrorState(Modifier.fillMaxSize())
            }
        }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .listener(
                    onStart = { state = Loading },
                    onError = { _, result ->
                        Napier.w("Failed to load image", result.throwable)
                        state = Error
                    },
                    onSuccess = { _, _ -> state = Success },
                    onCancel = {
                        Napier.w("Image load cancelled")
                        state = Error
                    },
                )
                .build(),
            contentDescription = contentDescription,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { this.alpha = asyncImageAlpha },
            transform = transform,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
            clipToBounds = clipToBounds,
        )
    }
}

@Composable
private fun LoadingState(
    modifier: Modifier = Modifier,
) = Spacer(
    modifier = modifier
        .shimmer()
        .shimmerItem(RectangleShape),
)

@Composable
private fun ErrorState(
    modifier: Modifier = Modifier,
) = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
) {
    Icon(
        imageVector = Icons.Filled.Warning,
        contentDescription = stringResource(R.string.accessibility_label_image_error),
        modifier = Modifier
            .sizeIn(
                minWidth = ErrorIconMinSize,
                minHeight = ErrorIconMinSize,
                maxWidth = ErrorIconMaxSize,
                maxHeight = ErrorIconMaxSize,
            )
            .fillMaxSize(),
        tint = colors.warning.base,
    )
}

private enum class StateAwareAsyncImageState {

    PreStart,
    Loading,
    Error,
    Success,
}

@PreviewLightDark
@Composable
private fun PreviewStateAwareAsyncImageLoading() = AppTheme {
    LoadingState(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    )
}

@PreviewLightDark
@Composable
private fun PreviewStateAwareAsyncImageError() = AppTheme {
    ErrorState(
        modifier = Modifier
            .size(100.dp, 100.dp)
            .background(colors.background.base),
    )
}
