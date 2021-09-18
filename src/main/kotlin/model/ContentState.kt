package model

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.window.WindowState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ContentState {

    lateinit var windowState: WindowState
    val scope = CoroutineScope(Dispatchers.IO)

    fun applyContent(state: WindowState): ContentState {
        windowState = state
        isContentReady.value = false

        initData()

        return this
    }

    private val isAppReady = mutableStateOf(false)
    fun isAppReady(): Boolean {
        return isAppReady.value
    }

    private val isContentReady = mutableStateOf(false)
    fun isContentReady(): Boolean {
        return isContentReady.value
    }

    private fun initData() {
        if (isContentReady.value)
            return

        scope.launch(Dispatchers.IO) {
            delay(5000)
            onContentReady()
        }
    }

    private fun onContentReady() {
        isContentReady.value = true
        isAppReady.value = true
    }
}
