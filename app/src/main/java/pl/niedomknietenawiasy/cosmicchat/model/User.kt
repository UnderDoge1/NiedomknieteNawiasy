package pl.niedomknietenawiasy.cosmicchat.model

import java.util.UUID

data class User(val id: UUID = UUID.randomUUID(), val name: String, val status: String)
