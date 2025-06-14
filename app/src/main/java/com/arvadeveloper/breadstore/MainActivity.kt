package com.arvadeveloper.breadstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arvadeveloper.breadstore.ui.theme.BreadStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BreadStoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BakeryApp()
                }
            }
        }
    }
}

@Composable
fun BakeryApp() {
    var currentScreen by remember { mutableStateOf("login") }
    var isLoggedIn by remember { mutableStateOf(false) }
    var userType by remember { mutableStateOf("") } // "admin" atau "user"

    when {
        !isLoggedIn && currentScreen == "login" -> {
            LoginScreen(
                onLoginClick = { email, password ->
                    val loginResult = validateLogin(email, password)
                    if (loginResult.isNotEmpty()) {
                        isLoggedIn = true
                        userType = loginResult
                        currentScreen = if (loginResult == "admin") "dashboard_admin" else "home"
                    }
                },
                onRegisterClick = {
                    currentScreen = "register"
                }
            )
        }
        !isLoggedIn && currentScreen == "register" -> {
            RegisterScreen(
                onRegisterClick = {
                    isLoggedIn = true
                    userType = "user"
                    currentScreen = "home"
                },
                onBackToLogin = {
                    currentScreen = "login"
                }
            )
        }
        isLoggedIn && currentScreen == "dashboard_admin" && userType == "admin" -> {
            DashboardAdminScreen(
                onLogout = {
                    isLoggedIn = false
                    userType = ""
                    currentScreen = "login"
                }
            )
        }
        isLoggedIn && currentScreen == "home" && userType == "user" -> {
            BreadListScreen(
                onNavigateToCart = {
                    currentScreen = "cart"
                },

            )
        }
        isLoggedIn && currentScreen == "cart" -> {
            CartScreen(
                onNavigateBack = {
                    currentScreen = "home"
                },
                onCheckout = {
                    // Navigate back to home after checkout
                    currentScreen = "home"
                }
            )
        }
    }
}

// Fungsi untuk validasi login
fun validateLogin(email: String, password: String): String {
    return when {
        email == "admin@admin.com" && password == "admin123" -> "admin"
        email == "user@gmail.com" && password == "12345678" -> "user"
        else -> "" // Login gagal
    }
}