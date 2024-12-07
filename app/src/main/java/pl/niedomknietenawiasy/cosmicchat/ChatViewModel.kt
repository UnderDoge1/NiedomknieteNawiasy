package pl.niedomknietenawiasy.cosmicchat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import pl.niedomknietenawiasy.cosmicchat.model.Message
import pl.niedomknietenawiasy.cosmicchat.model.MessageStatus
import pl.niedomknietenawiasy.cosmicchat.model.User
import java.time.LocalDateTime
import java.util.UUID

class ChatViewModel : ViewModel() {
    val toastMsg = "example toast msg"
    val messages = mutableStateOf(
        listOf(
            Message(
                UUID.randomUUID(),
                "elko",
                MessageStatus.RECEIVED,
                LocalDateTime.of(2024, 12, 7, 15, 15)
            ),
            Message(
                UUID.randomUUID(),
                "belko",
                MessageStatus.SENT,
                LocalDateTime.of(2024, 12, 7, 15, 27)
            ),
        )
    )
    val userList = mutableStateOf(
        listOf(
            User(UUID.randomUUID(), "elko", "active"),
            User(UUID.randomUUID(), "belko", "active")
        )
    )

    fun loadMessages() {

    }

    fun sendMessage() {

    }
}