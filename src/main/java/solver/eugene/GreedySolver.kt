package solver.eugene

import common.*
import kotlin.math.abs

data class CarInfo(
    val availableTime: Int = 0,
    val position: Point = Point(0,0),
    val bindRides: MutableList<Ride> = mutableListOf()
)

class GreedySolver(
    override val name: String = "eugene.GreedySolver"
) : Solver {

    private lateinit var gridSize: Size
    private var vehicleCount: Int = 0
    private lateinit var g_rides: MutableList<Ride>
    private var bonus: Int = 0
    private var timeLimit: Int = 0

    override fun solve(input: Input): Output {
        gridSize = input.gridSize
        vehicleCount = input.vehicles
        g_rides = input.rides.toMutableList() // mutable, we gonna remove during the way
        bonus = input.bonus
        timeLimit = input.timeLimit

        val carRideMap = hashMapOf<Int, CarInfo>()  // car index - [lastPosition, bindedRides]

        for (timeTick in 0..timeLimit) {
            for (carIndex in 0 until vehicleCount) {
                val carInfo = (carRideMap[carIndex] ?: CarInfo())
                val ride = findTheBestRideForCar(carInfo, timeTick) ?: break
                carRideMap[carIndex] = CarInfo(
                    availableTime = timeTick + ride.distance,
                    position = ride.end,
                    bindRides = carInfo.bindRides.also { it.add(ride) }
                )
            }
        }

        return Output(
            handledRides = carRideMap.map { (carIndex, carInfo) ->
                carInfo.bindRides.map { ride ->
                    HandledRide(
                        rideIndex = input.rides.indexOf(ride),
                        vehicleIndex = carIndex
                    )
                }
            }.flatten()
        )
    }

    private fun findTheBestRideForCar(carInfo: CarInfo, timeTick: Int): Ride? {
        val it = g_rides.iterator()
        var bestRide: Pair<Ride, Long>? = null // Ride - Score
        while (it.hasNext()) {
            val rideCandidate = it.next()
            val score = calculateScoreForRideCandidate(carInfo, rideCandidate, timeTick)

            if (bestRide == null) {
                bestRide = rideCandidate to score
            } else {
                if (bestRide.second < score) {
                    bestRide = rideCandidate to score
                }
            }
        }

        return when {
            bestRide == null && g_rides.isEmpty() -> {
                null
            }
            bestRide == null -> {
                g_rides.removeAt(0)
                g_rides.firstOrNull()
            }
            else -> {
                g_rides.remove(bestRide.first)
                bestRide.first
            }
        }
    }

    private fun calculateScoreForRideCandidate(carInfo: CarInfo, rideCandidate: Ride, timeTick: Int): Long {
        val (carAvailabilityTime, carPos, _) = carInfo
        val (rideStartPos, rideEndPos, rideTimeStart, rideTimeEnd) = rideCandidate

        if (carAvailabilityTime < timeTick) return 0

        val distanceToPickUpPlaceScore: Long =
            (abs(rideStartPos.x - carPos.x) + abs(rideStartPos.y - carPos.y)).toLong()
        val tripDistance: Long =
            (abs(rideEndPos.x - rideStartPos.x) + abs(rideEndPos.y - rideStartPos.y)).toLong()

        if (timeTick + distanceToPickUpPlaceScore + tripDistance > rideTimeEnd) return 0 // we can't finish trip after TIME
        val waitingTime = rideTimeStart - timeTick + distanceToPickUpPlaceScore

        val score = 0 - distanceToPickUpPlaceScore - waitingTime
        return score
    }
}