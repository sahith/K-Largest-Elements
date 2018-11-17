/**
 * @author Sahith Reddy - sxa180065
 * @author Sakshi Gupta - 
 */

package sxa180065;

import java.util.PriorityQueue;
import java.util.Random;

public class SP11 {
	public static Random random = new Random();
	public static int numTrials = 1;

	public static void main(String[] args) {
		int n = 16000;
		int kLargest = n / 2; // k indicates to find max k elements in a array
		int choice = 2;
		if (args.length > 0) {
			n = Integer.parseInt(args[0]);
		}
		if (args.length > 1) {
			choice = Integer.parseInt(args[1]);
		}
		int[] arr = new int[n];
		for (int i = 0; i < n; i++) {
			arr[i] = i;
		}
		Timer timer = new Timer();
		switch (choice) {
		case 1:
			for (int i = 0; i < numTrials; i++) {
				Shuffle.shuffle(arr);
				select(arr, kLargest);
			}
			break;
		case 2:
			for (int i = 0; i < numTrials; i++) {
				Shuffle.shuffle(arr);
				selectPQ(arr, kLargest);
			}
			break;
		}
		timer.end();
		timer.scale(numTrials);
		System.out.println("Choice: " + choice + "\n" + timer);
	}

	/**
	 * Runs insertion sort on the given array arr from p to r
	 * 
	 * @param arr int[]
	 * @param p   int
	 * @param r   int
	 */
	public static void insertionSort(int[] arr, int p, int r) {
		for (int i = p + 1; i <= r; i++) {
			int temp = arr[i];
			int j = i - 1;
			while (j >= p && temp < arr[j]) {
				arr[j + 1] = arr[j];
				j = j - 1;
			}
			arr[j + 1] = temp;
		}
	}

	/**
	 * runs insertion sort on the given array
	 * 
	 * @param arr int[]
	 */
	public static void insertionSort(int[] arr) {
		insertionSort(arr, 0, arr.length - 1);
	}

	/**
	 * Generates random index between p and r (both inclusive) and swap the value
	 * with end element
	 */
	public static int randomizedPartition(int[] arr, int p, int r) {
		int i = random.nextInt(r - p + 1) + p; // Generating random number i (p <= i <= r)
		Shuffle.swap(arr, i, r);
		i = p - 1;
		int x = arr[r]; // Selecting pivot element as End element of the array
		for (int j = p; j <= r - 1; j++) // Iterating through p to r to separate out elements < arr[r] and >= arr[r]
		{
			if (arr[j] <= x) {
				i = i + 1;
				Shuffle.swap(arr, i, j);
			}
		}
		Shuffle.swap(arr, i + 1, r); // Moving pivot back to middle
		return i + 1;
	}

	/**
	 * Select returns the index such that the last k elements of the array will be
	 * max k elements
	 */
	public static int select(int[] arr, int p, int n, int k) {
		if (n < 7) // If length of the array is less than a threshold, then Insertion sort is
		           // called for optimization
		{
			insertionSort(arr, p, p + n - 1);
			return n - k;
		} else // else partition of array
		{
			int q = randomizedPartition(arr, p, p + n - 1);
			int left = q - p;
			int right = n - left - 1;
			if (right >= k) // if right side of the partition is greater then move the pivot to the right */
			{
				return select(arr, q + 1, right, k);
			} else if (right + 1 == k) // If right part of the partition is equal to k then return the pivot position
			{
				return q;
			} else {
				return select(arr, p, left, k - right - 1);
			}
		}
	}

	/**
	
	 */
	public static void select(int[] arr, int k) {
		select(arr, 0, arr.length, k);
	}

	/**
	 * Find max k elements using Priority Queue method
	 * 
	 * @return
	 */
	public static PriorityQueue<Integer> selectPQ(int[] arr, int k) {
		PriorityQueue<Integer> pq = new PriorityQueue<>(k); // Initializing a priority queue with size k
		for (int i = 0; i < k; i++) // Adding first k elements of the array to priority queue
		{
			pq.add(arr[i]);
		}
		int len = arr.length;
		for (int i = k; i < len; i++) {
			int head = pq.peek();
			if (arr[i] > head) {
				pq.remove();
				pq.add(arr[i]);
			}
		}
		return pq;
	}

	/**
	 * Timer class for roughly calculating running time of programs
	 * 
	 * @author rbk Usage: Timer timer = new Timer(); timer.start(); timer.end();
	 *         System.out.println(timer); // output statistics
	 */

	public static class Timer {
		long startTime, endTime, elapsedTime, memAvailable, memUsed;
		boolean ready;

		public Timer() {
			startTime = System.currentTimeMillis();
			ready = false;
		}

		public void start() {
			startTime = System.currentTimeMillis();
			ready = false;
		}

		public Timer end() {
			endTime = System.currentTimeMillis();
			elapsedTime = endTime - startTime;
			memAvailable = Runtime.getRuntime().totalMemory();
			memUsed = memAvailable - Runtime.getRuntime().freeMemory();
			ready = true;
			return this;
		}

		public long duration() {
			if (!ready) {
				end();
			}
			return elapsedTime;
		}

		public long memory() {
			if (!ready) {
				end();
			}
			return memUsed;
		}

		public void scale(int num) {
			elapsedTime /= num;
		}

		public String toString() {
			if (!ready) {
				end();
			}
			return "Time: " + elapsedTime + " msec.\n" + "Memory: " + (memUsed / 1048576) + " MB / "
			    + (memAvailable / 1048576) + " MB.";
		}
	}

	/**
	 * @author rbk : based on algorithm described in a book
	 */

	/* Shuffle the elements of an array arr[from..to] randomly */
	public static class Shuffle {

		public static void shuffle(int[] arr) {
			shuffle(arr, 0, arr.length - 1);
		}

		public static <T> void shuffle(T[] arr) {
			shuffle(arr, 0, arr.length - 1);
		}

		public static void shuffle(int[] arr, int from, int to) {
			int n = to - from + 1;
			for (int i = 1; i < n; i++) {
				int j = random.nextInt(i);
				swap(arr, i + from, j + from);
			}
		}

		public static <T> void shuffle(T[] arr, int from, int to) {
			int n = to - from + 1;
			Random random = new Random();
			for (int i = 1; i < n; i++) {
				int j = random.nextInt(i);
				swap(arr, i + from, j + from);
			}
		}

		static void swap(int[] arr, int x, int y) {
			int tmp = arr[x];
			arr[x] = arr[y];
			arr[y] = tmp;
		}

		static <T> void swap(T[] arr, int x, int y) {
			T tmp = arr[x];
			arr[x] = arr[y];
			arr[y] = tmp;
		}

		public static <T> void printArray(T[] arr, String message) {
			printArray(arr, 0, arr.length - 1, message);
		}

		public static <T> void printArray(T[] arr, int from, int to, String message) {
			System.out.print(message);
			for (int i = from; i <= to; i++) {
				System.out.print(" " + arr[i]);
			}
			System.out.println();
		}
	}
}
