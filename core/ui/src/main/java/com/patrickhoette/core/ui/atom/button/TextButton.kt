package com.patrickhoette.core.ui.atom.button

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
    val foregroundEnabled = colors.primary.base
    val foregroundDisabled = colors.primary.base
    val ripple = colors.primary.container
    return remember(
        foregroundEnabled,
        foregroundDisabled,
        ripple,
    ) {
        ButtonTheme(
            background = ButtonColors(Color.Transparent),
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
fun TextButton(
    text: AnnotatedString,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = DefaultEnabled,
    loading: Boolean = DefaultLoading,
    description: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
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
fun TextButton(
    text: String,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = DefaultEnabled,
    loading: Boolean = DefaultLoading,
    description: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
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
fun TextButton(
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = DefaultEnabled,
    loading: Boolean = DefaultLoading,
    description: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
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
fun TextButton(
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
private fun PreviewTextButton() = AppTheme {
    Column(
        modifier = Modifier
            .background(colors.background.base)
            .padding(Spacing.x2)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(Spacing.x2),
    ) {
        TextButton(
            text = "Enabled",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )

        TextButton(
            text = "No onClick",
            onClick = null,
            modifier = Modifier.fillMaxWidth(),
        )

        TextButton(
            text = "Disabled",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
        )

        TextButton(
            text = "Loading",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            loading = true,
        )

        TextButton(
            text = "Loading - Disabled",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            loading = true,
        )
    }
}
