package com.patrickhoette.core.ui.atom.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.patrickhoette.core.ui.R
import com.patrickhoette.core.ui.animation.AnimationDefaults
import com.patrickhoette.core.ui.animation.AnimationDefaults.tween
import com.patrickhoette.core.ui.atom.button.ButtonTokens.AnimationDuration
import com.patrickhoette.core.ui.atom.button.ButtonTokens.DefaultHeight
import com.patrickhoette.core.ui.atom.button.ButtonTokens.DefaultWidth
import com.patrickhoette.core.ui.atom.button.ButtonTokens.LoadingIndicatorSize
import com.patrickhoette.core.ui.atom.button.ButtonTokens.Shape
import com.patrickhoette.core.ui.atom.button.model.ButtonColors
import com.patrickhoette.core.ui.atom.button.model.ButtonTheme
import com.patrickhoette.core.ui.theme.definition.Spacing

internal object ButtonTokens {

    val DefaultHeight = 48.dp
    val DefaultWidth = ButtonDefaults.MinWidth

    const val DefaultEnabled = true
    const val DefaultLoading = false

    val LoadingIndicatorSize = 24.dp

    val AnimationDuration = AnimationDefaults.Short

    val Shape = CircleShape
}

@Composable
internal fun BaseButton(
    text: AnnotatedString,
    onClick: (() -> Unit)?,
    enabled: Boolean,
    loading: Boolean,
    theme: ButtonTheme,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier,
    description: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
) = BaseButton(
    onClick = onClick,
    enabled = enabled,
    loading = loading,
    theme = theme,
    interactionSource = interactionSource,
    modifier = modifier,
    description = description,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon,
) {
    val isFocussed by interactionSource.collectIsFocusedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    ButtonText(
        text = text,
        enabled = enabled,
        loading = loading,
        pressed = isPressed,
        focussed = isFocussed,
        hovered = isHovered,
        theme = theme,
    )
}

@Composable
internal fun BaseButton(
    text: String,
    onClick: (() -> Unit)?,
    enabled: Boolean,
    loading: Boolean,
    theme: ButtonTheme,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier,
    description: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
) = BaseButton(
    onClick = onClick,
    enabled = enabled,
    loading = loading,
    theme = theme,
    interactionSource = interactionSource,
    modifier = modifier,
    description = description,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon,
) {
    val isFocussed by interactionSource.collectIsFocusedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    ButtonText(
        text = text,
        enabled = enabled,
        loading = loading,
        pressed = isPressed,
        focussed = isFocussed,
        hovered = isHovered,
        theme = theme,
    )
}

@Composable
internal fun BaseButton(
    onClick: (() -> Unit)?,
    enabled: Boolean,
    loading: Boolean,
    theme: ButtonTheme,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier,
    description: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val isFocussed by interactionSource.collectIsFocusedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val contentColor by animateButtonColorAsState(
        colors = theme.foreground,
        enabled = enabled,
        loading = loading,
        focussed = isFocussed,
        hovered = isHovered,
        pressed = isPressed,
        label = "Icon Color",
    )

    BaseButton(
        onClick = onClick,
        enabled = enabled,
        loading = loading,
        theme = theme,
        interactionSource = interactionSource,
        modifier = modifier,
        description = description,
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = contentColor,
                )
            }
        },
        trailingIcon = {
            trailingIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = contentColor,
                )
            }
        },
        content = content,
    )
}

