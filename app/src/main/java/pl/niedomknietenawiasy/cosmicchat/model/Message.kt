package pl.niedomknietenawiasy.cosmicchat.model

import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

data class Message(
    val senderId: UUID,
    val content: String,
    val status: MessageStatus,
    val date: LocalDateTime
)
