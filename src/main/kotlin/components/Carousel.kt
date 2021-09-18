package components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.InternalCoroutinesApi
import style.HomeTextColor
import utils.imageFromFile
import java.io.File

@InternalCoroutinesApi
@Composable
fun Carousel(
    result: List<File?>?,
    stateHorizontal: LazyListState,
) {
    LazyRow(
        state = stateHorizontal,
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(count = result!!.size) { page ->
            Column {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(200.dp),
                    text = result[page]?.name ?: "untitled",
                    maxLines = 1,
                    lineHeight = 35.sp,
                    textAlign = TextAlign.Center,
                    fontSize = 17.sp,
                    color = HomeTextColor
                )
                Spacer(Modifier.height(4.dp))
                Card(
                    modifier = Modifier
                        .width(261.dp)
                        .height(316.dp)
                        .padding(horizontal = 8.dp),
                    shape = MaterialTheme.shapes.medium.copy(CornerSize(24.dp)),
                    elevation = 8.dp
                ) {
                    result[page]?.let { it1 -> imageFromFile(it1) }
                        ?.let { it2 ->
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = BitmapPainter(it2),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds
                            )
                        }
                }
            }
        }
    }
}
