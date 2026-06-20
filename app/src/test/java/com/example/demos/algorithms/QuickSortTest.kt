package com.example.demos.algorithms

import org.junit.Test
import org.junit.Assert.assertEquals
import kotlin.random.Random

class QuickSortTest {
	@Test
	fun sortsSmallUnsortedList_andPrintsBeforeAfter() {
		val original = mutableListOf(5, 2, 9, 1, 5, 6)
		// Print the list before sorting so you can visually confirm the input.
		println("Before: $original")

		// Use the non-mutating overload which returns a sorted copy.
		val sorted = quickSort(original)

		// Print the resulting sorted list for visual confirmation.
		println("After:  $sorted")

		// Verify the returned list is sorted and the original list is unchanged.
		assertEquals(listOf(1, 2, 5, 5, 6, 9), sorted)
		assertEquals(listOf(5, 2, 9, 1, 5, 6), original)
	}

	@Test
	fun recursiveAndIterativeProduceSameSortedOutput() {
		val original = mutableListOf(3, 1, 4, 1, 5, 9, 2, 6, 5, 3)

		// Expected sorted order using Kotlin's standard library for clarity.
		val expected = original.sorted()

		// Use the non-mutating recursive overload which returns a sorted copy.
		val recursiveSorted = quickSort(original)

		// Prepare a separate mutable copy for the iterative in-place sort.
		val iterativeInput = original.toMutableList()
		iterativeQuickSort(iterativeInput)

		// Both sorted results should match the expected sorted sequence and each other.
		assertEquals(expected, recursiveSorted)
		assertEquals(expected, iterativeInput)
		assertEquals(recursiveSorted, iterativeInput)
	}

	@Test
	fun sortsEmptyList() {
		val original = mutableListOf<Int>()
		val sorted = quickSort(original)
		assertEquals(emptyList<Int>(), sorted)
		// original should remain unchanged by the non-mutating overload
		assertEquals(emptyList<Int>(), original)
	}

	@Test
	fun sortsAlreadySortedList() {
		val original = (1..100).toMutableList()
		val expected = original.toList()
		val sorted = quickSort(original)
		assertEquals(expected, sorted)
		// original remains unchanged
		assertEquals(expected, original)
	}

	@Test
	fun sortsLargeRandomList_andIterativeMatches() {
		val seed = 123456
		val rnd = Random(seed)
		val original = MutableList(10_000) { rnd.nextInt() }

		val expected = original.sorted()

		// Recursive non-mutating overload
		val recursiveSorted = quickSort(original)
		assertEquals(expected, recursiveSorted)

		// Iterative in-place sort on a separate copy
		val iterativeInput = original.toMutableList()
		iterativeQuickSort(iterativeInput)
		assertEquals(expected, iterativeInput)
	}
}
