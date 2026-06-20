# QuickSort Lab — Copilot Process Log

## Step 2: Implementation
**Prompt used:**
"Write a QuickSort implementation in Kotlin as a function `fun quickSort(arr: MutableList<Int>,
low: Int = 0, high: Int = arr.size - 1)`. Use the Lomuto partition scheme. Sort in-place. Add
inline comments explaining the partitioning step and the recursive calls. Also add a second
overload `fun quickSort(arr: MutableList<Int>): MutableList<Int>`..."

**Result:** Copilot generated a correct Lomuto-partition recursive QuickSort on the first try.

**Modification made:** The generated overload mutated the caller's original list. Asked Copilot
to change it to return a sorted copy instead, to avoid unexpected side effects when called from UI
code later. Prompt: "Modify the second quickSort overload so it does not mutate the original
list..."

## Step 6: Unit Test
**Prompt used:** (fill in after you run it)
**Result:**