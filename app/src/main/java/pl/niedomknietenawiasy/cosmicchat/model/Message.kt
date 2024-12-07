package pl.niedomknietenawiasy.cosmicchat.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val senderId: String,
    val content: String,
    val status: MessageStatus,
    val date: LocalDateTime
)