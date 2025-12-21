package com.iftikar.ownerintern.presentation.search_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MicNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.iftikar.ownerintern.domain.model.Destination
import com.iftikar.ownerintern.presentation.components.TopBarComponent
import com.iftikar.ownerintern.ui.theme.CustomLightGray
import com.iftikar.ownerintern.ui.theme.LightOrange

@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel(),
    onDestinationClick: (String, String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarComponent(
                content = {
                    Text(
                        text = "Search",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                rightIcon = {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Blue,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                onAction(SearchScreenAction.OnDestinationSearch(""))
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(top = 24.dp, start = 18.dp, end = 18.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SearchBar(
                query = state.queryText,
                onQueryChange = { onAction(SearchScreenAction.OnQueryChange(it)) },
                focusRequester = focusRequester,
                keyboardController = keyboardController,
                onGo = {
                    onAction(SearchScreenAction.OnDestinationSearch(it))
                }
            )

            Destinations(
                modifier = Modifier.padding(18.dp),
                isLoading = state.isLoading,
                error = state.error,
                destinations = state.destinations,
                onDestinationClick = { id, name ->
                    onDestinationClick(id, name)}
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
    onGo: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search Places") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Go
        ),

        keyboardActions = KeyboardActions(
            onGo = {
                onGo(query)
                keyboardController?.hide()
            }
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                VerticalDivider(Modifier.height(30.dp))
                Spacer(Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Default.MicNone,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = CustomLightGray,
            unfocusedContainerColor = CustomLightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun Destinations(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    error: String?,
    destinations: List<Destination>,
    onDestinationClick: (String, String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Text(
            text = "Search Places",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(14.dp))

        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = LightOrange
                )
            }
        }

        if (error != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = error,
                    color = Red
                )
            }
        }

        if (destinations.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No such destination",
                    color = LightOrange
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            items(destinations) { destination ->
                DestinationCard(
                    destination = destination,
                    onClick = { id, name ->
                        onDestinationClick(id, name)}
                )
            }
        }

    }
}

@Composable
private fun DestinationCard(
    modifier: Modifier = Modifier,
    destination: Destination,
    onClick: (String, String) -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        onClick = { onClick(destination.id, destination.name) },
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(6.dp)
                    .aspectRatio(4f / 3f)
                    .clip(RoundedCornerShape(14.dp))
            ) {
                AsyncImage(
                    model = destination.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                text = destination.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = destination.location,
                )
            }
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Blue,
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        append("$${destination.cost}/")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        append("Person")
                    }
                }
            )
        }
    }
}































