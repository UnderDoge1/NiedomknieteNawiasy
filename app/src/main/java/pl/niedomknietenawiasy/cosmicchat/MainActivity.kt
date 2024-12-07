package pl.niedomknietenawiasy.cosmicchat

import android.content.SharedPreferences
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
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import pl.niedomknietenawiasy.cosmicchat.model.DatabaseProvider
import pl.niedomknietenawiasy.cosmicchat.ui.ChatScreen
import pl.niedomknietenawiasy.cosmicchat.ui.UserListScreen
import pl.niedomknietenawiasy.cosmicchat.ui.theme.CosmicChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DatabaseProvider.getDatabase(this)
        val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            masterKey,
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        val viewModel = ChatViewModel(db, sharedPreferences)
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
                                    id!!,
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