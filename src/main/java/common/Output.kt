package common

data class Output(
    val handledRides: List<HandledRide>
)

data class HandledRide(
    val rideIndex: Int,
    val vehicleIndex: Int
)
