package com.Kebcaqa1.Veghar.domain.usecase

import com.Kebcaqa1.Veghar.domain.model.Stage

class UnlockStageUseCase {

    operator fun invoke(totalCharge: Int, stages: List<Stage>): Int {
        var unlocked = 1
        stages.forEach { stage ->
            if (totalCharge >= stage.chargeThreshold && stage.id > unlocked) unlocked = stage.id
        }
        return unlocked
    }
}
