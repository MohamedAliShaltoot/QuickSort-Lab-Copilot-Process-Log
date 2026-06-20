package com.example.demos
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//import com.example.demos.algorithms.SortStep
import com.example.demos.algorithms.quickSort
//import com.example.demos.algorithms.quickSortWithSteps
import com.example.demos.ui.theme.DemosTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemosTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QuickSortScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

/**
 * A simple Compose screen that accepts a comma-separated list of integers,
 * sorts them using the project's `quickSort` algorithm, and displays the result.
 *
 * The UI contains:
 * - a [TextField] for entering comma-separated integers,
 * - a [Button] labelled "Sort" which parses and sorts the input,
 * - and a [Text] area that shows either the sorted result or an error message.
 *
 * Input/validation behavior:
 * - If the input is empty or only whitespace, the screen shows the error
 *   "Please enter at least one number." and does not attempt to parse.
 * - If any token cannot be parsed as an [Int], the screen shows the error
 *   "Input contains invalid integer(s). Please enter only integers separated by commas.".
 * - For valid input, the composable calls `quickSort(nums)` (the non-mutating overload)
 *   and displays the sorted sequence. The original parsed list is not mutated.
 *
 * --- Visualization additions ---
 * On a successful sort, the screen also computes a frame-by-frame record of the
 * QuickSort process via `quickSortWithSteps` and renders it as an animated bar
 * chart: bars represent array values, red = current pivot, orange = element just
 * swapped, yellow = element currently being compared, blue = untouched, and green
 * = the final fully-sorted state. A Play/Pause button auto-advances the animation,
 * and a Slider lets the user scrub through steps manually.
 *
 * @param modifier Optional [Modifier] applied to the root column.
 */
@Composable
fun QuickSortScreen(modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    // --- Visualization additions: animation state ---
    var steps by remember { mutableStateOf<List<SortStep>>(emptyList()) }
    var currentStepIndex by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    val speedMs = 250L

    // Drives the auto-play animation: advances one step every `speedMs`
    // while isPlaying is true, and stops automatically at the last step.
    LaunchedEffect(isPlaying, steps) {
        while (isPlaying && currentStepIndex < steps.size - 1) {
            delay(speedMs)
            currentStepIndex++
        }
        if (steps.isNotEmpty() && currentStepIndex >= steps.size - 1) {
            isPlaying = false
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Enter comma-separated integers") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            errorText = null
            resultText = ""
            steps = emptyList()
            currentStepIndex = 0
            isPlaying = false

            if (input.isBlank()) {
                errorText = "Please enter at least one number."
                return@Button
            }

            // Split on commas, trim whitespace, ignore empty tokens
            val tokens = input.split(",").map { it.trim() }.filter { it.isNotEmpty() }

            if (tokens.isEmpty()) {
                errorText = "Please enter at least one number."
                return@Button
            }

            try {
                val nums = tokens.map { it.toInt() }.toMutableList()
                // Use the convenience overload which returns a sorted copy.
                val sorted = quickSort(nums)
                resultText = sorted.joinToString(", ")

                // --- Visualization additions: build the animation frames ---
                steps = quickSortWithSteps(nums)
                currentStepIndex = 0
                isPlaying = true
            } catch (_: NumberFormatException) {
                errorText = "Input contains invalid integer(s). Please enter only integers separated by commas."
            }
        }) {
            Text("Sort")
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (errorText != null) {
            Text(text = errorText ?: "", color = Color.Red)
        } else {
            if (resultText.isEmpty()) {
                Text(text = "Sorted result will appear here")
            } else {
                Text(text = "Sorted: $resultText")
            }
        }

        // --- Visualization additions: animated bar chart + controls ---
        if (steps.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Step ${currentStepIndex + 1} / ${steps.size}")

            Spacer(modifier = Modifier.height(8.dp))
            SortBarChart(
                step = steps[currentStepIndex],
                modifier = Modifier.fillMaxWidth().height(220.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Slider(
                value = currentStepIndex.toFloat(),
                onValueChange = {
                    isPlaying = false
                    currentStepIndex = it.toInt()
                },
                valueRange = 0f..(steps.size - 1).coerceAtLeast(0).toFloat()
            )

            Row {
                Button(onClick = { isPlaying = !isPlaying }) {
                    Text(if (isPlaying) "Pause" else "Play")
                }
                Spacer(modifier = Modifier.height(0.dp))
                Spacer(modifier = Modifier.fillMaxWidth())
                Button(onClick = {
                    isPlaying = false
                    currentStepIndex = 0
                }) {
                    Text("Restart")
                }
            }
        }
    }
}

/**
 * Renders a single [SortStep] as a bar chart on a [Canvas].
 *
 * Color legend:
 * - Red: the current pivot element
 * - Orange: the element that was just swapped into place
 * - Yellow: the element currently being compared against the pivot
 * - Blue: any other element still inside the active [low, high] range
 * - Green: final state, once the whole array is sorted (pivotIndex == -1)
 */
@Composable
private fun SortBarChart(step: SortStep, modifier: Modifier = Modifier) {
    val maxValue = (step.array.maxOrNull() ?: 1).coerceAtLeast(1)

    Canvas(modifier = modifier) {
        val barCount = step.array.size
        if (barCount == 0) return@Canvas

        val barWidth = size.width / barCount
        val gap = barWidth * 0.15f

        step.array.forEachIndexed { index, value ->
            val barHeightFraction = value.toFloat() / maxValue
            val barHeight = size.height * barHeightFraction
            val left = index * barWidth + gap / 2
            val top = size.height - barHeight

            val color = when {
                step.pivotIndex == -1 -> Color(0xFF4CAF50) // green: fully sorted
                index == step.pivotIndex -> Color(0xFFE53935) // red: pivot
                index == step.activeIndex && step.isSwap -> Color(0xFFFF9800) // orange: just swapped
                index == step.activeIndex -> Color(0xFFFFEB3B) // yellow: comparing
                index in step.low..step.high -> Color(0xFF2196F3) // blue: active range
                else -> Color(0xFFB0BEC5) // gray: outside current range
            }

            drawRect(
                color = color,
                topLeft = Offset(left, top),
                size = Size(barWidth - gap, barHeight)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DemosTheme {
        Greeting("Android")
    }
}
