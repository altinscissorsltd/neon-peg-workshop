package com.Kebcaqa1.Veghar.domain.repository

import com.Kebcaqa1.Veghar.domain.model.ModuleKind
import com.Kebcaqa1.Veghar.domain.model.Progress
import com.Kebcaqa1.Veghar.domain.model.ShiftResult
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {
    val progress: StateFlow<Progress>

    fun applyShift(result: ShiftResult)

    fun selectStage(stageId: Int)

    fun buyModule(moduleId: String, price: Int): Boolean

    fun equipModule(moduleId: String, kind: ModuleKind)

    fun claimMission(missionId: String, reward: Int): Boolean

    fun claimTrack(trackId: String, reward: Int): Boolean

    fun registerVisit()

    fun markTutorialSeen()

    fun eraseProgress()
}
