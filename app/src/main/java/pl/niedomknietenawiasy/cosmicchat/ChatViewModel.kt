package pl.niedomknietenawiasy.cosmicchat

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.niedomknietenawiasy.cosmicchat.model.Api
import pl.niedomknietenawiasy.cosmicchat.model.AppDatabase
import pl.niedomknietenawiasy.cosmicchat.model.Message
import pl.niedomknietenawiasy.cosmicchat.model.User
import pl.niedomknietenawiasy.cosmicchat.model.getRandomString


class ChatViewModel(
    val db: AppDatabase,
    val pref: SharedPreferences
): ViewModel() {
    val api = Api()
    val toastMsg = "example toast msg"
    val userList = mutableStateOf(listOf<User>())
    val myId = pref.getString("userId", getRandomString())

    init {
        viewModelScope.launch {
            val users = db.userDao().getUsers()
            if (users.isNotEmpty()) {
                userList.value = users
            } else {
                userList.value = dummyUsers
                dummyUsers.forEach {
                    db.userDao().insertUser(it)
                }
            }
        }
    }

    val messageDao = db.messageDao()
    val messages = mutableStateOf(listOf<Message>())
    val currentFriend = mutableStateOf<User?>(null)

    fun loadMessages(friendId: String) {
        viewModelScope.launch {
            messages.value = messageDao.getMessagesByFriendId(friendId)
        }
    }

    fun loadFriend(friendId: String) {
        viewModelScope.launch {
            currentFriend.value = db.userDao().getUserById(friendId)
        }
    }

    fun sendMessage(message: Message) {
        messages.value += message
        viewModelScope.launch {
            messageDao.insertMessage(message)
            api.sendMessage(message)
        }
    }
}

val dummyUsers = listOf(
    User("someId", "Filip", "active"),
    User("anotherId", "Jan", "active")
)