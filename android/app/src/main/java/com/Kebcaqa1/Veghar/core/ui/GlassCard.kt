package com.Kebcaqa1.Veghar.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Kebcaqa1.Veghar.core.theme.AccentCyan
import com.Kebcaqa1.Veghar.core.theme.GlassBorder
import com.Kebcaqa1.Veghar.core.theme.SurfaceRaised

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    accent: Color = AccentCyan,
    fill: Color = SurfaceRaised.copy(alpha = 0.66f),
    border: Color = GlassBorder,
    radius: Dp = 20.dp,
    brackets: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(radius)
    Box(
        modifier = modifier
            .background(fill, shape)
            .border(1.dp, border, shape)
            .then(if (brackets) Modifier.drawBehind { drawBrackets(accent) } else Modifier)
    ) {
        content()
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBrackets(accent: Color) {
    val arm = 18.dp.toPx()
    val inset = 10.dp.toPx()
    val stroke = 2.dp.toPx()
    val color = accent.copy(alpha = 0.75f)
    val w = size.width
    val h = size.height
    drawLine(color, Offset(inset, inset), Offset(inset + arm, inset), stroke, StrokeCap.Round)
    drawLine(color, Offset(inset, inset), Offset(inset, inset + arm), stroke, StrokeCap.Round)
    drawLine(color, Offset(w - inset, inset), Offset(w - inset - arm, inset), stroke, StrokeCap.Round)
    drawLine(color, Offset(w - inset, inset), Offset(w - inset, inset + arm), stroke, StrokeCap.Round)
    drawLine(color, Offset(inset, h - inset), Offset(inset + arm, h - inset), stroke, StrokeCap.Round)
    drawLine(color, Offset(inset, h - inset), Offset(inset, h - inset - arm), stroke, StrokeCap.Round)
    drawLine(color, Offset(w - inset, h - inset), Offset(w - inset - arm, h - inset), stroke, StrokeCap.Round)
    drawLine(color, Offset(w - inset, h - inset), Offset(w - inset, h - inset - arm), stroke, StrokeCap.Round)
}
