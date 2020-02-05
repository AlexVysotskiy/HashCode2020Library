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

   __private int carPositionsX[vehicles];
   for (int i = 0; i < vehicles; i++) carPositionsX[i] = 0;

   __private int carPositionsY[vehicles];
   for (int i = 0; i < vehicles; i++) carPositionsY[i] = 0;

   __private int handledRidesPosition = 0;
   __private int handledRidesResult[ridesCount * 2];

   __private int removedRides[ridesCount];
   for (int i = 0; i < ridesCount; i++) removedRides[i] = 0;

   while (tick < timeLimit) {
        for (int index = 0; index < vehicles; index++) {
            int time = carTakenUntil[index];
            if (time > tick) break;

            int carPositionX = carPositionsX[index];
            int carPositionY = carPositionsY[index];

            int maxScore = 0;
            int maxRideIndex = -1;

            for (int i = 0; i < ridesCount; i++) {
               if (removedRides[i] == 1) continue;

               int startX = rides[i * 6];
               int startY = rides[i * 6 + 1];
               int finishX = rides[i * 6 + 2];
               int finishY = rides[i * 6 + 3];
               int startTime = rides[i * 6 + 4];
               int endTime = rides[i * 6 + 5];

               int rideDistance = abs(startX - finishX) + abs(startY - finishY);

               int distanceFromCarToRideStart = abs(carPositionX - startX) + abs(carPositionY - startY);
               int canStartAt = tick + distanceFromCarToRideStart;

               float score = 0;
               if (canFinishAt <= endTime) {
                    int rideBonus = 0;
                    if (canStartAt <= startTime) rideBonus = bonus;
                    int actualStart = max(canStartAt, startTime);
                    int cost = rideDistance + bonus;
                    int waitingTime = actualStart - canStartAt;
                    score = (float) cost * a - (float) distanceFromCarToRideStart * b - (float) waitingTime * c;
               }

               if (maxRideIndex == -1 || score > maxScore) {
                    maxScore = score;
                    maxRideIndex = i;
               }
            }

            if (maxRideIndex == -1) {
                // no rides left
                break;
            }

            int startX = rides[maxRideIndex * 6];
            int startY = rides[maxRideIndex * 6 + 1];
            int finishX = rides[maxRideIndex * 6 + 2];
            int finishY = rides[maxRideIndex * 6 + 3];
            int startTime = rides[maxRideIndex * 6 + 4];
            int rideDistance = abs(startX - finishX) + abs(startY - finishY);
            int distanceFromCarToRideStart = abs(carPositionX - startX) + abs(carPositionY - startY);

            removedRides[maxRideIndex] = 1;
            carTakenUntil[index] = max(tick + distanceFromCarToRideStart, startTime) + rideDistance;
            carPositionsX[index] = finishX;
            carPositionsY[index] = finishY;

            handledRidesResult[handledRidesPosition * 2] = maxRideIndex;
            handledRidesResult[handledRidesPosition * 2 + 1] = index;
            handledRidesPosition++;
        }
        tick++;
   }

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