package com.Kebcaqa1.Veghar.presentation.app

import androidx.lifecycle.ViewModel
import com.Kebcaqa1.Veghar.core.navigation.Screen
import com.Kebcaqa1.Veghar.domain.model.ShiftResult
import com.Kebcaqa1.Veghar.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel(
    progress: ProgressRepository
) : ViewModel() {

    private val current = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = current.asStateFlow()

    private val backStack = ArrayList<Screen>()
    private var session = 0L

    init {
        progress.registerVisit()
    }

    fun finishSplash() {
        backStack.clear()
        current.value = Screen.Menu
    }

    fun open(target: Screen) {
        val now = current.value
        if (now != Screen.Splash) backStack.add(now)
        current.value = target
    }

    fun playShift() {
        session += 1
        backStack.clear()
        backStack.add(Screen.Menu)
        current.value = Screen.Game(session)
    }

    fun finishShift(result: ShiftResult) {
        current.value = Screen.GameOver(result)
    }

    fun backToMenu() {
        backStack.clear()
        current.value = Screen.Menu
    }

    fun back(): Boolean {
        if (backStack.isEmpty()) return false
        current.value = backStack.removeAt(backStack.lastIndex)
        return true
    }
}
