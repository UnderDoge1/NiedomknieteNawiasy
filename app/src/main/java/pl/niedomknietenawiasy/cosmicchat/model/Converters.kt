package pl.niedomknietenawiasy.cosmicchat.model

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.*
import java.util.UUID

class Converters {
    private val formatter = ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromUUID(uuid: UUID): String = uuid.toString()

    @TypeConverter
    fun toUUID(uuid: String): UUID = UUID.fromString(uuid)

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime): String = dateTime.format(formatter)

    @TypeConverter
    fun toLocalDateTime(dateTime: String): LocalDateTime = LocalDateTime.parse(dateTime, formatter)
}