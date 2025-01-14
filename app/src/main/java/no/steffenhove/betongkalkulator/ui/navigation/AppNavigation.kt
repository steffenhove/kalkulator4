package no.steffenhove.betongkalkulator.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import no.steffenhove.betongkalkulator.ui.screens.CalculationScreen
import no.steffenhove.betongkalkulator.ui.screens.HistoryScreen
import no.steffenhove.betongkalkulator.ui.screens.SettingsScreen

@Composable
fun AppNavigation(navController: NavHostController, context: Context, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = Screen.Calculation.route, modifier = modifier) {
        composable(Screen.Calculation.route) {
            CalculationScreen(context = context)
        }
        composable(Screen.History.route) {
            HistoryScreen(
                context = context,
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToCalculation = { navController.navigate(Screen.Calculation.route) }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(context = context)
        }
    }
}
