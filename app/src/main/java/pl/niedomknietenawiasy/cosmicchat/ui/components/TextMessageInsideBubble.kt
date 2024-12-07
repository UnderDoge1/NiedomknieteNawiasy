package pl.niedomknietenawiasy.cosmicchat.ui.components

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit


@Composable
fun TextMessageInsideBubble(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    messageStat: @Composable () -> Unit,
    onMeasure: ((ChatRowData) -> Unit)? = null
) {
    val chatRowData = remember { ChatRowData() }
    val content = @Composable {

        Text(
            modifier = modifier
                .wrapContentSize(),
            text = text,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            style = style,
            maxLines = maxLines,
            onTextLayout = { textLayoutResult: TextLayoutResult ->
                chatRowData.lineCount = textLayoutResult.lineCount
                chatRowData.lastLineWidth =
                    textLayoutResult.getLineRight(chatRowData.lineCount - 1)
                chatRowData.textWidth = textLayoutResult.size.width
            }
        )

        messageStat()
    }

    Layout(
        modifier = modifier,
        content = content
    ) { measurables: List<Measurable>, constraints: Constraints ->

        if (measurables.size != 2)
            throw IllegalArgumentException("There should be 2 components for this layout")

        val placeables: List<Placeable> = measurables.map { measurable ->
            measurable.measure(Constraints(0, constraints.maxWidth))
        }

        val message = placeables.first()
        val status = placeables.last()

        if ((chatRowData.rowWidth == 0 || chatRowData.rowHeight == 0) || chatRowData.text != text) {
            chatRowData.parentWidth = constraints.maxWidth
            calculateChatWidthAndHeight(text, chatRowData, message, status)
            chatRowData.parentWidth =
                chatRowData.rowWidth.coerceAtLeast(minimumValue = constraints.minWidth)
        }

        onMeasure?.invoke(chatRowData)

        layout(width = chatRowData.parentWidth, height = chatRowData.rowHeight) {

            message.placeRelative(0, 0)
            status.placeRelative(
                chatRowData.parentWidth - status.width,
                chatRowData.rowHeight - status.height
            )
        }
    }
}

fun calculateChatWidthAndHeight(
    text: String,
    chatRowData: ChatRowData,
    message: Placeable,
    status: Placeable?,
) {

    if (status != null) {

        val lineCount = chatRowData.lineCount
        val lastLineWidth = chatRowData.lastLineWidth
        val parentWidth = chatRowData.parentWidth

        val padding = (message.measuredWidth - chatRowData.textWidth) / 2

        if (lineCount > 1 && lastLineWidth + status.measuredWidth >= chatRowData.textWidth + padding) {
            chatRowData.rowWidth = message.measuredWidth
            chatRowData.rowHeight = message.measuredHeight + status.measuredHeight
            chatRowData.measuredType = 0
        } else if (lineCount > 1 && lastLineWidth + status.measuredWidth < chatRowData.textWidth + padding) {
            chatRowData.rowWidth = message.measuredWidth
            chatRowData.rowHeight = message.measuredHeight
            chatRowData.measuredType = 1
        } else if (lineCount == 1 && message.width + status.measuredWidth >= parentWidth) {
            chatRowData.rowWidth = message.measuredWidth
            chatRowData.rowHeight = message.measuredHeight + status.measuredHeight
            chatRowData.measuredType = 2
        } else {
            chatRowData.rowWidth = message.measuredWidth + status.measuredWidth
            chatRowData.rowHeight = message.measuredHeight
            chatRowData.measuredType = 3
        }
    } else {
        chatRowData.rowWidth = message.width
        chatRowData.rowHeight = message.height
    }
}