package net.jaredible.code.mtmm;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MTMM {
	private static boolean INFO_DISPLAYED = false;
	public static byte[] reservedMemory = new byte[0xa00000];

	public int numThreads;
	public int numRows = 1000;
	public int numCols = 1000;
	public int[][] A = new int[numRows][numCols];
	public int[][] B = new int[numRows][numCols];
	public int[][] C = new int[numRows][numCols];

	public MTMM(int numThreads) {
		this.numThreads = numThreads;

		generateRandom();
		displayInfo();
	}

	public void cleanup() {
		numThreads = 0;
		numRows = 0;
		numCols = 0;
		A = null;
		B = null;
		C = null;
		// System.gc();
	}

	@SuppressWarnings("static-access")
	private static void displayInfo() {
		if (INFO_DISPLAYED) return;

		String info = "" + //
				"When you use multiple threads, you need to be aware of the overhead \n" + //
				"of using additional threads. You also need to determine if your \n" + //
				"algorithm has work which can be preformed in parallel or not. So you \n" + //
				"need to have work which can be run concurrently which is large enough \n" + //
				"that it will exceed the overhead of using multiple threads.";
		System.err.println(info + "\n");
		System.err.flush();

		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Processor identifier: " + System.getenv("PROCESSOR_IDENTIFIER"));
		System.out.println("Processor architecture: " + System.getenv("PROCESSOR_ARCHITECTURE"));
		System.out.println("Total processors (cores): " + System.getenv("NUMBER_OF_PROCESSORS"));
		System.out.println("Available processors (cores): " + Runtime.getRuntime().availableProcessors());
		System.out.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory());
		long maxMemory = Runtime.getRuntime().maxMemory();
		System.out.println("Maximum memory (bytes): " + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));
		System.out.println("Total memory available to JVM (bytes): " + Runtime.getRuntime().totalMemory());
		System.out.flush();

		try {
			Thread.currentThread().sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		INFO_DISPLAYED = true;
	}

	private void generateRandom() {
		Random r = new Random();
		int x, y;
		for (y = 0; y < numRows; y++)
			for (x = 0; x < numCols; x++) {
				A[y][x] = r.nextInt();
				B[y][x] = r.nextInt();
			}
	}

	@SuppressWarnings("static-access")
	public void calculate() {
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("\nStarting with " + numThreads + " thread(s).");
			System.out.flush();

			long startTime = System.currentTimeMillis();
			ExecutorService executor = Executors.newCachedThreadPool();
			for (int i = 0; i < numThreads; i++) {
				final int threadIndex = i;
				executor.submit(() -> multiply(threadIndex));
			}
			executor.shutdown();
			while (!executor.isTerminated()) {
			}
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

			long endTime = System.currentTimeMillis();
			long elapsedTime = endTime - startTime;
			System.out.println("Execution time was " + elapsedTime + "ms.\n");
			System.out.flush();
		} catch (Exception e) {
		}
	}

	private void multiply(int threadIndex) {
		int d = numRows / numThreads;
		int start = d * threadIndex;
		int end = start + d;
		int i, j, k;
		for (i = start; i < end; i++)
			for (j = 0; j < numCols; j++)
				for (k = 0; k < numRows; k++)
					C[i][j] += A[i][k] * B[k][j];
	}

	public void printMatrix() {
		int i, j;
		for (i = 0; i < numRows; i++) {
			for (j = 0; j < numCols; j++)
				System.out.print(C[i][j] + " ");
			System.out.println();
		}
	}
}
