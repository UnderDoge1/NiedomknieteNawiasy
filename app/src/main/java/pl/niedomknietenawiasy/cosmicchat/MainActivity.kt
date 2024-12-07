package pl.niedomknietenawiasy.cosmicchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.niedomknietenawiasy.cosmicchat.ui.ChatScreen
import pl.niedomknietenawiasy.cosmicchat.ui.UserListScreen
import pl.niedomknietenawiasy.cosmicchat.ui.theme.CosmicChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ChatViewModel()
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val snackbarHostState = SnackbarHostState()
            CosmicChatTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        NavHost(navController = navController, startDestination = "users") {
                            composable("users") { UserListScreen(viewModel, navController) }
                            composable("chat/{id}") { backStackEntry ->
                                val id = backStackEntry.arguments?.getString("id")
                                ChatScreen(
                                    "12",
                                    "13",
                                    "15",
                                    "elko",
                                    viewModel,
                                    navController,
                                    snackbarHostState,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}