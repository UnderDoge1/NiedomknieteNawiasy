package pl.niedomknietenawiasy.cosmicchat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.niedomknietenawiasy.cosmicchat.model.Api
import pl.niedomknietenawiasy.cosmicchat.model.AppDatabase
import pl.niedomknietenawiasy.cosmicchat.model.Message
import pl.niedomknietenawiasy.cosmicchat.model.User

class ChatViewModel(
    val db: AppDatabase
): ViewModel() {
    val api = Api()
    val toastMsg = "example toast msg"
    val userList = mutableStateOf(listOf<User>())
    //TODO: remove hardcoded id
    val myId = "1"

    init {
        viewModelScope.launch {
            userList.value = db.userDao().getUsers()
        }
    }

    val messageDao = db.messageDao()
    val messages = mutableStateOf(listOf<Message>())
    val currentFriend = mutableStateOf<User?>(null)

    fun loadMessages(friendId: String) {
        viewModelScope.launch {
            messages.value = messageDao.getMessagesBySender(friendId)
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