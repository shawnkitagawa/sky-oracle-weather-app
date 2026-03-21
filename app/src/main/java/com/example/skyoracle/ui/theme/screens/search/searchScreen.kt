package com.example.skyoracle.ui.theme.screens.search

import android.R.attr.label
import android.R.attr.onClick
import android.view.inputmethod.InlineSuggestion
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skyoracle.ui.theme.AppTheme
import com.example.skyoracle.Greeting
import com.example.skyoracle.model.GeoData
import com.example.skyoracle.ui.theme.Dimens
import com.example.skyoracle.ui.theme.viewmodel.searchViewModel


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: searchViewModel = viewModel(factory = searchViewModel.Factory),
    oncitySelected: (Double, Double, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    Column()
    {
        SearchBar(
            query = uiState.query,
            onQueryChange = { viewModel.updateQuery(it) },
            onSearchClick = { viewModel.searchByCity(uiState.query) }
        )
        SuggestionList(suggestions = uiState.suggestion?: emptyList(), onClick = {lat, lon, cityName -> oncitySelected(lat, lon, cityName)})
    }
}

@Composable
fun SuggestionList(
    suggestions: List<GeoData>,
    onClick: (Double, Double, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = Dimens.paddingLarge)
    ) {
        items(suggestions) { suggestion ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClick(suggestion.lat, suggestion.lon, suggestion.name)
                    },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = Dimens.paddingLarge,
                            vertical = Dimens.paddingMedium
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = suggestion.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = suggestion.country,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = "Select",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.paddingLarge),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            label = { Text("Search city") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Button(
            onClick = onSearchClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Search")
        }
    }
}




@Preview(showBackground = true)
@Composable
fun searchPreview() {
    AppTheme {

    }
}