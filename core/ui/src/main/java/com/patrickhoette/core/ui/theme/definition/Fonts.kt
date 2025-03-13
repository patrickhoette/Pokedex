package com.patrickhoette.core.ui.theme.definition

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.patrickhoette.core.ui.R

object Fonts {

    val FireSans = FontFamily(
        Font(resId = R.font.fira_sans_thin, weight = FontWeight.Thin),
        Font(resId = R.font.fira_sans_thin_italic, weight = FontWeight.Thin, style = FontStyle.Italic),
        Font(resId = R.font.fira_sans_extra_light, weight = FontWeight.ExtraLight),
        Font(resId = R.font.fira_sans_extra_light_italic, weight = FontWeight.ExtraLight, style = FontStyle.Italic),
        Font(resId = R.font.fira_sans_light, weight = FontWeight.Light),
        Font(resId = R.font.fira_sans_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),
        Font(resId = R.font.fira_sans_regular, weight = FontWeight.Normal),
        Font(resId = R.font.fira_sans_medium, weight = FontWeight.Medium),
        Font(resId = R.font.fira_sans_medium_italic, weight = FontWeight.Medium, style = FontStyle.Italic),
        Font(resId = R.font.fira_sans_semi_bold, weight = FontWeight.SemiBold),
        Font(resId = R.font.fira_sans_semi_bold_italic, weight = FontWeight.SemiBold, style = FontStyle.Italic),
        Font(resId = R.font.fira_sans_bold, weight = FontWeight.Bold),
        Font(resId = R.font.fira_sans_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
        Font(resId = R.font.fira_sans_extra_bold, weight = FontWeight.ExtraBold),
        Font(resId = R.font.fira_sans_extra_bold_italic, weight = FontWeight.ExtraBold, style = FontStyle.Italic),
        Font(resId = R.font.fira_sans_black, weight = FontWeight.Black),
        Font(resId = R.font.fira_sans_black_italic, weight = FontWeight.Black, style = FontStyle.Italic),
    )

    val FiraCode = FontFamily(
        Font(resId = R.font.fira_code_light, weight = FontWeight.Light),
        Font(resId = R.font.fira_code_regular, weight = FontWeight.Normal),
        Font(resId = R.font.fira_code_medium, weight = FontWeight.Medium),
        Font(resId = R.font.fira_code_semi_bold, weight = FontWeight.SemiBold),
        Font(resId = R.font.fira_code_bold, weight = FontWeight.Bold),
    )

    val Oxanium = FontFamily(
        Font(resId = R.font.oxanium_extra_light, weight = FontWeight.ExtraLight),
        Font(resId = R.font.oxanium_light, weight = FontWeight.Light),
        Font(resId = R.font.oxanium_regular, weight = FontWeight.Normal),
        Font(resId = R.font.oxanium_medium, weight = FontWeight.Medium),
        Font(resId = R.font.oxanium_semi_bold, weight = FontWeight.SemiBold),
        Font(resId = R.font.oxanium_bold, weight = FontWeight.Bold),
        Font(resId = R.font.oxanium_extra_bold, weight = FontWeight.ExtraBold),
    )
}
