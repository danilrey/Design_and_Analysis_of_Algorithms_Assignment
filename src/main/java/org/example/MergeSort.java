package org.example;

public class MergeSort {

    private static final int CUTOFF = 16;
    //allocate reusable buffer once
    public static void mergeSort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        int[] buffer = new int[arr.length];
        sort(arr, buffer, 0, arr.length - 1);
    }

    private static void sort(int[] arr, int[] buffer, int left, int right) {
        if (left >= right) return;
        int length = right - left + 1;
        int mid = left + (right - left) / 2;
        if (length <= CUTOFF) {
            insertionSort(arr, left, right);
            return;
        }
        sort(arr, buffer, left, mid);
        sort(arr, buffer, mid + 1, right);
        merge(arr, buffer, left, mid, right);
    }

    // Merge two sorted parts
    private static void merge(int[] arr, int[] buffer, int left, int mid, int right) {
        for (int k = left; k <= right; k++) {
            buffer[k] = arr[k];
        }
        int i = left;
        int l = left;
        int r = mid + 1;
        while (l <= mid && r <= right) {
            if (buffer[l] <= buffer[r]) {
                arr[i++] = buffer[l++];
            } else {
                arr[i++] = buffer[r++];
            }
        }
        while (l <= mid) {
            arr[i++] = buffer[l++];
        }
    }

    private static void insertionSort(int[] arr, int l, int r) {
        for (int i = l + 1; i <= r;i++) {
            int temp = arr[i];
            int j = i-1;

            while (j >= l && arr[j] > temp ) {
                arr[j+1] = arr[j];
                j--;
            }
            arr[j+1] = temp;
        }
    }
}