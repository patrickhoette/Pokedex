package com.patrickhoette.core.ui.atom.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.patrickhoette.core.ui.atom.button.ButtonTokens.DefaultEnabled
import com.patrickhoette.core.ui.atom.button.ButtonTokens.DefaultLoading
import com.patrickhoette.core.ui.atom.button.model.ButtonColors
import com.patrickhoette.core.ui.atom.button.model.ButtonTheme
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.definition.Spacing

@Composable
private fun rememberTheme(): ButtonTheme {
    val backgroundEnabled = colors.primary.base
    val backgroundDisabled = colors.primary.base.copy(alpha = 0.5F).compositeOver(colors.background.base)
    val backgroundFocussed = colors.primary.base.copy(alpha = 0.75F).compositeOver(colors.background.onBase)
    val foregroundEnabled = colors.primary.onBase
    val foregroundDisabled = colors.primary.onBase
    val ripple = colors.primary.container
    return remember(
        backgroundEnabled,
        backgroundDisabled,
        backgroundFocussed,
        foregroundEnabled,
        foregroundDisabled,
        ripple,
    ) {
        ButtonTheme(
            background = ButtonColors(
                enabled = backgroundEnabled,
                disabled = backgroundDisabled,
                focussed = backgroundFocussed,
            ),
            foreground = ButtonColors(
                enabled = foregroundEnabled,
                disabled = foregroundDisabled,
            ),
            border = null,
            ripple = RippleConfiguration(
                color = ripple,
                rippleAlpha = RippleAlpha(draggedAlpha = 0F, focusedAlpha = 0F, hoveredAlpha = 0F, pressedAlpha = 1F),
            ),
        )
    }
}

@Composable
fun PrimaryButton(
    text: AnnotatedString,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = DefaultEnabled,
    loading: Boolean = DefaultLoading,
    description: String? = null,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
) = BaseButton(
    text = text,
    onClick = onClick,
    enabled = enabled,
    loading = loading,
    theme = rememberTheme(),
    interactionSource = interactionSource,
    modifier = modifier,
    description = description,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon,
)

@Composable
fun PrimaryButton(
    text: String,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = DefaultEnabled,
    loading: Boolean = DefaultLoading,
    description: String? = null,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
) = BaseButton(
    text = text,
    onClick = onClick,
    enabled = enabled,
    loading = loading,
    theme = rememberTheme(),
    interactionSource = interactionSource,
    modifier = modifier,
    description = description,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon,
)

@Composable
fun PrimaryButton(
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = DefaultEnabled,
    loading: Boolean = DefaultLoading,
    description: String? = null,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
    content: @Composable RowScope.() -> Unit,
) = BaseButton(
    onClick = onClick,
    enabled = enabled,
    loading = loading,
    theme = rememberTheme(),
    interactionSource = interactionSource,
    modifier = modifier,
    description = description,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon,
    content = content,
)

@Composable
fun PrimaryButton(
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = DefaultEnabled,
    loading: Boolean = DefaultLoading,
    description: String? = null,
    leadingIcon: (@Composable RowScope.() -> Unit),
    trailingIcon: (@Composable RowScope.() -> Unit),
    content: @Composable RowScope.() -> Unit,
) = BaseButton(
    onClick = onClick,
    enabled = enabled,
    loading = loading,
    theme = rememberTheme(),
    interactionSource = interactionSource,
    modifier = modifier,
    description = description,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon,
    content = content,
)

@PreviewLightDark
@Composable
private fun PreviewPrimaryButton() = AppTheme {
    Column(
        modifier = Modifier
            .background(colors.background.base)
            .padding(Spacing.x2)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(Spacing.x2),
    ) {
        PrimaryButton(
            text = "Enabled",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )

        PrimaryButton(
            text = "No onClick",
            onClick = null,
            modifier = Modifier.fillMaxWidth(),
        )

        PrimaryButton(
            text = "Disabled",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
        )

        PrimaryButton(
            text = "Loading",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            loading = true,
        )

        PrimaryButton(
            text = "Loading - Disabled",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            loading = true,
        )
    }
}
