package org.example;

public class Main {
    public static void main(String[] args) {
        int[] originalArr = new int[]{64, 34, 25, 12, 22, 11, 90, 5, 77, 30};

        int[] arr1 = originalArr.clone();
        int[] arr2 = originalArr.clone();

        System.out.print("Original: ");
        for (int i = 0; i < originalArr.length; i++) {
            System.out.print(originalArr[i] + " ");
        }
        System.out.println();

        InsertionSort.sort(arr1, arr1.length - 1);
        System.out.print("After InsertionSort: ");
        for (int i = 0; i < arr1.length; i++) {
            System.out.print(arr1[i] + " ");
        }
        System.out.println();

        SelectionSort.sort(arr2, arr2.length - 1);
        System.out.print("After SelectionSort: ");
        for (int i = 0; i < arr2.length; i++) {
            System.out.print(arr2[i] + " ");
        }
        System.out.println();
    }
}
