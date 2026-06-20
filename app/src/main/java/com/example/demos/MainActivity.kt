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
            // Split on commas, trim whitespace, ignore empty tokens
            val tokens = input.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            try {
                val nums = tokens.map { it.toInt() }.toMutableList()
                // Use the convenience overload which returns a sorted copy.
                val sorted = quickSort(nums)
                resultText = sorted.joinToString(", ")
            } catch (e: NumberFormatException) {
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
