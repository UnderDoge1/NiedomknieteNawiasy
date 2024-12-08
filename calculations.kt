import kotlin.math.sqrt

const val c = 299792458.0


data class Vector3d(val x: Double, val y: Double, val z: Double) {
  operator fun plus(other: Vector3d): Vector3d =
      Vector3d(x + other.x, y + other.y, z + other.z)

  operator fun minus(other: Vector3d): Vector3d =
      Vector3d(x - other.x, y - other.y, z - other.z)

  operator fun times(scalar: Double): Vector3d =
      Vector3d(x * scalar, y * scalar, z * scalar)

  operator fun div(scalar: Double): Vector3d =
      Vector3d(x / scalar, y / scalar, z / scalar)

  fun norm(): Double = sqrt(x * x + y * y + z * z)

  fun normalized(): Vector3d {
      val norm = norm()
      if( norm == 0.0 ) return this

      return this / norm
  } 

}

fun solveQuadratic(a: Double, b: Double, c: Double): Pair<Double, Double>? {
    val discriminant = b * b - 4 * a * c
    if (discriminant < 0) return null // Brak rozwiązań rzeczywistych
    val sqrtDiscriminant = sqrt(discriminant)
    val x1 = (-b - sqrtDiscriminant) / (2 * a)
    val x2 = (-b + sqrtDiscriminant) / (2 * a)
    return Pair(x1, x2)
}

fun calculateCurrentSenderPosition(
    senderRegisterdPosition: Vector3d,
    senderRegisterdVelocity: Vector3d,
    senderReportTime: Double,
    timeNow: Double,
){
    Dt = timeNow - senderReportTime
    return senderRegisterdPosition + senderRegisterdVelocity * Dt
}
fun calculateInterceptTime(
    senderPos: Vector3d,    // znana
    senderVel: Vector3d,    // znana
    receiverPos: Vector3d,  // w momencie wysłania sygnału
    receiverVel: Vector3d   // z rejestru
): Double? {
    val d0 = receiverPos - senderPos
    val vRel = receiverVel - senderVel

    val a = vRel.norm() * vRel.norm() - c * c
    val b = 2 * (d0.x * vRel.x + d0.y * vRel.y + d0.z * vRel.z)
    val cTerm = d0.norm() * d0.norm()

    return solveQuadratic(a, b, cTerm)?.let { (t1, t2) ->
        listOf(t1, t2).filter { it > 0 }.minOrNull() // Wybierz > 0
    }
}

fun calculateInterceptPoint(
    val intercept_time : Double,
    val receiverPos: Vector3d,
    val receiverVel: Vector3d
){
  return Vector3d = receiverPos + receiverVel * intercept_time
}


fun calculateTimes(
    distance: Double,                         // odległość pomiędzy nadawcą a PUNKTEM PRZECIĘCIA
    relativeVelocity: Double,                 // prędkość względna pomiędzy nadawcą a odbiorcą
    relativeVelocityReceiverObserver: Double  // prędkośc względna pomiędzy obserwatorem a odbiorcą -- odczytana z rejestru latarni
  ): Triple<Double, Double, Double> {

  val signalTravelTime = distance / c

  // czas z perspektywy nadawcy
  val senderTime = distance / c

  // wsp. dylatacji odbiorcy
  val timeDilationFactorReceiver = 1 / sqrt(1 - (relativeVelocity * relativeVelocity / (c * c)))

  // wsp. dylatacji obserwatora
  val timeDilationFactorObserver = 1 / sqrt(1 - (relativeVelocityReceiverObserver * relativeVelocityReceiverObserver / (c * c)))

  // czas z perspektywy odbiorcy
  val receiverTime = signalTravelTime * timeDilationFactorReceiver

  // czas z perspetywy obserwatora (latarni)
  val observerTime = signalTravelTime * timeDilationFactorObserver

  return Triple(senderTime, receiverTime, observerTime)
}