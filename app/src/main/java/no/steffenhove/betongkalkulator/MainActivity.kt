package no.steffenhove.betongkalkulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import no.steffenhove.betongkalkulator.ui.navigation.AppNavigation
import no.steffenhove.betongkalkulator.ui.navigation.Screen
import no.steffenhove.betongkalkulator.ui.theme.BetongKalkulatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BetongKalkulatorApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BetongKalkulatorApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    BetongKalkulatorTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Betong Kalkulator") })
            }
        ) { innerPadding ->
            AppNavigation(
                navController = navController,
                context = context,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
