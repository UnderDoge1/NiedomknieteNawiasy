package pl.niedomknietenawiasy.cosmicchat.model

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class MessageRequest(val msg: String)
data class EnterRoomRequest(val username: String, val room: String)
data class MessageResponse(val msg: String)
data class StatusResponse(val msg: String)

// Retrofit API Interface
interface ChatApi {
    @POST("chat/text")
    fun sendMessage(@Body request: MessageRequest): Call<MessageResponse>

    @POST("chat")
    fun enterRoom(@Body request: EnterRoomRequest): Call<MessageResponse>

    @GET("chat/status")
    fun getStatus(): Call<StatusResponse>
}

class ApiService {
    private val BASE_URL = "http://10.19.200.25:5000/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val api: ChatApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatApi::class.java)
    }

    fun sendMessage(message: Message) {
        api.sendMessage(MessageRequest(message.content))
    }

    fun enterRoom(username: String, room: String = "1") {
        api.enterRoom(EnterRoomRequest(username, room))
    }
}