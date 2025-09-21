package org.example;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class Main {
    private static final int DEMO_SIZE = 10;

    @Param({"100", "1000", "10000"})
    private int arraySize;

    private int[] baseArray;
    private Point[] basePoints;

    @Setup(Level.Trial)
    public void setUp() {
        Random rnd = new Random();
        baseArray = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            baseArray[i] = rnd.nextInt(arraySize) + 1;
        }
        basePoints = new Point[arraySize];
        for (int i = 0; i < arraySize; i++) {
            basePoints[i] = new Point(rnd.nextInt(arraySize) + 1, rnd.nextInt(arraySize) + 1);
        }
    }

    @Benchmark
    public void benchmarkMergeSort() {
        int[] arr = baseArray.clone();
        MergeSort.mergeSort(arr);
    }

    @Benchmark
    public void benchmarkQuickSort() {
        int[] arr = baseArray.clone();
        QuickSort.sort(arr, 0, arr.length - 1);
    }

    @Benchmark
    public int benchmarkDeterministicSelect() {
        int[] arr = baseArray.clone();
        return DeterministicSelect.select(arr, 3); // Select 25th percentile
    }

    @Benchmark
    public double benchmarkClosestPair() {
        Point[] points = new Point[basePoints.length];
        System.arraycopy(basePoints, 0, points, 0, basePoints.length);
        return ClosestPair.closest(points, points.length);
    }

    private static int[] generateDemoBase() {
        int[] a = new int[DEMO_SIZE];
        Random rnd = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = rnd.nextInt(DEMO_SIZE) + 1;
        }

        System.out.print("Base: ");
        for (int v : a) System.out.print(v + " ");
        System.out.println();
        return a;
    }

    private static Point[] generateDemoBasePoints() {
        Point[] a = new Point[DEMO_SIZE];
        Random rnd = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = new Point(rnd.nextInt(DEMO_SIZE) + 1, rnd.nextInt(DEMO_SIZE) + 1);
        }
        return a;
    }

    private static void runAlgorithmDemo() {
        System.out.println("___ Algorithm Demo and Correctness Verification ___");
        System.out.println("Array size: " + DEMO_SIZE);
        System.out.println();

        int[] BASE = generateDemoBase();

        // Demo runs showing actual results
        System.out.println("___ Demo Results ___");

        // MergeSort demo
        int[] mergeDemo = BASE.clone();
        System.out.print("Original: ");
        for (int v : mergeDemo) System.out.print(v + " ");
        System.out.println();

        MergeSort.mergeSort(mergeDemo);
        System.out.print("MergeSort result: ");
        for (int v : mergeDemo) System.out.print(v + " ");
        System.out.println();
        System.out.println();

        // QuickSort demo
        int[] quickDemo = BASE.clone();
        QuickSort.sort(quickDemo, 0, quickDemo.length - 1);
        System.out.print("QuickSort result: ");
        for (int v : quickDemo) System.out.print(v + " ");
        System.out.println();
        System.out.println();

        // DeterministicSelect demo
        int[] selectDemo = BASE.clone();
        int selected = DeterministicSelect.select(selectDemo, 3);
        System.out.print("Array after select: ");
        for (int v : selectDemo) System.out.print(v + " ");
        System.out.println();
        System.out.println("4th smallest element (index 3): " + selected);
        System.out.println();

        // ClosestPair demo
        Point[] pointsDemo = generateDemoBasePoints();
        System.out.print("Points: ");
        for (Point p : pointsDemo) System.out.print(p + " ");
        System.out.println();
        double closestDistance = ClosestPair.closest(pointsDemo, DEMO_SIZE);
        System.out.println("Closest pair distance: " + String.format("%.4f", closestDistance));
        System.out.println();
    }

    private static void runJMHBenchmarks() throws RunnerException {
        System.out.println("___ JMH Performance Benchmarks ___");
        System.out.println("Running professional benchmarks on array sizes: 100, 1000, 10000");
        System.out.println("This may take several minutes...");
        System.out.println();

        Options opt = new OptionsBuilder()
                .include(Main.class.getSimpleName())
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .build();

        new Runner(opt).run();
    }

    public static void main(String[] args) throws RunnerException {
        if (args.length > 0 && args[0].equals("benchmark")) {
            // Run only JMH benchmarks
            runJMHBenchmarks();
        } else {
            // Run demo first
            runAlgorithmDemo();

            System.out.println("___ Performance Benchmarking ___");
            System.out.println("To run JMH benchmarks: java -jar target/benchmarks.jar\n");
        }
    }
}