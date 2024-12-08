package pl.niedomknietenawiasy.cosmicchat.ui

import com.example.chatwithme.core.SnackbarController

import androidx.compose.foundation.lazy.items
import android.widget.Toast
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.chatwithme.presentation.chat.chatrow.ReceivedMessageRow
import com.example.chatwithme.presentation.chat.chatrow.SentMessageRow
import pl.niedomknietenawiasy.cosmicchat.ChatViewModel
import pl.niedomknietenawiasy.cosmicchat.model.Message
import pl.niedomknietenawiasy.cosmicchat.model.MessageStatus
import pl.niedomknietenawiasy.cosmicchat.model.User
import pl.niedomknietenawiasy.cosmicchat.ui.components.ChatAppBar
import pl.niedomknietenawiasy.cosmicchat.ui.components.ChatInput
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ChatScreen(
    friendId: String,
    chatViewModel: ChatViewModel,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    val toastMessage = chatViewModel.toastMsg
    LaunchedEffect(key1 = toastMessage) {
        if (toastMessage != "") {
            SnackbarController(this).showSnackbar(snackbarHostState, toastMessage, "Close")
        }
    }
    LaunchedEffect(key1 = Unit) {
        chatViewModel.loadMessages(friendId)
        chatViewModel.loadFriend(friendId)
    }
    ChatScreenContent(
        chatViewModel,
        navController,
    )

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatScreenContent(
    chatViewModel: ChatViewModel,
    navController: NavHostController,
) {
    val messages = chatViewModel.messages
    val friend = chatViewModel.currentFriend
    val myId = chatViewModel.myId
    val keyboardController = LocalSoftwareKeyboardController.current

    var showDialog by remember {
        mutableStateOf(false)
    }

    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = messages.value.size)
    var isChatInputFocus by remember {
        mutableStateOf(false)
    }

    val imePaddingValues = PaddingValues()
    val imeBottomPadding = imePaddingValues.calculateBottomPadding().value.toInt()
    LaunchedEffect(key1 = imeBottomPadding) {
        if (messages.value.isNotEmpty()) {
            scrollState.scrollToItem(
                index = messages.value.size - 1
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusable()
            .wrapContentHeight()
            .imePadding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { keyboardController?.hide() })
            }
    ) {
        val context = LocalContext.current

        ChatAppBar(
            title = friend.value?.name ?: "",
            description = friend.value?.status ?: "",
            onUserNameClick = {
                Toast.makeText(context, "User Profile Clicked", Toast.LENGTH_SHORT).show()
            }, onBackArrowClick = {
                chatViewModel.leaveChat()
                navController.popBackStack()
            }, onUserProfilePictureClick = {
                showDialog = true
            },
            onMoreDropDownBlockUserClick = {
                //POC
            }
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = scrollState
        ) {
            items(messages.value) { message ->
                if (message.senderId == myId) {
                    SentMessageRow(
                        text = message.content,
                        quotedMessage = null,
                        messageTime = "${message.date.hour}:${message.date.minute}",
                        messageStatus = message.status
                    )
                } else {
                    ReceivedMessageRow(
                        text = message.content,
                        opponentName = friend.value?.name ?: "",
                        quotedMessage = null,
                        messageTime = "${message.date.hour}:${message.date.minute}"
                    )
                }
            }

        }
        ChatInput(
            modifier = Modifier.padding(4.dp),
            onMessageChange = { messageContent ->
                chatViewModel.sendMessage(
                    Message(
                        senderId = myId!!,
                        receiverId = friend.value?.id ?: "",
                        content = messageContent,
                        date = LocalDateTime.now(),
                        status = MessageStatus.SENT
                    )
                )
            },
            onFocusEvent = {
                isChatInputFocus = it
            }
        )
    }
}