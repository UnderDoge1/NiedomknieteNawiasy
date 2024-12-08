
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SocketIOClient() {
    private lateinit var socket: Socket

    fun connect(
        onMessage: (message: String, senderId: String) -> Unit,
    ) {
        try {
            socket = IO.socket("http://10.19.200.54:5000/chat")
            socket.on(Socket.EVENT_CONNECT) {
                println("Connected to server")
                socket.emit("join")
            }.on("status") { args ->
                val data = args[0] as JSONObject
                println("Status: ${data.getString("msg")}")
            }.on("message") { args ->
                val data = args[0] as JSONObject
                println("Message: ${data.getString("msg")}")
                onMessage(data.getString("msg"), data.getString("senderId"))
            }
            socket.connect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendMessage(message: String, senderId: String) {
        val jsonMessage = JSONObject()
        jsonMessage.put("msg", message)
        jsonMessage.put("senderId", senderId)
        socket.emit("text", jsonMessage)
//        socket.emit("message", JSONObject().put("msg", message))
    }

    fun disconnect() {
        socket.disconnect()
    }
}