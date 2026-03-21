package com.example.skyoracle.ui.theme.screens.result


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.skyoracle.R
import com.example.skyoracle.ui.theme.Dimens
import com.example.skyoracle.ui.theme.viewmodel.HourWeather
import com.example.skyoracle.ui.theme.viewmodel.resultViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.unit.sp
import com.example.skyoracle.ui.theme.viewmodel.Status
import com.example.skyoracle.ui.theme.viewmodel.resultTodayUiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


@Composable
fun TodayScreen(
    modifier: Modifier = Modifier,
    lat: Double,
    lon: Double,
    cityName: String,
    viewModel: resultViewModel = viewModel(factory = resultViewModel.Factory),
    Nav5Days: (Double, Double) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(lat, lon) {
        viewModel.getWeatherData(lat = lat, lon = lon)
    }

    val status = uiState.status
    when (status)
    {
        Status.LOADING -> LoadingScreen()

        Status.ERROR -> ErrorScreen(onRetry = {viewModel.getWeatherData(lat, lon)})

        Status.SUCCESS -> SuccessScreen(
            lat = lat,
            lon = lon,
            uiState = uiState,
            Nav5Days = Nav5Days,
            getIcon = viewModel::getWeatherIcon,
            hourWeather = uiState.hourWeather[0],
            cityName = cityName,
        )

    }
}

@Composable
fun SuccessScreen(
    uiState: resultTodayUiState,
    getIcon: (String, Int) -> ImageVector,
    Nav5Days: (Double, Double) -> Unit,
    lat: Double,
    lon: Double,
    cityName: String,
    hourWeather: HourWeather,

)
{
    Column(modifier = Modifier.fillMaxSize())
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(Dimens.paddingXL),
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center)
            {
                Text(
                    text = cityName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = ", ${uiState.country}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = uiState.weather,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.padding(bottom = 32.dp))

//                AsyncImage(
//                    model = getIcon(hourWeather.icon),
//
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .size(200.dp),
//                    contentScale = ContentScale.Crop
//                )
            Icon(
                imageVector = getIcon(hourWeather.icon, hourWeather.weatherConidtion),
                contentDescription = hourWeather.weatherConidtion.toString(),
                modifier = Modifier.size(150.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.padding(bottom = 32.dp))

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = LocalDate.parse(hourWeather.date, formatter)

            val displayFormatter = DateTimeFormatter.ofPattern("MMM d")
            val formattedDate = date.format(displayFormatter)

            Row()
                {
                    Text(text = "${formattedDate} ",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 4.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "• ${hourWeather.time}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                }
                val rounded = String.format("%.1f", hourWeather.temp)
                Text(text = "${rounded}°", style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                   fontSize = 48.sp )


            Row(modifier = Modifier.padding(bottom = Dimens.paddingLarge))
            {
                Text(
                    text = "H:${uiState.tempMax.roundToInt()}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "L:${uiState.tempMin.roundToInt()}",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.paddingMedium),
                    colors = CardDefaults.cardColors(
                        containerColor= MaterialTheme.colorScheme.surface
                    ),
                elevation = CardDefaults.cardElevation(3.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        AdditionalInfo(R.drawable.umbrella, R.string.rainy, uiState.pop)
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        AdditionalInfo(R.drawable.wind, R.string.wind, uiState.windSpeed)
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        AdditionalInfo(R.drawable.humidity, R.string.humidity, uiState.humidity)
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(Dimens.paddingLarge))
        Column(modifier = Modifier.padding(Dimens.paddingMedium))
        {
            Row(modifier = Modifier.padding(bottom = Dimens.paddingLarge))
            {
                Text(
                    text = stringResource(R.string.today),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = Dimens.paddingLarge)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.next_5_days),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(end = Dimens.paddingLarge)
                        .clickable {
                            Nav5Days(lat, lon)
                        },

                    )

            }
            hourWeather(hourlyDatas = uiState.hourWeather, getUrl = {icon , code-> getIcon(icon, code)} )
            Log.d("UI", "hourlyData is ${uiState.hourWeather}")
        }
    }
}



@Composable
fun LoadingScreen(modifier: Modifier = Modifier)
{
    Log.d("LoadingScreen", "Loading Screen was executed")
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
    {
        CircularProgressIndicator()

    }
}

@Composable
fun ErrorScreen(
    message: String = stringResource(R.string.something_went_wrong),
    onRetry: () -> Unit,
)
{
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
fun hourWeather(
    hourlyDatas: List<HourWeather>,
    getUrl: (String, Int) -> ImageVector,
)
{
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 12.dp))
    {
        items(hourlyDatas) { data ->
            hourlyForecastCard(hourlydata = data, getUrl = getUrl)
        }
    }
}

@Composable
fun hourlyForecastCard(
    hourlydata: HourWeather,
    getUrl: (String, Int) -> ImageVector,
)
{
    Card(modifier = Modifier.width(110.dp), elevation = CardDefaults.cardElevation(2.dp))
    {
        Column(modifier = Modifier.padding(Dimens.paddingMedium).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text( text = hourlydata.time,
                style = MaterialTheme.typography.titleSmall)
//            AsyncImage(
//                model = getUrl(hourlydata.icon),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp)
//            )
            Icon(
                imageVector = getUrl(hourlydata.icon, hourlydata.weatherConidtion),
                contentDescription = hourlydata.weatherConidtion.toString(),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
            Log.d("UI", "${hourlydata}")

            Text( text = "${hourlydata.temp}°",
                style = MaterialTheme.typography.titleMedium)
        }

    }

}


@Composable
fun AdditionalInfo(
    image: Int,
    description: Int,
    data: Number,

)
{
    Column(horizontalAlignment = Alignment.CenterHorizontally)
    {
        Image(
            painter = painterResource(image),
            contentDescription = stringResource(description),
        )
        Text(
            text = if (description == R.string.rainy) "${data.toString()}%"
            else if (description == R.string.wind)
            {
                var value = data.toDouble()
                val rounded = String.format("%.1f", value)
                "${rounded} km/h"

            }
            else  "${data.toString()}%" ,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = stringResource(description),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}