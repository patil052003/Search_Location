import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaishnavi.tryretro.api.NetworkResponse
import com.vaishnavi.tryretro.api.RetrofitInstance
import com.vaishnavi.tryretro.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val weatherApi = RetrofitInstance.weatherApi

    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult: LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city: String) {
        _weatherResult.value = NetworkResponse.Loading

        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(com.vaishnavi.tryretro.api.Constant.apikey, city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                        Log.d("API_SUCCESS", "Data: ${it}")
                    } ?: run {
                        _weatherResult.value = NetworkResponse.Error("Empty response body")
                        Log.e("API_ERROR", "Error: Empty response")
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown API error"
                    _weatherResult.value = NetworkResponse.Error("Failed to load data: $errorMessage")
                    Log.e("API_ERROR", "Code: ${response.code()}, Error: $errorMessage")
                }
            } catch (e: Exception) {
                _weatherResult.value = NetworkResponse.Error("Network failure: ${e.localizedMessage}", e)
                Log.e("NETWORK_ERROR", "Error fetching data: ${e.message}", e)
            }
        }
    }
}