@Composable
internal fun BaseButton(
    onClick: (() -> Unit)?,
    enabled: Boolean,
    loading: Boolean,
    theme: ButtonTheme,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier,
    description: String? = null,
    leadingIcon: (@Composable RowScope.() -> Unit),
    trailingIcon: (@Composable RowScope.() -> Unit),
    content: @Composable RowScope.() -> Unit,
) {
    val sizeModifier = Modifier.defaultMinSize(minWidth = DefaultWidth, minHeight = DefaultHeight)

    val isFocussed by interactionSource.collectIsFocusedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val borderModifier = Modifier.buttonBorder(
        colors = theme.border,
        enabled = enabled,
        loading = loading,
        focussed = isFocussed,
        hovered = isHovered,
        pressed = isPressed,
    )

    val backgroundModifier = Modifier.buttonBackground(
        colors = theme.background,
        enabled = enabled,
        loading = loading,
        focussed = isFocussed,
        hovered = isHovered,
        pressed = isPressed,
    )

    val clickModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled && !loading,
            role = Role.Button,
            onClick = onClick,
            onClickLabel = description,
        )
    } else {
        Modifier
    }

    val focusModifier = if (onClick != null) {
        Modifier.focusable(
            enabled = enabled,
            interactionSource = interactionSource,
        )
    } else {
        Modifier
    }

    val hoverableModifier = if (onClick != null) {
        Modifier.hoverable(
            enabled = enabled,
            interactionSource = interactionSource,
        )
    } else {
        Modifier
    }

    val scaleModifier = Modifier.buttonScale(
        hovered = isHovered,
        focussed = isFocussed,
    )

    val paddingModifier = Modifier.padding(vertical = Spacing.x1, horizontal = Spacing.x2)

    val semanticsModifier = Modifier.buttonSemantics(
        loading = loading,
        enabled = enabled,
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (loading) 0F else 1F,
        animationSpec = tween(AnimationDuration),
        label = "Content Alpha",
    )

    CompositionLocalProvider(LocalRippleConfiguration provides theme.ripple) {
        Box(
            modifier = modifier
                .then(scaleModifier)
                .clip(Shape)
                .then(sizeModifier)
                .then(borderModifier)
                .then(clickModifier)
                .focusGroup()
                .then(focusModifier)
                .then(hoverableModifier)
                .then(semanticsModifier),
            contentAlignment = Alignment.Center,
        ) {
            Spacer(
                modifier = Modifier
                    .then(backgroundModifier)
                    .indication(interactionSource, ripple())
                    .matchParentSize(),
            )

            val spinnerColor by animateButtonColorAsState(
                colors = theme.foreground,
                enabled = enabled,
                loading = loading,
                focussed = isFocussed,
                hovered = isHovered,
                pressed = isPressed,
                label = "Spinner Color",
            )
            CircularProgressIndicator(
                modifier = Modifier
                    .size(LoadingIndicatorSize)
                    .graphicsLayer { alpha = 1F - contentAlpha },
                color = spinnerColor,
                strokeWidth = 4.dp,
            )

            Row(
                modifier = Modifier
                    .then(paddingModifier)
                    .graphicsLayer { alpha = contentAlpha },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.x0_5)
            ) {
                leadingIcon()

                content()

                trailingIcon()
            }
        }
    }
}

private fun Modifier.buttonBorder(
    colors: ButtonColors?,
    enabled: Boolean,
    loading: Boolean,
    focussed: Boolean,
    hovered: Boolean,
    pressed: Boolean,
) = composed {
    if (colors != null) {
        val currentColor by animateButtonColorAsState(
            colors = colors,
            enabled = enabled,
            loading = loading,
            focussed = focussed,
            hovered = hovered,
            pressed = pressed,
            label = "Border Color",
        )
        then(
            Modifier.drawWithCache {
                onDrawWithContent {
                    val outline = Shape.createOutline(
                        size = size,
                        layoutDirection = layoutDirection,
                        density = this,
                    ) as Outline.Rounded

                    drawContent()
                    drawRoundRect(
                        color = currentColor,
                        style = Stroke(width = 1.dp.toPx()),
                        cornerRadius = outline.roundRect.topLeftCornerRadius
                    )
                }
            }
        )
    } else {
        this
    }
}

private fun Modifier.buttonBackground(
    colors: ButtonColors?,
    enabled: Boolean,
    loading: Boolean,
    focussed: Boolean,
    hovered: Boolean,
    pressed: Boolean,
) = composed {
    if (colors != null) {
        val currentColor by animateButtonColorAsState(
            colors = colors,
            enabled = enabled,
            loading = loading,
            focussed = focussed,
            hovered = hovered,
            pressed = pressed,
            label = "Background Color",
        )
        then(Modifier.drawBehind { drawRect(color = currentColor) })
    } else {
        this
    }
}

private fun Modifier.buttonScale(
    focussed: Boolean,
    hovered: Boolean,
) = composed {
    val targetScale = when {
        focussed -> 1.05F
        hovered -> 1.02F
        else -> 1F
    }
    val currentScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(AnimationDuration),
        label = "Scale",
    )
    then(
        Modifier.graphicsLayer {
            scaleX = currentScale
            scaleY = currentScale
        },
    )
}

private fun Modifier.buttonSemantics(
    loading: Boolean,
    enabled: Boolean,
) = composed {
    val loadingDescription = stringResource(R.string.accessibility_label_button_state_loading)
    then(
        Modifier.semantics {
            if (loading) stateDescription = loadingDescription

            if (!enabled) disabled()
        }
    )
}

@Composable
internal fun animateButtonColorAsState(
    colors: ButtonColors,
    enabled: Boolean,
    loading: Boolean,
    focussed: Boolean,
    hovered: Boolean,
    pressed: Boolean,
    label: String,
): State<Color> {
    val targetColor = when {
        focussed -> colors.focussed
        hovered -> colors.hovered
        pressed -> colors.pressed
        loading -> colors.loading
        enabled -> colors.enabled
        else -> colors.disabled
    }
    return animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(AnimationDuration),
        label = label,
    )
}
