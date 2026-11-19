package com.Kebcaqa1.Veghar.presentation.app

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kebcaqa1.Veghar.core.di.ServiceLocator
import com.Kebcaqa1.Veghar.core.navigation.Screen
import com.Kebcaqa1.Veghar.core.theme.BgBase
import com.Kebcaqa1.Veghar.presentation.collection.CollectionRoute
import com.Kebcaqa1.Veghar.presentation.game.GameRoute
import com.Kebcaqa1.Veghar.presentation.gameover.GameOverRoute
import com.Kebcaqa1.Veghar.presentation.levelmap.LevelMapRoute
import com.Kebcaqa1.Veghar.presentation.menu.MenuRoute
import com.Kebcaqa1.Veghar.presentation.missions.MissionsRoute
import com.Kebcaqa1.Veghar.presentation.settings.SettingsRoute
import com.Kebcaqa1.Veghar.presentation.splash.SplashRoute
import com.Kebcaqa1.Veghar.presentation.tutorial.TutorialRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BackHandler(enabled = true) {
        if (!appViewModel.back()) {
            (context as? Activity)?.finish()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(BgBase)) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                when (targetState) {
                    is Screen.GameOver ->
                        (fadeIn(tween(380)) + scaleIn(tween(380), initialScale = 0.94f)) togetherWith
                            fadeOut(tween(260))
                    is Screen.Game ->
                        (slideInVertically(tween(320)) { full -> full / 6 } + fadeIn(tween(320))) togetherWith
                            fadeOut(tween(220))
                    else -> fadeIn(tween(380)) togetherWith fadeOut(tween(280))
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { target ->
            when (target) {
                Screen.Splash -> SplashRoute(
                    onFinished = appViewModel::finishSplash
                )
                Screen.Menu -> MenuRoute(
                    onPlay = appViewModel::playShift,
                    onLevels = { appViewModel.open(Screen.LevelMap) },
                    onMissions = { appViewModel.open(Screen.Missions) },
                    onCollection = { appViewModel.open(Screen.Collection) },
                    onSettings = { appViewModel.open(Screen.Settings) },
                    onTutorial = { appViewModel.open(Screen.Tutorial) }
                )
                Screen.Tutorial -> TutorialRoute(
                    onBack = appViewModel::backToMenu,
                    onPlay = appViewModel::playShift
                )
                is Screen.Game -> GameRoute(
                    session = target.session,
                    onGameOver = appViewModel::finishShift,
                    onMenu = appViewModel::backToMenu
                )
                is Screen.GameOver -> GameOverRoute(
                    result = target.result,
                    onReplay = appViewModel::playShift,
                    onMenu = appViewModel::backToMenu,
                    onLevels = { appViewModel.open(Screen.LevelMap) }
                )
                Screen.LevelMap -> LevelMapRoute(
                    onBack = appViewModel::backToMenu,
                    onPlay = appViewModel::playShift
                )
                Screen.Collection -> CollectionRoute(
                    onBack = appViewModel::backToMenu,
                    onPlay = appViewModel::playShift
                )
                Screen.Missions -> MissionsRoute(
                    onBack = appViewModel::backToMenu,
                    onPlay = appViewModel::playShift
                )
                Screen.Settings -> SettingsRoute(
                    onBack = appViewModel::backToMenu
                )
            }
        }
    }
}
