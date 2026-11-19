package com.Kebcaqa1.Veghar.presentation.missions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kebcaqa1.Veghar.data.sample.SampleData
import com.Kebcaqa1.Veghar.domain.model.Progress
import com.Kebcaqa1.Veghar.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MissionsViewModel(
    private val progress: ProgressRepository
) : ViewModel() {

    private val mutableState = MutableStateFlow(build(progress.progress.value, MissionsNotice.NONE))
    val state: StateFlow<MissionsUiState> = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            progress.progress.collect { saved ->
                mutableState.update { current -> build(saved, current.notice) }
            }
        }
    }

    private fun build(saved: Progress, notice: MissionsNotice): MissionsUiState {
        val missions = SampleData.missions.map { mission ->
            val done = saved.missionProgress[mission.id] ?: 0
            val claimed = saved.claimedMissions.contains(mission.id)
            MissionCard(
                id = mission.id,
                progress = minOf(done, mission.target),
                target = mission.target,
                reward = mission.reward,
                claimed = claimed,
                ready = !claimed && done >= mission.target
            )
        }
        val tracks = SampleData.rewardTracks.map { track ->
            val done = saved.trackProgress[track.id] ?: 0
            val claimed = saved.claimedTracks.contains(track.id)
            TrackCard(
                id = track.id,
                progress = minOf(done, track.target),
                target = track.target,
                reward = track.reward,
                claimed = claimed,
                ready = !claimed && done >= track.target
            )
        }
        return MissionsUiState(
            missions = missions,
            tracks = tracks,
            notice = notice,
            allClaimed = missions.all { it.claimed } && tracks.all { it.claimed }
        )
    }

    fun onClaimMission(missionId: String) {
        val card = mutableState.value.missions.firstOrNull { it.id == missionId } ?: return
        if (!card.ready) {
            mutableState.update { it.copy(notice = MissionsNotice.NOT_READY) }
            return
        }
        mutableState.update { it.copy(notice = MissionsNotice.NONE) }
        progress.claimMission(card.id, card.reward)
    }

    fun onClaimTrack(trackId: String) {
        val card = mutableState.value.tracks.firstOrNull { it.id == trackId } ?: return
        if (!card.ready) {
            mutableState.update { it.copy(notice = MissionsNotice.NOT_READY) }
            return
        }
        mutableState.update { it.copy(notice = MissionsNotice.NONE) }
        progress.claimTrack(card.id, card.reward)
    }
}
