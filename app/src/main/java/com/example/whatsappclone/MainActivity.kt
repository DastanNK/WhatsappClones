package com.example.whatsappclone

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.whatsappclone.Screens.*
import com.example.whatsappclone.data.ChatData
import com.example.whatsappclone.data.ChatUser
import com.example.whatsappclone.ui.theme.WhatsappCloneTheme
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhatsappCloneTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    WhatsappClone()
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object LogIn : Screen("login")
    object SignIn : Screen("signin")
    object Status: Screen("status")
    object Chat : Screen("chat")
    object Profile : Screen("profile")
    object NewChat : Screen("newchat/{chatData}"){
        fun createRoute(chatData: ChatData):String{
            return "newchat/${Uri.encode(Gson().toJson(chatData))}"
        }
    }
}

@Composable
fun WhatsappClone() {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<WhatsappViewModel>()
    Handle(viewModel)
    val loading= viewModel.progress.value
    if(loading){
        Spinner()
    }
    NavHost(navController = navController, startDestination = Screen.SignIn.route) {
        composable(route = Screen.SignIn.route) {
            SignInScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.LogIn.route) {
            LogInScreen(navController = navController, viewModel=viewModel)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.Chat.route) {
            ChatScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.Status.route) {
            StatusScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.NewChat.route, arguments = listOf(navArgument("chatData"){
            type= NavType.StringType})) { navBackStackEntry ->
            val chatDataOne = navBackStackEntry.arguments?.getString("chatData")
            val chatDataTwo= Gson().fromJson(chatDataOne, ChatData::class.java)
            NewChatScreen(navController = navController, viewModel = viewModel, chatData = chatDataTwo)
        }
    }
}