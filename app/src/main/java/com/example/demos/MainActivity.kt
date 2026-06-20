package com.example.demos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.demos.algorithms.quickSort
import com.example.demos.ui.theme.DemosTheme

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
 * @param modifier Optional [Modifier] applied to the root column.
 */
@Composable
fun QuickSortScreen(modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

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
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DemosTheme {
        Greeting("Android")
    }
}
