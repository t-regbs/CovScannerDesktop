// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import components.Carousel
import components.SplashUI
import kotlinx.coroutines.InternalCoroutinesApi
import model.ContentState
import style.MarrColor
import style.icAppIcon
import style.icEmptyImage
import utils.getPreferredWindowSize
import utils.imageFromFile
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@InternalCoroutinesApi
@Composable
@Preview
fun App() {
    val isPickerOpen = remember { mutableStateOf(false) }
    val selectedFile: MutableState<File?> = remember { mutableStateOf(null) }
    val selected: MutableState<List<File?>?> = remember { mutableStateOf(null) }
    if (isPickerOpen.value) {
        FileDialog(
            onCloseRequest = {
                isPickerOpen.value = false
                println("Result ${it?.last()}")
                selected.value = it?.toList()
                selectedFile.value = it!!.last()
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
                if (selected.value != null) {
                    ScrollableArea(selected.value)
                } else {
                    Card(
                        modifier = Modifier
                            .width(261.dp)
                            .height(316.dp),
                        shape = MaterialTheme.shapes.medium.copy(CornerSize(24.dp)),
                        elevation = 8.dp
                    ) {
                        Image(
                            if (selectedFile.value == null) icEmptyImage()
                            else BitmapPainter(imageFromFile(selectedFile.value!!)),
                            contentDescription = null,
                            contentScale = if (selectedFile.value == null) ContentScale.Fit
                            else ContentScale.FillBounds
                        )
                    }
                }
                Spacer(Modifier.height(25.dp))
                Text(
                    modifier = Modifier
                        .clickable {
                            isPickerOpen.value = !isPickerOpen.value
                        },
                    text = if (selected.value == null) "Upload" else "Re-Upload",
                    lineHeight = 23.87.sp,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = MarrColor
                )
                Spacer(Modifier.height(25.dp))
                CovButton(
                    title = "Run scanner",
                    onClick = {
//                        val cxrModel = Covid.newInstance(context)
//                        val bitmaps = mutableListOf<Bitmap>()
//                        for (uri in result!!) {
//                            bitmaps.add(uriToBitmap(uri!!, context))
//                        }
//                        viewModel.process(cxrModel, bitmaps)
                    },
                    color = MarrColor,
//                    enabled = !(state is UploadState.Init || state is UploadState.Loading)
                )
            }
        }
    }
}

@InternalCoroutinesApi
fun main() = application {
    val state = rememberWindowState()
    val content = remember { ContentState.applyContent(state) }
    val icon = icAppIcon()

    if (content.isAppReady()) {
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
    } else {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Cov Scanner",
            state = WindowState(
                position = WindowPosition.Aligned(Alignment.Center),
                size = getPreferredWindowSize(800, 300)
            ),
            undecorated = true,
            icon = icon,
        ) {
            SplashUI()
        }
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
        object : FileDialog(parent, "Choose a File or Files", LOAD) {
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

@Composable
fun CovButton(
    title: String,
    icon: ImageVector? = null,
    onClick: () -> Unit = {},
    color: Color,
    textColor: Color = Color.White,
    enabled: Boolean = true
) {
    Button(
        modifier = Modifier
            .height(70.dp)
            .width(350.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
        enabled = enabled
    ) {
        if (icon != null) {
            Icon(
                modifier = Modifier.size(30.dp),
                tint = Color.White,
                imageVector = icon,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = textColor
        )
    }
}

@InternalCoroutinesApi
@Composable
fun ScrollableArea(result: List<File?>?) {
    Box {
        val stateHorizontal = rememberLazyListState(0)
        Carousel(result, stateHorizontal)
        HorizontalScrollbar(
            adapter = rememberScrollbarAdapter(stateHorizontal),
            style = LocalScrollbarStyle.current.copy(hoverColor = MarrColor.copy(alpha = 0.65f)),
            modifier = Modifier.align(Alignment.BottomStart)
                .fillMaxWidth()
        )
    }
}
