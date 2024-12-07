package pl.niedomknietenawiasy.cosmicchat.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.niedomknietenawiasy.cosmicchat.ChatViewModel
import pl.niedomknietenawiasy.cosmicchat.model.User

@Composable
fun UserListScreen(
    viewModel: ChatViewModel,
    navController: NavController
) {
    val users by viewModel.userList

    LazyColumn {
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
        Text(text = user.name)
        IconButton(
            onClick = goToChat
        ) { Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null) }
    }
}