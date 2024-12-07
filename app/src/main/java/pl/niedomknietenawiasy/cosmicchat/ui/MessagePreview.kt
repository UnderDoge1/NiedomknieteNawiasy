package pl.niedomknietenawiasy.cosmicchat.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MessagePreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            drawRect(
                topLeft = Offset(0f, 0f),
                size = Size(50f ,50f),
                color = Color.Red
            )

            // Kwadrat w prawym górnym rogu
            drawRect(
                topLeft = Offset(canvasWidth - 50.dp.toPx(), 0f),
                size = Size(50f ,50f),
                color = Color.Green
            )

            // Kwadrat w lewym dolnym rogu
            drawRect(
                topLeft = Offset(0f, canvasHeight - 50.dp.toPx()),
                size = Size(50f ,50f),
                color = Color.Blue
            )

            // Kwadrat w prawym dolnym rogu
            drawRect(
                topLeft = Offset(canvasWidth - 50.dp.toPx(), canvasHeight - 50.dp.toPx()),
                size = Size(50f ,50f),
                color = Color.Yellow
            )

            // Koło w środku
            drawCircle(
                color = Color.Magenta,
                radius = 50f,
                center = Offset(canvasWidth / 2, canvasHeight / 2)
            )
        }
    }
}