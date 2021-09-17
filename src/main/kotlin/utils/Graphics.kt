package utils

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowSize
import java.awt.Dimension
import java.awt.Toolkit

fun getPreferredWindowSize(desiredWidth: Int, desiredHeight: Int): WindowSize {
    val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
    val preferredWidth: Int = (screenSize.width * 0.8f).toInt()
    val preferredHeight: Int = (screenSize.height * 0.8f).toInt()
    val width: Int = if (desiredWidth < preferredWidth) desiredWidth else preferredWidth
    val height: Int = if (desiredHeight < preferredHeight) desiredHeight else preferredHeight
    return WindowSize(width.dp, height.dp)
}
