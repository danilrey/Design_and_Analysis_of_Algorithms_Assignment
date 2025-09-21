package org.example;

import java.util.Random;

public class QuickSort {

    private static void random(int[] arr, int low, int high) {
        Random rnd = new Random();
        int pivot = rnd.nextInt(high-low + 1) + low;
        int temp = arr[pivot];
        arr[pivot] = arr[high];
        arr[high] = temp;
    }

    private static int partition(int[] arr, int low, int high) {

        random(arr, low, high);
        int pivot = arr[high];

        int i = low-1;

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i+1];
        arr[i+1] = arr[high];
        arr[high] = temp;
        return i+1;
    }

    static void sort(int[] arr, int low, int high) {
        while (low < high) {
            int p = partition(arr, low, high);
            if (p - low < high - p) {
                sort(arr, low, p - 1);
                low = p + 1;
            } else {
                sort(arr, p + 1, high);
                high = p - 1;
            }
        }
    }

}