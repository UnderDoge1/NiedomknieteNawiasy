package pl.niedomknietenawiasy.cosmicchat.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(message: User)

    @Query("SELECT * FROM users")
    suspend fun getUsers(): List<User>

    @Query("SELECT * FROM messages WHERE id = :userId")
    suspend fun getUserById(userId: String): User
}