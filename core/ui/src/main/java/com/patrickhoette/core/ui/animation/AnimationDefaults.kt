@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.patrickhoette.core.ui.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

object AnimationDefaults {

    val DefaultEasing = FastOutSlowInEasing
    val BounceEasing = EaseOutBack

    val Minuscule = 100.milliseconds
    val Tiny = 150.milliseconds
    val Short = 200.milliseconds
    val DefaultExit = 250.milliseconds
    val Default = 300.milliseconds

    val NavEnter = 600.milliseconds
    val NavExit = 500.milliseconds

    val AnimatedContentTransitionScope<*>.DefaultEnterAnimation
        get() = slideIntoContainer(
            towards = SlideDirection.Start,
            animationSpec = tween()
        )

    val AnimatedContentTransitionScope<*>.DefaultExitAnimation
        get() = fadeOut(tween())

    val AnimatedContentTransitionScope<*>.DefaultPopEnterAnimation
        get() = fadeIn(tween(DefaultExit))

    val AnimatedContentTransitionScope<*>.DefaultPopExitAnimation
        get() = slideOutOfContainer(
            towards = SlideDirection.End,
            animationSpec = tween(DefaultExit),
        )

    fun <T> tween(
        duration: Duration = Default,
        delay: Duration = 0.milliseconds,
        easing: Easing = DefaultEasing,
    ) = tween<T>(
        durationMillis = duration.inWholeMilliseconds.toInt(),
        delayMillis = delay.inWholeMilliseconds.toInt(),
        easing = easing,
    )

    fun <T> repeatableTween(
        duration: Duration = Default,
        easing: Easing = DefaultEasing,
        repeatMode: RepeatMode = RepeatMode.Restart,
        initialStartOffset: StartOffset = StartOffset(0),
    ) = infiniteRepeatable<T>(
        animation = tween(duration = duration, easing = easing),
        repeatMode = repeatMode,
        initialStartOffset = initialStartOffset,
    )
}
