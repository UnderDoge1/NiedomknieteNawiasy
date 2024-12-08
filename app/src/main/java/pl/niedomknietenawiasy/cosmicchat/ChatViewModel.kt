package pl.niedomknietenawiasy.cosmicchat

import SocketIOClient
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.niedomknietenawiasy.cosmicchat.model.ApiService
import pl.niedomknietenawiasy.cosmicchat.model.AppDatabase
import pl.niedomknietenawiasy.cosmicchat.model.Message
import pl.niedomknietenawiasy.cosmicchat.model.MessageStatus
import pl.niedomknietenawiasy.cosmicchat.model.User
import pl.niedomknietenawiasy.cosmicchat.model.getRandomString
import java.time.LocalDateTime


class ChatViewModel(
    val db: AppDatabase,
    val pref: SharedPreferences
): ViewModel() {
    val api = ApiService()
    val toastMsg = "example toast msg"
    val userList = mutableStateOf(listOf<User>())
    val myId = pref.getString("userId", "").let {
        if (it.isNullOrEmpty()) {
            val newId = getRandomString()
            pref.edit().putString("userId", newId).apply()
            newId
        } else { it }
    }
    val serverUrl = "ws://10.19.200.54:5000/chat"
    val webSocketClient = SocketIOClient()

    init {
        viewModelScope.launch {
            val users = db.userDao().getUsers()
            if (users.isNotEmpty()) {
                userList.value = users.filter { it.id != myId }
            } else {
                userList.value = dummyUsers
                (dummyUsers + User(myId, "Paweł", "active")).forEach {
                    db.userDao().insertUser(it)
                }
            }
        }
    }

    val messageDao = db.messageDao()
    var messages = mutableStateOf(listOf<Message>())
    val currentFriend = mutableStateOf<User?>(null)

    fun loadMessages(friendId: String) {
        viewModelScope.launch {
            messages.value = messageDao.getMessagesByFriendId(friendId)
            val me = db.userDao().getUserById(myId)
            api.enterRoom(me.name)
            webSocketClient.connect { m, id ->
                viewModelScope.launch {
                    val message = Message(
                        content = m,
                        senderId = id,
                        receiverId = if (myId == id) friendId else myId,
                        status = MessageStatus.SENT,
                        date = LocalDateTime.now()
                    )
                    messageDao.insertMessage(
                        message
                    )
                    messages.value += message
                }
            }
        }
    }

    fun loadFriend(friendId: String) {
        viewModelScope.launch {
            currentFriend.value = db.userDao().getUserById(friendId)
        }
    }

    fun sendMessage(message: Message) {
//        messages.value += message
        viewModelScope.launch {
            messageDao.insertMessage(message)
            webSocketClient.sendMessage(message.content, myId)
        }
    }

    fun leaveChat() {
        webSocketClient.disconnect()
    }
}

val dummyUsers = listOf(
    User("someId", "Filip", "active"),
    User("anotherId", "Jan", "active")
)