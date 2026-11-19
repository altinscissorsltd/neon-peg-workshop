package com.Kebcaqa1.Veghar.core.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.Kebcaqa1.Veghar.core.config.GameConfig
import com.Kebcaqa1.Veghar.data.local.PreferencesStore
import com.Kebcaqa1.Veghar.data.repository.ProgressRepositoryImpl
import com.Kebcaqa1.Veghar.data.repository.SettingsRepositoryImpl
import com.Kebcaqa1.Veghar.domain.repository.ProgressRepository
import com.Kebcaqa1.Veghar.domain.repository.SettingsRepository
import com.Kebcaqa1.Veghar.domain.usecase.AdvanceMissionsUseCase
import com.Kebcaqa1.Veghar.domain.usecase.BuildCalibrationUseCase
import com.Kebcaqa1.Veghar.domain.usecase.ResolveDropUseCase
import com.Kebcaqa1.Veghar.domain.usecase.ScoreDropUseCase
import com.Kebcaqa1.Veghar.domain.usecase.UnlockStageUseCase

object ServiceLocator {

    private var factory: ViewModelProvider.Factory? = null

    fun initialize(context: Context) {
        if (factory != null) return
        val store = PreferencesStore(context.applicationContext)
        val progress: ProgressRepository = ProgressRepositoryImpl(
            store = store,
            advanceMissions = AdvanceMissionsUseCase(),
            unlockStage = UnlockStageUseCase()
        )
        val settings: SettingsRepository = SettingsRepositoryImpl(store)
        factory = AppViewModelFactory(
            progress = progress,
            settings = settings,
            resolveDrop = ResolveDropUseCase(
                lanes = GameConfig.LANES,
                rows = GameConfig.PEG_ROWS,
                centerLane = GameConfig.CENTER_LANE,
                slipChance = GameConfig.PEG_SLIP_CHANCE,
                gateSpan = GameConfig.GATE_SPAN
            ),
            scoreDrop = ScoreDropUseCase(
                basePayout = GameConfig.STAGE_BASE_PAYOUT,
                overchargeFactor = GameConfig.OVERCHARGE_FACTOR,
                calibrationFactor = GameConfig.CALIBRATION_FACTOR
            ),
            buildCalibration = BuildCalibrationUseCase(
                pads = GameConfig.CALIBRATION_PADS,
                steps = GameConfig.CALIBRATION_STEPS
            ),
            advanceMissions = AdvanceMissionsUseCase(),
            unlockStage = UnlockStageUseCase()
        )
    }

    val viewModelFactory: ViewModelProvider.Factory
        get() = checkNotNull(factory)
}
