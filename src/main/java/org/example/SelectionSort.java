package org.example;

public class SelectionSort {

    public static void sort(int[] arr,int length) {
        for (int i = 0; i < length; i++) {
            int min_ind = i;
            for (int j = i+1; j < length; j++) {
                if (arr[j] < arr[min_ind]) {
                    min_ind = j;
                }
            }

            int temp = arr[i];
            arr[i] = arr[min_ind];
            arr[min_ind] = temp;
        }
    }
}
