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
   __private int carTakenUntil[$vehicle_count$];
   for (int i = 0; i < vehicles; i++) carTakenUntil[i] = 0;

   __private int carPositionsX[$vehicle_count$];
   for (int i = 0; i < vehicles; i++) carPositionsX[i] = 0;

   __private int carPositionsY[$vehicle_count$];
   for (int i = 0; i < vehicles; i++) carPositionsY[i] = 0;

   __private int handledRidesPosition = 0;
   __private int handledRidesResult[$rides_count$ * 2];

   __private int removedRides[$rides_count$];
   for (int i = 0; i < ridesCount; i++) removedRides[i] = 0;

   while (tick < timeLimit) {
        for (int index = 0; index < vehicles; index++) {
            int time = carTakenUntil[index];
            if (time > tick) break;

            int carPositionX = carPositionsX[index];
            int carPositionY = carPositionsY[index];

            float maxScore = 0;
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
               int canFinishAt = canStartAt + rideDistance;

               float score = 0;
               if (canFinishAt <= endTime) {
                    int rideBonus = 0;
                    if (canStartAt <= startTime) rideBonus = bonus;
                    int actualStart = max(canStartAt, startTime);
                    int cost = rideDistance + rideBonus;
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

   for (int i = 0; i < vehicles; i++) carPositionsX[i] = 0;
   for (int i = 0; i < vehicles; i++) carPositionsY[i] = 0;
   for (int i = 0; i < vehicles; i++) carTakenUntil[i] = 0; // car time

   int totalScore = 0;
   for (int i = 0; i < handledRidesPosition; i++) {
        int rideIndex = handledRidesResult[i * 2];
        int carIndex = handledRidesResult[i * 2 + 1];

        int carPositionX = carPositionsX[carIndex];
        int carPositionY = carPositionsY[carIndex];

        int startX = rides[rideIndex * 6];
        int startY = rides[rideIndex * 6 + 1];
        int finishX = rides[rideIndex * 6 + 2];
        int finishY = rides[rideIndex * 6 + 3];
        int startTime = rides[rideIndex * 6 + 4];
        int endTime = rides[rideIndex * 6 + 5];
        int rideDistance = abs(startX - finishX) + abs(startY - finishY);

        int spentTimeMovingToStart = abs(carPositionX - startX) + abs(carPositionY - startY);
        carTakenUntil[carIndex] += spentTimeMovingToStart;

        if (carTakenUntil[carIndex] < startTime) {
            // we need to wait for the start time
            carTakenUntil[carIndex] = startTime;
        }

        int startArrivalTime = carTakenUntil[carIndex];
        int spentTimeMovingToFinish = rideDistance;
        carTakenUntil[carIndex] += spentTimeMovingToFinish;
        int finishArrivalTime = carTakenUntil[carIndex];

        carPositionsX[carIndex] = finishX;
        carPositionsY[carIndex] = finishY;

        if (finishArrivalTime <= endTime) {
            // arrived on time, woo hoo
            totalScore += rideDistance;

            if (startArrivalTime == startTime) {
                totalScore += bonus;
            }
        }
   }

   resultScores[gid] = totalScore;
}
