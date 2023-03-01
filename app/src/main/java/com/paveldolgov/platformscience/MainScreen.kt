@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.paveldolgov.platformscience

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.paveldolgov.platformscience.Constants.DRIVER_ASSIGNMENT_ARGUMENT_KEY
import com.paveldolgov.platformscience.ui.assignmentscreen.DriverAssignmentScreen
import com.paveldolgov.platformscience.ui.common.AppBar
import com.paveldolgov.platformscience.ui.driversscreen.DriversScreen

object Constants {
    const val DRIVER_ASSIGNMENT_ARGUMENT_KEY = "driverId"
}

/**
 * Application screens with routes and top bar titles.
 */
enum class PlatformScienceScreen(val route: String, @StringRes val appBarTitle: Int) {
    DRIVERS(route = "drivers", appBarTitle = R.string.drivers_screen_title),
    DRIVER_ASSIGNMENT(route = "driverAssignment", appBarTitle = R.string.assignment_screen_title);

    companion object {
        fun fromRoute(route: String?): PlatformScienceScreen {
            return values().firstOrNull { route?.startsWith(it.route) == true } ?: DRIVERS
        }
    }
}

/**
 * Defines app screens and routing between screens.
 */
@Composable
fun PlatformScienceApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val appBarTitle =
        stringResource(id = PlatformScienceScreen.fromRoute(backStackEntry?.destination?.route).appBarTitle)

    Scaffold(
        topBar = {
            AppBar(
                title = appBarTitle,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PlatformScienceScreen.DRIVERS.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = PlatformScienceScreen.DRIVERS.route) {
                DriversScreen(
                    onAssignmentClicked = { assignmentId ->
                        navController.navigate("${PlatformScienceScreen.DRIVER_ASSIGNMENT.route}/${assignmentId.value}")
                    }
                )
            }
            composable(
                route = "${PlatformScienceScreen.DRIVER_ASSIGNMENT.route}/{$DRIVER_ASSIGNMENT_ARGUMENT_KEY}",
                arguments = listOf(navArgument(DRIVER_ASSIGNMENT_ARGUMENT_KEY) {
                    type = NavType.StringType
                }
                )
            ) {
                DriverAssignmentScreen {
                    navController.popBackStack()
                }
            }
        }
    }
}