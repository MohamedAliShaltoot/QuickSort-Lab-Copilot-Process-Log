@file:Suppress("unused")

package com.example.demos.algorithms
/*
 * In-place QuickSort using the Lomuto partition scheme.
 * Sorts arr[low->high] recursively; pivot is always arr[high].
 * Average O(n log n), worst case O(n^2); not a stable sort.
 */

fun quickSort(arr: MutableList<Int>, low: Int = 0, high: Int = arr.size - 1) {
	if (low >= high) return

	val pivotIndex = partition(arr, low, high)

	// Recursively sort the elements smaller than the pivot.
	quickSort(arr, low, pivotIndex - 1)
	// Recursively sort the elements larger than the pivot.
	quickSort(arr, pivotIndex + 1, high)
}

/**
 * Returns a sorted copy of [arr] without mutating the original list.
 */
fun quickSort(arr: MutableList<Int>): MutableList<Int> {
	val copy = arr.toMutableList()
	quickSort(copy, 0, copy.size - 1)
	return copy
}

/**
 * Lomuto partition: moves the pivot (arr[high]) into its final sorted
 * position so everything before it is <= pivot and everything after is
 * > pivot. Returns the pivot's final index.
 */
private fun partition(arr: MutableList<Int>, low: Int, high: Int): Int {
	val pivot = arr[high]
	var i = low - 1

	for (j in low until high) {
		// Move every value smaller than the pivot to the left side.
		if (arr[j] <= pivot) {
			i++
			val temp = arr[i]
			arr[i] = arr[j]
			arr[j] = temp
		}
	}

	// Place the pivot between the smaller and larger partitions.
	val temp = arr[i + 1]
	arr[i + 1] = arr[high]
	arr[high] = temp

	return i + 1
}

/**
 * Iterative QuickSort using an explicit stack to store subarray ranges.
 *
 * This function performs the same partitioning as the recursive `quickSort` but
 * avoids recursion by pushing (low, high) ranges onto a stack. Each loop
 * iteration pops a range, partitions it, and pushes the resulting sub-ranges
 * that still need sorting. The ArrayDeque therefore plays the role of the
 * function call stack that recursion would normally use (LIFO order).
 */
fun iterativeQuickSort(arr: MutableList<Int>) {
	// Small arrays are already sorted.
	if (arr.size < 2) return

	// Stack of (low, high) ranges to sort. We use ArrayDeque as a LIFO stack.
	val stack = ArrayDeque<Pair<Int, Int>>()

	// Start with the whole array range.
	stack.addLast(0 to arr.size - 1)

	while (stack.isNotEmpty()) {
		val (low, high) = stack.removeLast()

		// If the range is invalid or a single element, it's already sorted.
		if (low >= high) continue

		// Partition the current range and get pivot index.
		val pivotIndex = partition(arr, low, high)

		// After partition, elements left of pivotIndex are <= pivot and
		// elements right are > pivot. We need to sort those sub-ranges.
		// Push the right range first and then the left range so that the
		// left range is processed next (mimicking the typical recursive order).
		if (pivotIndex + 1 < high) {
			stack.addLast(pivotIndex + 1 to high)
		}
		if (low < pivotIndex - 1) {
			stack.addLast(low to pivotIndex - 1)
		}
	}
}

