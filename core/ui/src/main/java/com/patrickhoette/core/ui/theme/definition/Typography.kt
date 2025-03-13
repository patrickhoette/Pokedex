package com.patrickhoette.core.ui.theme.definition

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.Hyphens.Companion.Auto
import androidx.compose.ui.text.style.Hyphens.Companion.None
import androidx.compose.ui.text.style.LineBreak.Companion.Heading
import androidx.compose.ui.text.style.LineBreak.Companion.Paragraph
import androidx.compose.ui.unit.sp
import com.patrickhoette.core.ui.theme.definition.Fonts.FiraCode
import com.patrickhoette.core.ui.theme.definition.Fonts.FireSans
import com.patrickhoette.core.ui.theme.definition.Fonts.Oxanium
import com.patrickhoette.core.ui.theme.model.SizedTextStyles
import com.patrickhoette.core.ui.theme.model.ThemeTypography

object Typography {

    val App = ThemeTypography(
        display = SizedTextStyles(
            large = TextStyle(
                fontSize = 64.sp,
                fontWeight = Bold,
                fontFamily = Oxanium,
                lineBreak = Heading,
                hyphens = Auto,
            ),
            medium = TextStyle(
                fontSize = 48.sp,
                fontWeight = Bold,
                fontFamily = Oxanium,
                lineBreak = Heading,
                hyphens = Auto,
            ),
            small = TextStyle(
                fontSize = 36.sp,
                fontWeight = SemiBold,
                fontFamily = Oxanium,
                lineBreak = Heading,
                hyphens = Auto,
            )
        ),
        headings = SizedTextStyles(
            large = TextStyle(
                fontSize = 30.sp,
                fontWeight = Medium,
                fontFamily = Oxanium,
                lineBreak = Heading,
                hyphens = Auto,
            ),
            medium = TextStyle(
                fontSize = 24.sp,
                fontWeight = Medium,
                fontFamily = Oxanium,
                lineBreak = Heading,
                hyphens = Auto,
            ),
            small = TextStyle(
                fontSize = 20.sp,
                fontWeight = Medium,
                fontFamily = Oxanium,
                lineBreak = Heading,
                hyphens = Auto,
            ),
        ),
        body = SizedTextStyles(
            large = TextStyle(
                fontSize = 18.sp,
                fontWeight = Normal,
                fontFamily = FireSans,
                lineBreak = Paragraph,
                hyphens = Auto,
            ),
            medium = TextStyle(
                fontSize = 16.sp,
                fontWeight = Normal,
                fontFamily = FireSans,
                lineBreak = Paragraph,
                hyphens = Auto,
            ),
            small = TextStyle(
                fontSize = 14.sp,
                fontWeight = Normal,
                fontFamily = FireSans,
                lineBreak = Paragraph,
                hyphens = Auto,
            ),
        ),
        label = SizedTextStyles(
            large = TextStyle(
                fontSize = 16.sp,
                fontWeight = SemiBold,
                fontFamily = FireSans,
                lineBreak = Paragraph,
                hyphens = Auto,
            ),
            medium = TextStyle(
                fontSize = 14.sp,
                fontWeight = Medium,
                fontFamily = FireSans,
                lineBreak = Paragraph,
                hyphens = Auto,
            ),
            small = TextStyle(
                fontSize = 12.sp,
                fontWeight = Medium,
                fontFamily = FireSans,
                lineBreak = Paragraph,
                hyphens = Auto,
            ),
        ),
        mono = SizedTextStyles(
            large = TextStyle(
                fontSize = 18.sp,
                fontWeight = Normal,
                fontFamily = FiraCode,
                lineBreak = Paragraph,
                hyphens = None,
            ),
            medium = TextStyle(
                fontSize = 16.sp,
                fontWeight = Normal,
                fontFamily = FiraCode,
                lineBreak = Paragraph,
                hyphens = None,
            ),
            small = TextStyle(
                fontSize = 14.sp,
                fontWeight = Normal,
                fontFamily = FiraCode,
                lineBreak = Paragraph,
                hyphens = None,
            )
        ),
        caption = TextStyle(
            fontSize = 12.sp,
            fontWeight = Normal,
            fontFamily = FireSans,
            lineBreak = Paragraph,
            hyphens = Auto,
        ),
    )
}
