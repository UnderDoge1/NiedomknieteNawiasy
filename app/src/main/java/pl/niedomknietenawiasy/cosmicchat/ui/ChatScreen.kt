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
import androidx.navigation.NavHostController
import com.example.chatwithme.presentation.chat.chatrow.ReceivedMessageRow
import com.example.chatwithme.presentation.chat.chatrow.SentMessageRow
import pl.niedomknietenawiasy.cosmicchat.ChatViewModel
import pl.niedomknietenawiasy.cosmicchat.model.User
import pl.niedomknietenawiasy.cosmicchat.ui.components.ChatAppBar
import pl.niedomknietenawiasy.cosmicchat.ui.components.ChatInput
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(
    chatRoomUUID: String,
    opponentUUID: String,
    registerUUID: String,
    oneSignalUserId: String,
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
    chatViewModel.loadMessages()//chatRoomUUID, opponentUUID, registerUUID)
    ChatScreenContent(
        chatRoomUUID,
        opponentUUID,
        registerUUID,
        oneSignalUserId,
        chatViewModel,
        navController,
    )

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatScreenContent(
    chatRoomUUID: String,
    opponentUUID: String,
    myId: String,
    oneSignalUserId: String,
    chatViewModel: ChatViewModel,
    navController: NavHostController,
) {
    val messages = chatViewModel.messages
    val keyboardController = LocalSoftwareKeyboardController.current

    var opponentProfileFromFirebase by remember {
        mutableStateOf(User(UUID.randomUUID(), "example", "active"))
    }
    val opponentName = opponentProfileFromFirebase.name
    val opponentStatus = opponentProfileFromFirebase.status

    var showDialog by remember {
        mutableStateOf(false)
    }

    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = messages.value.size)
//    val messagesLoadedFirstTime = chatViewModel.messagesLoadedFirstTime.value
//    val messageInserted = chatViewModel.messageInserted.value
    var isChatInputFocus by remember {
        mutableStateOf(false)
    }
//    LaunchedEffect(key1 = messagesLoadedFirstTime, messages, messageInserted) {
//        if (messages.isNotEmpty()) {
//            scrollState.scrollToItem(
//                index = messages.size - 1
//            )
//        }
//    }

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
            title = "$opponentName",
            description = opponentStatus.lowercase(),
            onUserNameClick = {
                Toast.makeText(context, "User Profile Clicked", Toast.LENGTH_SHORT).show()
            }, onBackArrowClick = {
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
                when (message.senderId.toString() == myId) {
                    true -> {
                        ReceivedMessageRow(
                            text = message.content,
                            opponentName = opponentName,
                            quotedMessage = null,
                            messageTime = message.date.format(DateTimeFormatter.ISO_DATE_TIME)
                        )
                    }

                    false -> {
                        SentMessageRow(
                            text = message.content,
                            quotedMessage = null,
                            messageTime = message.date.format(DateTimeFormatter.ISO_DATE_TIME),
                            messageStatus = message.status
                        )
                    }
                }
            }

        }
        ChatInput(
            onMessageChange = { messageContent ->
                chatViewModel.sendMessage()
                //TODO
            },
            onFocusEvent = {
                isChatInputFocus = it
            }
        )
    }
}