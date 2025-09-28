package org.example;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class Main {

    @Param({"100", "1000", "10000"})
    public int arraySize;

    private int[] randomArray;
    private int[] nearlySortedArray;
    private int[] reverseSortedArray;

    @Setup(Level.Trial)
    public void setUp() {
        randomArray = generateRandomArray(arraySize);
        nearlySortedArray = generateNearlySortedArray(arraySize);
        reverseSortedArray = generateReverseArray(arraySize);
    }

    // Insertion Sort benchmarks
    @Benchmark
    public int[] benchmarkInsertionSortRandom() {
        int[] arr = randomArray.clone();
        InsertionSort.sort(arr, arr.length);
        return arr;
    }

    @Benchmark
    public int[] benchmarkInsertionSortNearlySorted() {
        int[] arr = nearlySortedArray.clone();
        InsertionSort.sort(arr, arr.length);
        return arr;
    }

    @Benchmark
    public int[] benchmarkInsertionSortReverse() {
        int[] arr = reverseSortedArray.clone();
        InsertionSort.sort(arr, arr.length);
        return arr;
    }

    // Selection Sort benchmarks
    @Benchmark
    public int[] benchmarkSelectionSortRandom() {
        int[] arr = randomArray.clone();
        SelectionSort.sort(arr, arr.length);
        return arr;
    }

    @Benchmark
    public int[] benchmarkSelectionSortNearlySorted() {
        int[] arr = nearlySortedArray.clone();
        SelectionSort.sort(arr, arr.length);
        return arr;
    }

    @Benchmark
    public int[] benchmarkSelectionSortReverse() {
        int[] arr = reverseSortedArray.clone();
        SelectionSort.sort(arr, arr.length);
        return arr;
    }

    public static void main(String[] args) throws RunnerException {
        System.out.println("Quick Demo:");
        int[] demo = {64, 34, 25, 12, 22, 11, 90, 5, 77, 30};
        testSmallArray(demo);

        Options opt = new OptionsBuilder()
                .include(Main.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

    private static void testSmallArray(int[] originalArr) {
        int[] arr1 = originalArr.clone();
        int[] arr2 = originalArr.clone();

        System.out.print("Original:         ");
        printArray(originalArr);

        InsertionSort.sort(arr1, arr1.length);
        System.out.print("After Insertion Sort: ");
        printArray(arr1);

        SelectionSort.sort(arr2, arr2.length);
        System.out.print("After Selection Sort:  ");
        printArray(arr2);
    }

    private static void printArray(int[] arr) {
        for (int value : arr) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    private static int[] generateRandomArray(int size) {
        Random random = new Random(42);
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(10000);
        }
        return arr;
    }

    private static int[] generateNearlySortedArray(int size) {
        int[] arr = new int[size];
        Random random = new Random(42);

        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }

        int swaps = Math.max(1, size / 20);
        for (int i = 0; i < swaps; i++) {
            int idx1 = random.nextInt(size);
            int idx2 = random.nextInt(size);
            int temp = arr[idx1];
            arr[idx1] = arr[idx2];
            arr[idx2] = temp;
        }

        return arr;
    }

    private static int[] generateReverseArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = size - i;
        }
        return arr;
    }
}
