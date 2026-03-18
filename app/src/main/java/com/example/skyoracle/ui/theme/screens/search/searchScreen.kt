package com.example.skyoracle.ui.theme.screens.search

import android.R.attr.label
import android.view.inputmethod.InlineSuggestion
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skyoracle.Greeting
import com.example.skyoracle.model.GeoData
import com.example.skyoracle.ui.theme.SkyOracleTheme
import com.example.skyoracle.ui.theme.viewmodel.searchViewModel


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: searchViewModel = viewModel(factory = searchViewModel.Factory),
    oncitySelected: (Double, Double) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    Column()
    {
        searchBar(
            query = uiState.query,
            onQueryChange = { viewModel.updateQuery(it) },
            onSearchClick = { viewModel.searchByCity(uiState.query) }
        )
        suggetionList(suggestions = uiState.suggestion?: emptyList(), onClick = {lat, lon -> oncitySelected(lat, lon)})
    }
}

@Composable
fun suggetionList(modifier: Modifier = Modifier,
                  suggestions: List<GeoData>,
                  onClick:(Double, Double) -> Unit,
                  )
{
    LazyColumn {
        items(suggestions) { suggestion ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClick(suggestion.lat, suggestion.lon)
                    }
            ) {
                Text(text = suggestion.name)
                Text(text = "(${suggestion.country})")
            }
        }
    }
}
@Composable
fun searchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            label = { Text("Search City") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSearchClick
        ) {
            Text("Search")
        }
    }
}




@Preview(showBackground = true)
@Composable
fun searchPreview() {
    SkyOracleTheme {

    }
}