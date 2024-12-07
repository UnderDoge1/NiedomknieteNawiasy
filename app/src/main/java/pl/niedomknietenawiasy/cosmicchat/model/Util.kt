package pl.niedomknietenawiasy.cosmicchat.model

import kotlin.math.roundToInt

fun getRandomString(): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    var string = ""
    for (i in (1..20)) {
        string += allowedChars[(Math.random() * (allowedChars.size - 1)).roundToInt()]
    }
    return string
}