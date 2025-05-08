package com.patrickhoette.core.ui.theme.definition

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.patrickhoette.core.ui.theme.model.ColorVariations
import com.patrickhoette.core.ui.theme.model.ThemeTypeColors
import com.patrickhoette.pokedex.entity.generic.Type
import com.patrickhoette.pokedex.entity.generic.Type.*

@Immutable
object LightThemeTypeColors : ThemeTypeColors {

    @Suppress("LongMethod")
    override operator fun get(type: Type) = when (type) {
        Normal -> ColorVariations(
            base = Color(0xFFA8A77A),
            onBase = Color.White,
            container = Color(0xFFE6E5D1),
            onContainer = Color(0xFF434326),
        )
        Fire -> ColorVariations(
            base = Color(0xFFEE8130),
            onBase = Color.White,
            container = Color(0xFFFFDBB3),
            onContainer = Color(0xFF5A1B00),
        )
        Water -> ColorVariations(
            base = Color(0xFF6390F0),
            onBase = Color.White,
            container = Color(0xFFC8DAFF),
            onContainer = Color(0xFF002C71),
        )
        Grass -> ColorVariations(
            base = Color(0xFF7AC74C),
            onBase = Color.White,
            container = Color(0xFFCFF4B2),
            onContainer = Color(0xFF184D00),
        )
        Electric -> ColorVariations(
            base = Color(0xFFF7D02C),
            onBase = Color(0xFF5A4300),
            container = Color(0xFFFFF5C2),
            onContainer = Color(0xFF332200),
        )
        Ice -> ColorVariations(
            base = Color(0xFF96D9D6),
            onBase = Color(0xFF003F3E),
            container = Color(0xFFD3F8F7),
            onContainer = Color(0xFF004444),
        )
        Fighting -> ColorVariations(
            base = Color(0xFFC22E28),
            onBase = Color.White,
            container = Color(0xFFF8B2AC),
            onContainer = Color(0xFF5A0000),
        )
        Poison -> ColorVariations(
            base = Color(0xFFA33AE1),
            onBase = Color.White,
            container = Color(0xFFE5C6E5),
            onContainer = Color(0xFF4D004D),
        )
        Ground -> ColorVariations(
            base = Color(0xFFE2BF65),
            onBase = Color(0xFF4A3600),
            container = Color(0xFFFBEEC5),
            onContainer = Color(0xFF2D2000),
        )
        Flying -> ColorVariations(
            base = Color(0xFFA98FF3),
            onBase = Color.White,
            container = Color(0xFFE3DAFF),
            onContainer = Color(0xFF3D268C),
        )
        Psychic -> ColorVariations(
            base = Color(0xFFF95587),
            onBase = Color.White,
            container = Color(0xFFFFB8CE),
            onContainer = Color(0xFF630028),
        )
        Bug -> ColorVariations(
            base = Color(0xFFA6B91A),
            onBase = Color(0xFF3A4800),
            container = Color(0xFFE7F4A2),
            onContainer = Color(0xFF223300),
        )
        Rock -> ColorVariations(
            base = Color(0xFFB6A136),
            onBase = Color(0xFF3D2E00),
            container = Color(0xFFF2E3B2),
            onContainer = Color(0xFF251C00),
        )
        Ghost -> ColorVariations(
            base = Color(0xFF735797),
            onBase = Color.White,
            container = Color(0xFFD6C9E7),
            onContainer = Color(0xFF3D2856),
        )
        Dragon -> ColorVariations(
            base = Color(0xFF6F35FC),
            onBase = Color.White,
            container = Color(0xFFC7B2FF),
            onContainer = Color(0xFF310084),
        )
        Dark -> ColorVariations(
            base = Color(0xFF707070),
            onBase = Color.White,
            container = Color(0xFFC3C3C3),
            onContainer = Color(0xFF2E2E2E),
        )
        Steel -> ColorVariations(
            base = Color(0xFFB7B7CE),
            onBase = Color(0xFF33334D),
            container = Color(0xFFE6E6F2),
            onContainer = Color(0xFF1E1E30),
        )
        Fairy -> ColorVariations(
            base = Color(0xFFD685AD),
            onBase = Color(0xFF4D002A),
            container = Color(0xFFF9DCE7),
            onContainer = Color(0xFF33001D),
        )
        Stellar -> ColorVariations(
            base = Color(0xFF4B4E89),
            onBase = Color.White,
            container = Color(0xFFD8D9F2),
            onContainer = Color(0xFF1A1B33),
        )
        Unknown -> ColorVariations(
            base = Color(0xFF808080),
            onBase = Color.White,
            container = Color(0xFFD3D3D3),
            onContainer = Color(0xFF303030),
        )
    }
}

