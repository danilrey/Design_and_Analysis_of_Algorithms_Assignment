package org.example;

public class DeterministicSelect {

    public static int select(int[] arr, int k) {
        if (arr == null) return -1;
        if (k < 0 || k >= arr.length) return -1;
        return select(arr, 0, arr.length - 1, k);
    }

    private static int select(int[] a, int left, int right, int k) {
        while (true) {
            if (left == right) return a[left];

            int pivotIndex = medianOfMedians(a, left, right);
            pivotIndex = partition(a, left, right, pivotIndex);

            int leftSize = pivotIndex - left;
            if (k == leftSize) return a[pivotIndex];

            if (k < leftSize) {
                int rightSideSize = right - pivotIndex;
                if (leftSize <= rightSideSize) {
                    return select(a, left, pivotIndex - 1, k);
                } else {
                    right = pivotIndex - 1;
                }
            } else {
                int rightSideSize = right - pivotIndex;
                int newK = k - leftSize - 1;
                if (rightSideSize <= leftSize) {
                    return select(a, pivotIndex + 1, right, newK);
                } else {
                    left = pivotIndex + 1;
                    k = newK;
                }
            }
        }
    }

    private static int medianOfMedians(int[] a, int left, int right) {
        int n = right - left + 1;
        if (n <= 5) {
            insertionSort(a, left, right);
            return left + n / 2;
        }

        int dst = left;
        for (int i = left; i <= right; i += 5) {
            int subRight = Math.min(i + 4, right);
            insertionSort(a, i, subRight);
            int medianIdx = i + (subRight - i) / 2;
            swap(a, dst++, medianIdx);
        }
        int medLeft = left;
        int medRight = dst - 1;
        return medianOfMedians(a, medLeft, medRight);
    }

    private static int partition(int[] a, int left, int right, int pivotIndex) {
        int pivot = a[pivotIndex];
        swap(a, pivotIndex, right);
        int store = left;
        for (int i = left; i < right; i++) {
            if (a[i] < pivot) {
                swap(a, store++, i);
            }
        }
        swap(a, store, right);
        return store;
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

    private static void swap(int[] a, int i, int j) {
        if (i != j) {
            int t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
    }
}