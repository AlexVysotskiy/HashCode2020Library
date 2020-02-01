package common.helpers.sort;

public class QuickSort {

    private static void swap(short[] arrayToSort, float[] arrayToCompare, short i, short j) {
        short t = arrayToSort[i];
        arrayToSort[i] = arrayToSort[j];
        arrayToSort[j] = t;

        float f = arrayToCompare[i];
        arrayToCompare[i] = arrayToCompare[j];
        arrayToCompare[j] = f;
    }

    // first <= second
    private static boolean isLessThanOrEqual(float first, float second) {
        final float firstTrimmed = first - (int) first;
        final float secondTrimmed = second - (int) second;
        return firstTrimmed <= secondTrimmed;
    }

    private static short partition(
            short[] arrayToSort,
            float[] arrayToCompare,
            short l,
            short h
    ) {
        float x = arrayToCompare[h];
        short i = (short) (l - 1);

        for (short j = l; j <= h - 1; j++) {
            if (isLessThanOrEqual(arrayToCompare[j], x)) {
                i++;
                swap(arrayToSort, arrayToCompare, i, j);
            }
        }
        swap(arrayToSort, arrayToCompare, (short) (i + 1), h);
        return (short) (i + 1);
    }


    public static void quickSortArrayBasedOnSecond(
            short[] arrayToSort,
            float[] arrayToCompare
    ) {
        quickSortArrayBasedOnSecond(arrayToSort, arrayToCompare, (short) 0, (short) (arrayToSort.length - 1));
    }

    private static void quickSortArrayBasedOnSecond(
            short[] arrayToSort,
            float[] arrayToCompare,
            short l,
            short h
    ) {
        final short[] stack = new short[h - l + 1];

        short top = -1;

        stack[++top] = l;
        stack[++top] = h;

        while (top >= 0) {
            h = stack[top--];
            l = stack[top--];

            short p = partition(arrayToSort, arrayToCompare, l, h);

            if (p - 1 > l) {
                stack[++top] = l;
                stack[++top] = (short) (p - 1);
            }

            if (p + 1 < h) {
                stack[++top] = (short) (p + 1);
                stack[++top] = h;
            }
        }
    }
}