@Immutable
object DarkThemeTypeColors : ThemeTypeColors {

    @Suppress("LongMethod")
    override operator fun get(type: Type) = when (type) {
        Normal -> ColorVariations(
            base = Color(0xFFD4D4A3),
            onBase = Color(0xFF434326),
            container = Color(0xFFA8A77A),
            onContainer = Color(0xFF1E1E10),
        )
        Fire -> ColorVariations(
            base = Color(0xFFFF9D56),
            onBase = Color(0xFF5A1B00),
            container = Color(0xFFEE8130),
            onContainer = Color(0xFF2E0F00),
        )
        Water -> ColorVariations(
            base = Color(0xFF8FB2FF),
            onBase = Color(0xFF002C71),
            container = Color(0xFF6390F0),
            onContainer = Color(0xFF001A44),
        )
        Grass -> ColorVariations(
            base = Color(0xFFA7E078),
            onBase = Color(0xFF184D00),
            container = Color(0xFF7AC74C),
            onContainer = Color(0xFF0D2900),
        )
        Electric -> ColorVariations(
            base = Color(0xFFFFF380),
            onBase = Color(0xFF332200),
            container = Color(0xFFF7D02C),
            onContainer = Color(0xFF1A1100),
        )
        Ice -> ColorVariations(
            base = Color(0xFFC6F2F1),
            onBase = Color(0xFF004444),
            container = Color(0xFF96D9D6),
            onContainer = Color(0xFF002A2A),
        )
        Fighting -> ColorVariations(
            base = Color(0xFFE86059),
            onBase = Color(0xFF5A0000),
            container = Color(0xFFC22E28),
            onContainer = Color(0xFF2E0000),
        )
        Poison -> ColorVariations(
            base = Color(0xFFC26BC0),
            onBase = Color(0xFF4D004D),
            container = Color(0xFFA33EA1),
            onContainer = Color(0xFF290029),
        )
        Ground -> ColorVariations(
            base = Color(0xFFF3D998),
            onBase = Color(0xFF2D2000),
            container = Color(0xFFE2BF65),
            onContainer = Color(0xFF1A1100),
        )
        Flying -> ColorVariations(
            base = Color(0xFFC9B6FF),
            onBase = Color(0xFF3D268C),
            container = Color(0xFFA98FF3),
            onContainer = Color(0xFF24135A),
        )
        Psychic -> ColorVariations(
            base = Color(0xFFFF86AF),
            onBase = Color(0xFF640028),
            container = Color(0xFFF95587),
            onContainer = Color(0xFF330014),
        )
        Bug -> ColorVariations(
            base = Color(0xFFC7D848),
            onBase = Color(0xFF223300),
            container = Color(0xFFA6B91A),
            onContainer = Color(0xFF101A00),
        )
        Rock -> ColorVariations(
            base = Color(0xFFD5C46A),
            onBase = Color(0xFF251C00),
            container = Color(0xFFB6A136),
            onContainer = Color(0xFF120E00),
        )
        Ghost -> ColorVariations(
            base = Color(0xFF9C79C2),
            onBase = Color(0xFF3D2856),
            container = Color(0xFF735797),
            onContainer = Color(0xFF1E142B),
        )
        Dragon -> ColorVariations(
            base = Color(0xFF9A6BFF),
            onBase = Color(0xFF31008A),
            container = Color(0xFF6F35FC),
            onContainer = Color(0xFF1A004D),
        )
        Dark -> ColorVariations(
            base = Color(0xFFA0A0A0),
            onBase = Color(0xFF2E2E2E),
            container = Color(0xFF707070),
            onContainer = Color(0xFF1A1A1A),
        )
        Steel -> ColorVariations(
            base = Color(0xFFD2D2E6),
            onBase = Color(0xFF1E1E30),
            container = Color(0xFFB7B7CE),
            onContainer = Color(0xFF101018),
        )
        Fairy -> ColorVariations(
            base = Color(0xFFF0AFCB),
            onBase = Color(0xFF33001D),
            container = Color(0xFFD685AD),
            onContainer = Color(0xFF1A000F),
        )
        Stellar -> ColorVariations(
            base = Color(0xFF6C71C4),
            onBase = Color.White,
            container = Color(0xFF383A5E),
            onContainer = Color(0xFFC3C5D0),
        )
        Unknown -> ColorVariations(
            base = Color(0xFFA0A0A0),
            onBase = Color(0xFF1E1E1E),
            container = Color(0xFF505050),
            onContainer = Color(0xFFFAFAFA),
        )
    }
}
