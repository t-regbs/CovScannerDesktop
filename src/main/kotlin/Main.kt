// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import style.icAppIcon
import style.icEmptyImage
import utils.getPreferredWindowSize
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
@Preview
fun App() {
    val isPickerOpen = remember { mutableStateOf(false) }
    if (isPickerOpen.value) {
        FileDialog(
            onCloseRequest = {
                isPickerOpen.value = false
                println("Result ${it?.last()}")
            }
        )
    }
    DesktopMaterialTheme {
        Column {
            TitleBar(title = "Covid Scanner")
            Spacer(Modifier.height(50.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 24.dp)
                    .scrollable(state = rememberScrollState(), orientation = Orientation.Vertical),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .width(261.dp)
                        .height(316.dp),
                    shape = MaterialTheme.shapes.medium.copy(CornerSize(24.dp)),
                    elevation = 8.dp
                ) {
                    Image(
                        icEmptyImage(),
                        contentDescription = null,
                    )
                }

                Spacer(Modifier.height(25.dp))
                Text(
                    modifier = Modifier
                        .clickable {
                            isPickerOpen.value = !isPickerOpen.value
                        },
                    text = "Upload",
                    lineHeight = 23.87.sp,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
//                    color = marrColor
                )
            }
        }
    }
}

fun main() = application {
    val icon = icAppIcon()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Cov Scanner",
        icon = icon,
        state = WindowState(
            position = WindowPosition.Aligned(Alignment.Center),
            size = getPreferredWindowSize(800, 1000)
        ),
    ) {
        App()
    }
}

@Composable
fun TitleBar(title: String, showBack: Boolean = false, onBackPressed: (() -> Unit)? = null) {
    val pad = if (showBack) 56.dp else 0.dp
    TopAppBar(
        title = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    modifier = Modifier.padding(end = pad),
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start
                )
            }
        },
        navigationIcon = if (showBack) {
            {
                IconButton(onClick = { onBackPressed?.invoke() }) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
            }
            }
        } else null,
        backgroundColor = Color.White,
    )
}

@Composable
private fun FileDialog(
    parent: Frame? = null,
    onCloseRequest: (result: Array<File>?) -> Unit,
) = AwtWindow(
    create = {
        object : FileDialog(parent, "Choose a File", LOAD) {
            override fun setVisible(b: Boolean) {
                isMultipleMode = true
                super.setVisible(b)
                if (b) {
                    onCloseRequest(files)
                }
            }
        }
    },
    dispose = FileDialog::dispose
)
