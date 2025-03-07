package com.patrickhoette.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import com.patrickhoette.core.ui.theme.entity.VariantColor
import com.patrickhoette.pokedex.entity.Type
import com.patrickhoette.pokedex.entity.Type.*

val LocalPokeTypeColors = staticCompositionLocalOf<PokeTypeColors> { LightPokeTypeColors }

@Stable
interface PokeTypeColors {

    operator fun get(type: Type): VariantColor
}

@Immutable
object LightPokeTypeColors : PokeTypeColors {

    override operator fun get(type: Type) = when (type) {
        Normal -> VariantColor(
            light = Color(0xFFD4D4A3),
            onLight = Black,
            base = Color(0xFFA8A77A),
            onBase = Black,
            dark = Color(0xFF7C7B56),
            onDark = Black,
        )
        Fire -> VariantColor(
            light = Color(0xFFFFAA5C),
            onLight = Black,
            base = Color(0xFFEE8130),
            onBase = Black,
            dark = Color(0xFF773810),
            onDark = Black,
        )
        Water -> VariantColor(
            light = Color(0xFF8FB2FF),
            onLight = Black,
            base = Color(0xFF6390F0),
            onBase = Black,
            dark = Color(0xFF3E5EB8),
            onDark = White,
        )
        Grass -> VariantColor(
            light = Color(0xFFA7E078),
            onLight = Black,
            base = Color(0xFF7AC74C),
            onBase = Black,
            dark = Color(0xFF4F8E30),
            onDark = Black,
        )
        Electric -> VariantColor(
            light = Color(0xFFFFEB70),
            onLight = Black,
            base = Color(0xFFF7D02C),
            onBase = Black,
            dark = Color(0xFFC0A51E),
            onDark = Black,
        )
        Ice -> VariantColor(
            light = Color(0xFFC6F2F1),
            onLight = Black,
            base = Color(0xFF96D9D6),
            onBase = Black,
            dark = Color(0xFF5DAAA8),
            onDark = Black,
        )
        Fighting -> VariantColor(
            light = Color(0xFFE86059),
            onLight = Black,
            base = Color(0xFFC22E28),
            onBase = White,
            dark = Color(0xFF8A1F1B),
            onDark = White,
        )
        Poison -> VariantColor(
            light = Color(0xFFC26BC0),
            onLight = Black,
            base = Color(0xFFA33EA1),
            onBase = White,
            dark = Color(0xFF722978),
            onDark = White,
        )
        Ground -> VariantColor(
            light = Color(0xFFF3D998),
            onLight = Black,
            base = Color(0xFFE2BF65),
            onBase = Black,
            dark = Color(0xFFAF8A4A),
            onDark = Black,
        )
        Flying -> VariantColor(
            light = Color(0xFFC9B6FF),
            onLight = Black,
            base = Color(0xFFA98FF3),
            onBase = Black,
            dark = Color(0xFF7964BB),
            onDark = White,
        )
        Psychic -> VariantColor(
            light = Color(0xFFFF86AF),
            onLight = Black,
            base = Color(0xFFF95587),
            onBase = Black,
            dark = Color(0xFFC32E5C),
            onDark = White,
        )
        Bug -> VariantColor(
            light = Color(0xFFC7D848),
            onLight = Black,
            base = Color(0xFFA6B91A),
            onBase = Black,
            dark = Color(0xFF73810D),
            onDark = Black,
        )
        Rock -> VariantColor(
            light = Color(0xFFD5C46A),
            onLight = Black,
            base = Color(0xFFB6A136),
            onBase = Black,
            dark = Color(0xFF807224),
            onDark = White,
        )
        Ghost -> VariantColor(
            light = Color(0xFF9C79C2),
            onLight = Black,
            base = Color(0xFF735797),
            onBase = White,
            dark = Color(0xFF513B6D),
            onDark = White,
        )
        Dragon -> VariantColor(
            light = Color(0xFF9A6BFF),
            onLight = Black,
            base = Color(0xFF6F35FC),
            onBase = White,
            dark = Color(0xFF4A24B2),
            onDark = White,
        )
        Dark -> VariantColor(
            light = Color(0xFFA0A0A0),
            onLight = Black,
            base = Color(0xFF707070),
            onBase = White,
            dark = Color(0xFF505050),
            onDark = White,
        )
        Steel -> VariantColor(
            light = Color(0xFFD2D2E6),
            onLight = Black,
            base = Color(0xFFB7B7CE),
            onBase = Black,
            dark = Color(0xFF83839A),
            onDark = Black,
        )
        Fairy -> VariantColor(
            light = Color(0xFFF0AFCB),
            onLight = Black,
            base = Color(0xFFD685AD),
            onBase = Black,
            dark = Color(0xFFA55F80),
            onDark = Black,
        )
        Unknown -> VariantColor(
            light = Color(0xFFCCCCCC),
            onLight = Black,
            base = Color(0xFF888888),
            onBase = Black,
            dark = Color(0xFF555555),
            onDark = White,
        )
    }
}

