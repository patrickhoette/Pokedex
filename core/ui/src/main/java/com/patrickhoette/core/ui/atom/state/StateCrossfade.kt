package com.patrickhoette.core.ui.atom.state

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.patrickhoette.core.presentation.model.TypedUIState
import java.util.UUID

/**
 * [StateCrossfade] allows to switch between two layouts with a crossfade animation.
 * This has been changed to be used with the [TypedUIState] as crossfading between content is unwanted.
 * Completely copied from the Compose Crossfade except a few lines in the next method, annotated with comments. Only
 * changes in the type of [TypedUIState] will be animated so no animation for the following:
 *
 * - [TypedUIState.Error] -> [TypedUIState.Error]
 * - [TypedUIState.Loading] -> [TypedUIState.Loading]
 * - [TypedUIState.Normal] -> [TypedUIState.Normal]
 *
 * @sample androidx.compose.animation.samples.CrossfadeSample
 *
 * @param targetState is a key representing your target layout state. Every time you change a key
 * the animation will be triggered. The [content] called with the old key will be faded out while
 * the [content] called with the new key will be faded in.
 * @param modifier Modifier to be applied to the animation container.
 * @param animationSpec the [AnimationSpec] to configure the animation.
 * @param label An optional label to differentiate from other animations in Android Studio.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <D, E, T : TypedUIState<D, E>> StateCrossfade(
    targetState: T,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    label: String = "Crossfade",
    content: @Composable (T) -> Unit,
) {
    val transition = updateTransition(targetState, label)
    transition.StateCrossfade(modifier, animationSpec, content = content)
}

/**
 * [StateCrossfade] allows to switch between two layouts with a crossfade animation. The target state of
 * this Crossfade will be the target state of the given Transition object. In other words, when
 * the Transition changes target, the [StateCrossfade] will fade in the target content while fading out
 * the current content.
 *
 * [content] is a mapping between the state and the composable function for the content of
 * that state. During the crossfade, [content] lambda will be invoked multiple times with different
 * state parameter such that content associated with different states will be fading in/out at the
 * same time.
 *
 * @param modifier Modifier to be applied to the animation container.
 * @param animationSpec the [AnimationSpec] to configure the animation.
 */
@ExperimentalAnimationApi
@Composable
private fun <D, E, T : TypedUIState<D, E>> Transition<T>.StateCrossfade(
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    content: @Composable (targetState: T) -> Unit,
) {
    val contentKey: (T) -> String = {
        it::class.qualifiedName ?: UUID.randomUUID().toString()
    }

    val currentlyVisible = remember { mutableStateListOf<T>().apply { add(currentState) } }
    val contentMap = remember {
        mutableMapOf<TypedUIState<*, *>, @Composable () -> Unit>()
    }

    if (currentState == targetState) {
        // If not animating, just display the current state
        if (currentlyVisible.size != 1 || currentlyVisible[0] != targetState) {
            // Remove all the intermediate items from the list once the animation is finished.
            currentlyVisible.removeAll { it != targetState }
            contentMap.clear()
        }
    }
    if (!contentMap.contains(targetState)) {
        // Replace target with the same key if any
        val replacementId = currentlyVisible.indexOfFirst {
            contentKey(it) == contentKey(targetState)
        }
        if (replacementId == -1) {
            currentlyVisible.add(targetState)
        } else {
            currentlyVisible[replacementId] = targetState
        }
        contentMap.clear()
        currentlyVisible.forEach { stateForContent ->
            contentMap[stateForContent] = {
                val alpha by animateFloat(
                    transitionSpec = { animationSpec }
                ) { if (it == stateForContent) 1f else 0f }
                Box(Modifier.graphicsLayer { this.alpha = alpha }) {
                    content(stateForContent)
                }
            }
        }
    }

    Box(modifier) {
        currentlyVisible.forEach {
            key(contentKey(it)) {
                contentMap[it]?.invoke()
            }
        }
    }
}
