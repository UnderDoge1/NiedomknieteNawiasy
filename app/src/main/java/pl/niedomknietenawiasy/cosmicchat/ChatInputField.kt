package pl.niedomknietenawiasy.cosmicchat

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTextField(
    modifier: Modifier = Modifier,
    input: TextFieldValue,
    empty: Boolean,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusEvent: (Boolean) -> Unit
) {
    val context = LocalContext.current
    Surface(
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(2.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.then(Modifier.size(circleButtonSize)),
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_add_reaction_24),
                    contentDescription = "emoji"
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = circleButtonSize),
                contentAlignment = Alignment.CenterStart
            ) {

                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusable(true)
                        .onFocusEvent {
                            onFocusEvent(it.hasFocus)
                        },
                    value = input,
                    onValueChange = onValueChange,
                    cursorBrush = SolidColor(Color(0xff00897B)),
                    decorationBox = { innerTextField ->
                        if (empty) {
                            Text(
                                text = "Message",
                                fontSize = 18.sp
                            )
                        }
                        innerTextField()
                    }
                )
            }

            IconButton(
                onClick = {
                    Toast.makeText(
                        context,
                        "Attach Clicked.\n(Not Available)",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier.then(Modifier.size(circleButtonSize)),
            ) {
                Icon(
                    modifier = Modifier.rotate(-45f),
                    painter = painterResource(R.drawable.baseline_attach_file_24),
                    contentDescription = "attach"
                )
            }
            AnimatedVisibility(visible = empty) {
                IconButton(
                    onClick = {
                        Toast.makeText(
                            context,
                            "Send Photo Clicked.\n(Not Available)",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier.then(Modifier.size(circleButtonSize)),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_add_a_photo_24),
                        contentDescription = "camera"
                    )
                }
            }
        }
    }
}

val circleButtonSize = 44.dp