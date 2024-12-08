package pl.niedomknietenawiasy.cosmicchat.model

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class MessageRequest(val msg: String)
data class EnterRoomRequest(val username: String, val room: String)
data class RegisterResponse(
    val call_sign: String,
    val pos_x: Float,
    val pos_y: Float,
    val vel_x: Float,
    val vel_y: Float
)
data class MessageResponse(val msg: String)
data class StatusResponse(val msg: String)

// Retrofit API Interface
interface ChatApi {
    @POST("chat/text")
    fun sendMessage(@Body request: MessageRequest): Call<MessageResponse>

    @GET("/register")
    fun getUsers(): Call<List<RegisterResponse>>

    @POST("/register")
    fun updateUser(@Body request: RegisterResponse): Call<Unit>

    @GET("chat/status")
    fun getStatus(): Call<StatusResponse>
}

class ApiService {
    private val BASE_URL = "http://10.19.200.54:5000/"

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

    suspend fun getUsers(): List<RegisterResponse> {
        val users = api.getUsers()
        return users.await()
    }

    suspend fun updateUser(user: User) {
        val status = api.updateUser(
            RegisterResponse(
                user.name,
                0f,
                0f,
                1f,
                1f
            )
        )
        return status.await()
    }
}