@Immutable
object DarkPokeTypeColors : PokeTypeColors {

    override operator fun get(type: Type) = when (type) {
        Normal -> VariantColor(
            light = Color(0xFFD4D4A3),
            onLight = Black,
            base = Color(0xFFA8A77A),
            onBase = Black,
            dark = Color(0xFF5A593F),
            onDark = White
        )
        Fire -> VariantColor(
            light = Color(0xFFFFB873),
            onLight = Black,
            base = Color(0xFFEE8130),
            onBase = Black,
            dark = Color(0xFF773810),
            onDark = White,
        )
        Water -> VariantColor(
            light = Color(0xFFA3C3FF),
            onLight = Black,
            base = Color(0xFF6390F0),
            onBase = Black,
            dark = Color(0xFF243F77),
            onDark = White,
        )
        Grass -> VariantColor(
            light = Color(0xFFA7E078),
            onLight = Black,
            base = Color(0xFF7AC74C),
            onBase = Black,
            dark = Color(0xFF29531B),
            onDark = White,
        )
        Electric -> VariantColor(
            light = Color(0xFFFFF380),
            onLight = Black,
            base = Color(0xFFF7D02C),
            onBase = Black,
            dark = Color(0xFF756107),
            onDark = White,
        )
        Ice -> VariantColor(
            light = Color(0xFFD7FFFF),
            onLight = Black,
            base = Color(0xFF96D9D6),
            onBase = Black,
            dark = Color(0xFF2A6160),
            onDark = White,
        )
        Fighting -> VariantColor(
            light = Color(0xFFF0706A),
            onLight = Black,
            base = Color(0xFFC22E28),
            onBase = White,
            dark = Color(0xFF530F0D),
            onDark = White,
        )
        Poison -> VariantColor(
            light = Color(0xFFD88FDF),
            onLight = Black,
            base = Color(0xFFA33EA1),
            onBase = White,
            dark = Color(0xFF3E1241),
            onDark = White,
        )
        Ground -> VariantColor(
            light = Color(0xFFFBE3A0),
            onLight = Black,
            base = Color(0xFFE2BF65),
            onBase = Black,
            dark = Color(0xFF6D541F),
            onDark = White,
        )
        Flying -> VariantColor(
            light = Color(0xFFD8C7FF),
            onLight = Black,
            base = Color(0xFFA98FF3),
            onBase = Black,
            dark = Color(0xFF3E2F77),
            onDark = White,
        )
        Psychic -> VariantColor(
            light = Color(0xFFFFB2CF),
            onLight = Black,
            base = Color(0xFFF95587),
            onBase = Black,
            dark = Color(0xFF6A1A30),
            onDark = White,
        )
        Bug -> VariantColor(
            light = Color(0xFFD4F06D),
            onLight = Black,
            base = Color(0xFFA6B91A),
            onBase = Black,
            dark = Color(0xFF384003),
            onDark = White,
        )
        Rock -> VariantColor(
            light = Color(0xFFE0D58D),
            onLight = Black,
            base = Color(0xFFB6A136),
            onBase = Black,
            dark = Color(0xFF3E3A11),
            onDark = White,
        )
        Ghost -> VariantColor(
            light = Color(0xFFBEA4E4),
            onLight = Black,
            base = Color(0xFF735797),
            onBase = White,
            dark = Color(0xFF281B3C),
            onDark = White,
        )
        Dragon -> VariantColor(
            light = Color(0xFFC3A8FF),
            onLight = Black,
            base = Color(0xFF6F35FC),
            onBase = White,
            dark = Color(0xFF2C0973),
            onDark = White,
        )
        Dark -> VariantColor(
            light = Color(0xFFB0B0B0),
            onLight = Black,
            base = Color(0xFF707070),
            onBase = White,
            dark = Color(0xFF303030),
            onDark = White,
        )
        Steel -> VariantColor(
            light = Color(0xFFE2E2F4),
            onLight = Black,
            base = Color(0xFFB7B7CE),
            onBase = Black,
            dark = Color(0xFF40405A),
            onDark = White,
        )
        Fairy -> VariantColor(
            light = Color(0xFFF8D1E1),
            onLight = Black,
            base = Color(0xFFD685AD),
            onBase = Black,
            dark = Color(0xFF531F38),
            onDark = White,
        )
        Unknown -> VariantColor(
            light = Color(0xFFDDDDDD),
            onLight = Black,
            base = Color(0xFF888888),
            onBase = Black,
            dark = Color(0xFF333333),
            onDark = White,
        )
    }
}
