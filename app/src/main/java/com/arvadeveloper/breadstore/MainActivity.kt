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

    when {
        !isLoggedIn && currentScreen == "login" -> {
            LoginScreen(
                onLoginClick = {
                    isLoggedIn = true
                    currentScreen = "home"
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
                    currentScreen = "home"
                },
                onBackToLogin = {
                    currentScreen = "login"
                }
            )
        }
        isLoggedIn && currentScreen == "home" -> {
            BreadListScreen(
                onNavigateToCart = {
                    currentScreen = "cart"
                }
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