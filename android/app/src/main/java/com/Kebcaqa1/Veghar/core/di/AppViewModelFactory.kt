package com.Kebcaqa1.Veghar.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Kebcaqa1.Veghar.domain.repository.ProgressRepository
import com.Kebcaqa1.Veghar.domain.repository.SettingsRepository
import com.Kebcaqa1.Veghar.domain.usecase.AdvanceMissionsUseCase
import com.Kebcaqa1.Veghar.domain.usecase.BuildCalibrationUseCase
import com.Kebcaqa1.Veghar.domain.usecase.ResolveDropUseCase
import com.Kebcaqa1.Veghar.domain.usecase.ScoreDropUseCase
import com.Kebcaqa1.Veghar.domain.usecase.UnlockStageUseCase
import com.Kebcaqa1.Veghar.presentation.app.AppViewModel
import com.Kebcaqa1.Veghar.presentation.collection.CollectionViewModel
import com.Kebcaqa1.Veghar.presentation.game.GameViewModel
import com.Kebcaqa1.Veghar.presentation.gameover.GameOverViewModel
import com.Kebcaqa1.Veghar.presentation.levelmap.LevelMapViewModel
import com.Kebcaqa1.Veghar.presentation.menu.MenuViewModel
import com.Kebcaqa1.Veghar.presentation.missions.MissionsViewModel
import com.Kebcaqa1.Veghar.presentation.settings.SettingsViewModel
import com.Kebcaqa1.Veghar.presentation.splash.SplashViewModel
import com.Kebcaqa1.Veghar.presentation.tutorial.TutorialViewModel

class AppViewModelFactory(
    private val progress: ProgressRepository,
    private val settings: SettingsRepository,
    private val resolveDrop: ResolveDropUseCase,
    private val scoreDrop: ScoreDropUseCase,
    private val buildCalibration: BuildCalibrationUseCase,
    private val advanceMissions: AdvanceMissionsUseCase,
    private val unlockStage: UnlockStageUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel(progress)
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(progress, settings)
            modelClass.isAssignableFrom(TutorialViewModel::class.java) -> TutorialViewModel(progress)
            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(
                progress = progress,
                settings = settings,
                resolveDrop = resolveDrop,
                scoreDrop = scoreDrop,
                buildCalibration = buildCalibration,
                advanceMissions = advanceMissions,
                unlockStage = unlockStage
            )
            modelClass.isAssignableFrom(GameOverViewModel::class.java) -> GameOverViewModel(progress)
            modelClass.isAssignableFrom(LevelMapViewModel::class.java) -> LevelMapViewModel(progress)
            modelClass.isAssignableFrom(CollectionViewModel::class.java) -> CollectionViewModel(progress)
            modelClass.isAssignableFrom(MissionsViewModel::class.java) -> MissionsViewModel(progress)
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(progress, settings)
            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
