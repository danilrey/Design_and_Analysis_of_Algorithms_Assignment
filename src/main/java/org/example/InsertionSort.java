package org.example;

public class InsertionSort {

    public static void sort(int[] arr, int length) {
        for (int gap = length/2; gap > 0; gap /= 2) {

            for (int i = gap; i < length; i++) {
                int temp = arr[i];
                int j = i;

                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }

                arr[j] = temp;
            }
        }
    }
}
