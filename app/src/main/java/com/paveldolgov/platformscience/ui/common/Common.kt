package com.paveldolgov.platformscience.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.paveldolgov.platformscience.R

/**
 * Loader to indicate loading state of a screen
 */
@Composable
fun ScreenLoader() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

/**
 * Error dialog to notify user about various errors.
 */
@Composable
fun ErrorDialog(message: String, onDismissed: () -> Unit) {
    Dialog(
        title = stringResource(R.string.error_dialog_title),
        message = message,
        onDismissed = onDismissed
    )
}

/**
 * Dialog to notify user about something.
 */
@Composable
fun Dialog(title: String, message: String, onDismissed: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            onDismissed()
        },
        title = {
            Text(title)
        },
        text = {
            Text(message)
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismissed()
                }) {
                Text(stringResource(R.string.error_dialog_positive_button_title))
            }
        }
    )
}

/**
 * Application top bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(title) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button_content_description)
                    )
                }
            }
        }
    )
}