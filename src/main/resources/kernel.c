__kernel void solverKernel(
    __global const int *commonParams,
    __global const int *rides,
    __global const float *greedyParams,
    __global int *resultScores
) {
   int gid = get_global_id(0);

   int width = commonParams[0];
   int height = commonParams[1];
   int vehicles = commonParams[2];
   int ridesCount = commonParams[3];
   int bonus = commonParams[4];
   int timeLimit = commonParams[5];

   float a = greedyParams[gid * 3];
   float b = greedyParams[gid * 3 + 1];
   float c = greedyParams[gid * 3 + 2];

   int tick = 0;
   __private int carTakenUntil[vehicles];
   for (int i = 0; i < vehicles; i++) carTakenUntil[i] = 0;

   __private int carPositionX[vehicles];
   for (int i = 0; i < vehicles; i++) carPositionX[i] = 0;

   __private int carPositionY[vehicles];
   for (int i = 0; i < vehicles; i++) carPositionY[i] = 0;

   __private int handledRidesPosition = 0;

   __private int handledRidesResult[ridesCount];
   for (int i = 0; i < vehicles; i++) carPositionY[i] = 0;

   __private int visitedRides[ridesCount];

   while (tick < timeLimit) {

        tick++;
   }

   int rideIndex = 0;
   int startX = rides[rideIndex * 6];
   int startY = rides[rideIndex * 6 + 1];
   int finishX = rides[rideIndex * 6 + 2];
   int finishY = rides[rideIndex * 6 + 3];
   int startTime = rides[rideIndex * 6 + 4];
   int endTime = rides[rideIndex * 6 + 5];



   /**
            var tick = 0
           val carTakenUntil = IntArray(input.vehicles)
           val carPositions = Array(input.vehicles) { Point(0, 0) }
           val sortedRides = input.rides.mapIndexed { index, ride -> index to ride }.toMutableList()

           val handledRides = mutableListOf<HandledRide>()
           while (tick < input.timeLimit) {
               carTakenUntil.forEachIndexed { index, time ->
                   if (time > tick) return@forEachIndexed
                   val carPosition = carPositions[index]

                   val nextRide = sortedRides.maxBy { (index, ride) ->
                       val canStartAt = tick + carPosition.distanceTo(ride.start)
                       val canFinishAt = canStartAt + ride.distance

                       if (canFinishAt > ride.endTime) return@maxBy 0f

                       val bonus = if (canStartAt <= ride.startTime) input.bonus else 0
                       val actualStart = max(canStartAt, ride.startTime)
                       val cost = ride.distance + bonus
                       val distanceToStart = carPosition.distanceTo(ride.start)
                       val waitingTime = actualStart - canStartAt
                       val score = cost * params[0] - distanceToStart * params[1] - waitingTime * params[2]
                       score
                   } ?: return@forEachIndexed

                   val (rideIndex, ride) = nextRide
                   sortedRides.remove(nextRide)
                   carTakenUntil[index] = max(tick + carPosition.distanceTo(ride.start), nextRide.second.startTime) + ride.distance
                   handledRides.add(
                       HandledRide(rideIndex, index)
                   )
                   carPositions[index] = ride.end
               }
               tick++
           }
           return Output(handledRides)
   **/

   c[gid] = a[gid] * b[gid];
}