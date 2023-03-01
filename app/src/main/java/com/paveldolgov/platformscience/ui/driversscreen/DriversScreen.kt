package com.paveldolgov.platformscience.ui.driversscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paveldolgov.platformscience.R
import com.paveldolgov.platformscience.model.Driver
import com.paveldolgov.platformscience.model.DriverId
import com.paveldolgov.platformscience.ui.common.ErrorDialog
import com.paveldolgov.platformscience.ui.common.ScreenLoader


@Composable
fun DriversScreen(
    onAssignmentClicked: (DriverId) -> Unit,
    viewModel: DriversScreenViewModel = hiltViewModel(),
) {
    when (val state =
        viewModel.state.collectAsStateWithLifecycle(DriversScreenState.Loading).value) {
        is DriversScreenState.Error -> ErrorDialog(state.message) {
            viewModel.onErrorDialogDismissed(
                state.error
            )
        }
        DriversScreenState.Loading -> ScreenLoader()
        is DriversScreenState.Success -> DriversList(
            state,
            onAssignmentClicked,
            viewModel::onRefreshRequested
        )
        DriversScreenState.UnrecoverableError -> UnrecoverableErrorScreen()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DriversList(
    state: DriversScreenState.Success,
    onAssignmentClicked: (DriverId) -> Unit,
    onRefreshRequested: () -> Unit
) {
    val isRefreshing = state.isRefreshing
    val pullRefreshState = rememberPullRefreshState(isRefreshing, onRefreshRequested)

    Box(Modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxHeight(),
        ) {
            item {
                Text(
                    text = stringResource(R.string.drivers_list_description),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            items(
                items = state.drivers,
                key = { it.id.value },
                itemContent = { DriverItem(it, onAssignmentClicked) }
            )
        }
        PullRefreshIndicator(
            isRefreshing,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun DriverItem(driver: Driver, onClick: (DriverId) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick(driver.id) }
        .background(LightGray)
        .padding(4.dp)
    ) {
        Text(driver.fullName, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun UnrecoverableErrorScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(LightGray)
            .padding(4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            stringResource(id = R.string.unrecoverable_drivers_error_state_message),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}