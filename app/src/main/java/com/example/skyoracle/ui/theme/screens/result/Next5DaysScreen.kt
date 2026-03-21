package com.example.skyoracle.ui.theme.screens.result

import android.media.Image
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.keepScreenOn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.skyoracle.ui.theme.viewmodel.resultViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.example.skyoracle.Greeting
import com.example.skyoracle.R
import com.example.skyoracle.ui.theme.AppTheme
import com.example.skyoracle.ui.theme.Dimens
import com.example.skyoracle.ui.theme.viewmodel.DayWeathersSummary
import com.example.skyoracle.ui.theme.viewmodel.Status
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.drop
import kotlin.math.roundToInt

@Composable
fun Next5DaysScreen(
    modifier: Modifier = Modifier,
    lat: Double,
    lon: Double,
    viewModel: resultViewModel = viewModel(factory = resultViewModel.Factory)
    )
{
    LaunchedEffect(lat, lon) {
        viewModel.getWeatherData(lat, lon)
    }

    val uiState by viewModel.uiState.collectAsState()
    val status = uiState.status

    when (status)
    {
        Status.LOADING -> NextDaysLoadScreen()
        Status.ERROR -> NextDaysErrorScreen(onRetry = {viewModel.getWeatherData(lat, lon)})
        Status.SUCCESS -> NextDaysSuccessScreen(nextDaysWeather = uiState.nextDaysWeather,
           getWeatherIcon = viewModel::getWeatherIcon )
    }
}

@Composable
fun NextDaysLoadScreen(modifier: Modifier = Modifier){

        Log.d("LoadingScreen", "Loading Screen was executed")
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
        {
            CircularProgressIndicator()

        }
}

@Composable
fun NextDaysErrorScreen(
    message: String = stringResource(R.string.something_went_wrong),
    onRetry: () -> Unit,
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(text = message, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(Dimens.paddingLarge))
        Button(
            onClick = {onRetry}
        )
        {
            Text(stringResource(R.string.retry))
        }
    }

}

@Composable
fun NextDaysSuccessScreen(
    nextDaysWeather: List<DayWeathersSummary>,
    getWeatherIcon: (String, Int) -> ImageVector,

){
    Column()
    {
        MainCard(getIcon = getWeatherIcon,
            nextDaysWeather = nextDaysWeather)
        Days(nextDaysWeather = nextDaysWeather, getImage = getWeatherIcon, formatter = DateTimeFormatter.ISO_LOCAL_DATE)
    }
}


@Composable
fun Days(
    modifier: Modifier = Modifier,
    nextDaysWeather: List<DayWeathersSummary>,
    getImage: (String, Int) -> ImageVector,
    formatter: DateTimeFormatter
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.paddingLarge),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        items(nextDaysWeather.drop(1)) { weather ->
            val dateTime = LocalDate.parse(weather.date, formatter)
            val day = dateTime.format(DateTimeFormatter.ofPattern("EEE"))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.35f))
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = day,
                    modifier = Modifier.width(42.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Box(
                    modifier = Modifier.width(44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getImage(weather.icon, weather.weatherCondition),
                        contentDescription = weather.weatherCondition.toString(),
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = weather.weather,
                    modifier = Modifier.width(78.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "${weather.tempMax}°",
                    modifier = Modifier.width(42.dp),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${weather.tempMin}°",
                    modifier = Modifier.width(42.dp),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
fun MainCard(
    modifier: Modifier = Modifier,
    nextDaysWeather: List<DayWeathersSummary>,
    getIcon: (String, Int) -> ImageVector
) {
    Card(
        modifier = modifier
            .padding(horizontal = Dimens.paddingXL, vertical = Dimens.paddingLarge)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
        )
    ) {
        nextDaysWeather.firstOrNull()?.let { weather ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.size(110.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getIcon( weather.icon,weather.weatherCondition),
                            contentDescription = weather.weatherCondition.toString(),
                            modifier = Modifier.size(82.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.width(18.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Tomorrow",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = "${weather.temp}°",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = weather.weatherDescription.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase() else it.toString()
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WeatherStat(
                        icon = R.drawable.umbrella,
                        value = weather.pop.toString(),
                        label = "Rainy"
                    )
                    WeatherStat(
                        icon = R.drawable.wind,
                        value = weather.windSpeed.toString(),
                        label = "Wind"
                    )
                    WeatherStat(
                        icon = R.drawable.humidity,
                        value = weather.humidity.toString(),
                        label = "Humidity"
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherStat(
    icon: Int,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.widthIn(min = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = label,
            modifier = Modifier.size(22.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

//@Preview
//@Composable
//fun Next5DaysScreenPreview()
//{
//    AppTheme {
//        iconInfo(
//            icon = R.drawable.wind,
//            data = 2342,
//            info = R.string.wind
//
//        )
//    }
//}