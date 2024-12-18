package pl.niedomknietenawiasy.cosmicchat.ui.components

import androidx.compose.material.icons.outlined.Send
import androidx.compose.ui.res.painterResource
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import pl.niedomknietenawiasy.cosmicchat.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatAppBar(
    modifier: Modifier = Modifier,
    title: String = "Title",
    description: String = "Description",
    onUserNameClick: (() -> Unit)? = null,
    onBackArrowClick: (() -> Unit)? = null,
    onUserProfilePictureClick: (() -> Unit)? = null,
    onMoreDropDownBlockUserClick: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    SmallTopAppBar(
        modifier = Modifier.statusBarsPadding(),
        title = {
            Row {
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape,
                    color = Color.LightGray
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .clickable { onUserProfilePictureClick?.invoke() })
                }
                Column(
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .clickable {
                            onUserNameClick?.invoke()
                        },
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { onBackArrowClick?.invoke() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    Toast.makeText(
                        context,
                        "Videochat Clicked.\n(Not Available)",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                Icon(painter = painterResource(R.drawable.baseline_video_call_24), contentDescription = null)
            }
            IconButton(
                onClick = {
                    Toast.makeText(
                        context,
                        "Voicechat Clicked.\n(Not Available)",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                Icon(imageVector = Icons.Filled.Call, contentDescription = null)
            }
            IconButton(
                onClick = {
                    expanded = true
                }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "Block User")
                        },
                        onClick = {
                            onMoreDropDownBlockUserClick?.invoke()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Send,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    )
}