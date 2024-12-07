package pl.niedomknietenawiasy.cosmicchat.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pl.niedomknietenawiasy.cosmicchat.ChatViewModel
import pl.niedomknietenawiasy.cosmicchat.model.User

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserListScreen(
    viewModel: ChatViewModel,
    navController: NavController
) {
    val users by viewModel.userList

    LazyColumn {
        stickyHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    fontWeight = FontWeight.W400,
                    fontSize = 24.sp,
                    text = "Users"
                )
            }
        }
        items(users) { user ->
            UserListItem(
                user = user,
                goToChat = {
                    navController.navigate("chat/${user.id}")
                }
            )
        }
    }
}

@Composable
fun UserListItem(
    user: User,
    goToChat: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .width(24.dp)
                    .aspectRatio(1f))
            Text(
                text = user.name,
                fontSize = 20.sp
            )
        }
        IconButton(
            onClick = goToChat
        ) { Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null) }
    }
}