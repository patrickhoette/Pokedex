package com.patrickhoette.core.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.DefaultFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.Pokeball: ImageVector
    get() {
        val current = _pokeball
        if (current != null) return current

        return ImageVector.Builder(
            name = "com.patrickhoette.core.ui.theme.AppTheme.Pokeball",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f,
        ).apply {
            // M12 14.7 a2.7 2.7 0 0 0 2.65 -2.2 h7.84 a10.5 10.5 0 0 1 -20.98 0 h7.84 A2.7 2.7 0 0 0 12 14.7Z m0 -5.4 a2.7 2.7 0 0 0 -2.65 2.2 H1.5 a10.5 10.5 0 0 1 20.98 0 h-7.84 A2.7 2.7 0 0 0 12 9.3Z
            path(
                fill = SolidColor(Color.Black),
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Bevel,
                strokeLineMiter = 1f,
                pathFillType = DefaultFillType,
            ) {
                // M 12 14.7
                moveTo(x = 12.0f, y = 14.7f)
                // a 2.7 2.7 0 0 0 2.65 -2.2
                arcToRelative(
                    a = 2.7f,
                    b = 2.7f,
                    theta = 0.0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    dx1 = 2.65f,
                    dy1 = -2.2f,
                )
                // h 7.84
                horizontalLineToRelative(dx = 7.84f)
                // a 10.5 10.5 0 0 1 -20.98 0
                arcToRelative(
                    a = 10.5f,
                    b = 10.5f,
                    theta = 0.0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    dx1 = -20.98f,
                    dy1 = 0.0f,
                )
                // h 7.84
                horizontalLineToRelative(dx = 7.84f)
                // A 2.7 2.7 0 0 0 12 14.7z
                arcTo(
                    horizontalEllipseRadius = 2.7f,
                    verticalEllipseRadius = 2.7f,
                    theta = 0.0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    x1 = 12.0f,
                    y1 = 14.7f,
                )
                close()
                // m 0 -5.4
                moveToRelative(dx = 0.0f, dy = -5.4f)
                // a 2.7 2.7 0 0 0 -2.65 2.2
                arcToRelative(
                    a = 2.7f,
                    b = 2.7f,
                    theta = 0.0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    dx1 = -2.65f,
                    dy1 = 2.2f,
                )
                // H 1.5
                horizontalLineTo(x = 1.5f)
                // a 10.5 10.5 0 0 1 20.98 0
                arcToRelative(
                    a = 10.5f,
                    b = 10.5f,
                    theta = 0.0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    dx1 = 20.98f,
                    dy1 = 0.0f,
                )
                // h -7.84
                horizontalLineToRelative(dx = -7.84f)
                // A 2.7 2.7 0 0 0 12 9.3z
                arcTo(
                    horizontalEllipseRadius = 2.7f,
                    verticalEllipseRadius = 2.7f,
                    theta = 0.0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    x1 = 12.0f,
                    y1 = 9.3f,
                )
                close()
            }
            // M12 10.5 A1.5 1.5 0 1 0 12 13.5 1.5 1.5 0 1 0 12 10.5z
            path(
                fill = SolidColor(Color.Black),
                strokeLineWidth = 1f,
            ) {
                // M 12 10.5
                moveTo(x = 12.0f, y = 10.5f)
                // A 1.5 1.5 0 1 0 12 13.5
                arcTo(
                    horizontalEllipseRadius = 1.5f,
                    verticalEllipseRadius = 1.5f,
                    theta = 0.0f,
                    isMoreThanHalf = true,
                    isPositiveArc = false,
                    x1 = 12.0f,
                    y1 = 13.5f,
                )
                // A 1.5 1.5 0 1 0 12 10.5z
                arcTo(
                    horizontalEllipseRadius = 1.5f,
                    verticalEllipseRadius = 1.5f,
                    theta = 0.0f,
                    isMoreThanHalf = true,
                    isPositiveArc = false,
                    x1 = 12.0f,
                    y1 = 10.5f,
                )
                close()
            }
        }.build().also { _pokeball = it }
    }

@Suppress("ObjectPropertyName")
private var _pokeball: ImageVector? = null
