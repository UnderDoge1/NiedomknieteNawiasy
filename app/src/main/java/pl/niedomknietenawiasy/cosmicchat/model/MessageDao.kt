package pl.niedomknietenawiasy.cosmicchat.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface MessageDao {
    @Insert
    suspend fun insertMessage(message: Message)

    @Query("SELECT * FROM messages WHERE senderId = :senderId")
    suspend fun getMessagesBySender(senderId: String): List<Message>

    @Query("SELECT * FROM messages WHERE senderId = :senderId OR receiverId = :senderId")
    suspend fun getMessagesByFriendId(senderId: String): List<Message>

    @Query("SELECT * FROM messages WHERE senderId = :senderId")
    fun getMessagesBySenderAsync(senderId: String): Flow<List<Message>>
}