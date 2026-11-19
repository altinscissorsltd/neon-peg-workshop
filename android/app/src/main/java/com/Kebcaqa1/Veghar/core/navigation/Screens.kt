package com.Kebcaqa1.Veghar.core.navigation

import com.Kebcaqa1.Veghar.domain.model.ShiftResult

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object Tutorial : Screen
    data class Game(val session: Long) : Screen
    data class GameOver(val result: ShiftResult) : Screen
    data object LevelMap : Screen
    data object Collection : Screen
    data object Missions : Screen
    data object Settings : Screen
}
