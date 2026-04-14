package com.laniao.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

/**
 * Theme-aware semantic colors that adapt to dark/light mode.
 * Card backgrounds use subtle tinted variants in dark mode.
 * Accent colors lighten in dark mode for readability.
 */
object LaNiaoColors {

    // Card backgrounds
    val SummaryCardBackground: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF1A2A3A) else Color(0xFFE3F2FD)

    val DrinksCardBackground: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF1A2F33) else Color(0xFFE0F7FA)

    val ExerciseCardBackground: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF1B2E1B) else Color(0xFFE8F5E9)

    val StreaksCardBackground: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF2E2A1A) else Color(0xFFFFF8E1)

    val ExerciseScheduleBackground: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF2E2518) else Color(0xFFFFF3E0)

    // Accent text/icon colors
    val SummaryAccent: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF90CAF9) else Color(0xFF1565C0)

    val DrinksAccent: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF4DD0E1) else Color(0xFF00838F)

    val ExerciseAccent: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF81C784) else Color(0xFF2E7D32)

    val StreaksAccent: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFFFF8A65) else Color(0xFFE65100)

    val ScheduleAccent: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFFFF8A65) else Color(0xFFE65100)

    // Chart colors (slightly brighter in dark mode for contrast)
    val ChartGreen: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF81C784) else Color(0xFF66BB6A)

    val ChartRed: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFFEF5350) else Color(0xFFEF5350)

    val ChartGold: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFFFFD54F) else Color(0xFFFFCA28)

    val ChartBlue: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF64B5F6) else Color(0xFF42A5F5)

    val ChartBlueDim: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF1E3A5F) else Color(0xFFBBDEFB)

    val ChartPurple: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFFCE93D8) else Color(0xFFAB47BC)

    val ChartPurpleDim: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF3A1E5F) else Color(0xFFE1BEE7)

    // FAB colors
    val VoidFabBackground: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF4A4520) else Color(0xFFFFF9C4)

    val VoidFabContent: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFFFFEE58) else Color(0xFF827717)

    val ExerciseFabBackground: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF1B3D1B) else Color(0xFFC8E6C9)

    val ExerciseFabContent: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF81C784) else Color(0xFF2E7D32)

    val QuickAddFabBackground: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF3A2571) else Color(0xFFD1C4E9)

    val QuickAddFabContent: Color
        @Composable @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFFCE93D8) else Color(0xFF4A148C)
}
