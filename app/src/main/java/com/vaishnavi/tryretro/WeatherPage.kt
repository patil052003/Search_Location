package com.vaishnavi.tryretro

import WeatherViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vaishnavi.tryretro.api.NetworkResponse
import com.vaishnavi.tryretro.api.WeatherModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("") }
    val weatherResult by viewModel.weatherResult.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp)) // Moves search bar lower

        // Search Bar
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = city,
            onValueChange = { city = it },
            label = { Text(text = "Enter City", fontSize = 18.sp) },
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions.Default,
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            trailingIcon = {
                IconButton(onClick = { viewModel.getData(city) }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            }
        )

        Spacer(modifier = Modifier.height(45.dp))

        // Display Weather Data in a Card
        when (val result = weatherResult) {
            is NetworkResponse.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(18.dp))
            }
            is NetworkResponse.Error -> {
                Text(
                    text = "Error: ${result.message}",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 19.sp
                )
            }
            is NetworkResponse.Success<*> -> {
                result.data?.let { weatherModel ->
                    WeatherInfoCard(weatherModel as WeatherModel)
                } ?: Text(
                    text = "No data available",
                    fontSize = 19.sp
                )
            }
            null -> {
                Text(
                    text = "Search for a city to get weather updates",
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun WeatherInfoCard(weatherModel: WeatherModel) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD9E8F5)), // Soft blue background
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "🌍 City: ${weatherModel.location.name}", fontSize = 25.sp)
            Spacer(modifier = Modifier.height(15.dp))
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            Text(text = "🏳 Country: ${weatherModel.location.country}", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(15.dp))
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            Text(text = "🌡 Temperature: ${weatherModel.current.temp_c}°C", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(15.dp))
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            Text(text = "🌦 Condition: ${weatherModel.current.condition.text}", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(15.dp))
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            Text(text = "💧 Humidity: ${weatherModel.current.humidity}%", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(15.dp))
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            Text(text = "💨 Wind Speed: ${weatherModel.current.wind_kph} km/h", fontSize = 22.sp)
        }
    }
}
