package com.patrickhoette.core.ui.atom.button

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import com.patrickhoette.core.ui.atom.button.model.ButtonTheme
import com.patrickhoette.core.ui.theme.Theme.typography

@Composable
internal fun ButtonText(
    text: String,
    enabled: Boolean,
    loading: Boolean,
    focussed: Boolean,
    hovered: Boolean,
    pressed: Boolean,
    theme: ButtonTheme,
    modifier: Modifier = Modifier,
) = ButtonText(
    text = AnnotatedString(text),
    enabled = enabled,
    loading = loading,
    focussed = focussed,
    hovered = hovered,
    pressed = pressed,
    theme = theme,
    modifier = modifier,
)

@Composable
internal fun ButtonText(
    text: AnnotatedString,
    enabled: Boolean,
    loading: Boolean,
    focussed: Boolean,
    hovered: Boolean,
    pressed: Boolean,
    theme: ButtonTheme,
    modifier: Modifier = Modifier,
) {
    val contentColor by animateButtonColorAsState(
        colors = theme.foreground,
        enabled = enabled,
        loading = loading,
        focussed = focussed,
        hovered = hovered,
        pressed = pressed,
        label = "Text Color"
    )
    Text(
        text = text,
        modifier = modifier,
        color = contentColor,
        overflow = Ellipsis,
        textAlign = TextAlign.Center,
        style = typography.label.medium,
    )
}
