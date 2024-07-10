package com.example.whatsappclone.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.whatsappclone.Screen
import com.example.whatsappclone.WhatsappViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(navController: NavController, viewModel: WhatsappViewModel) {
    var email by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }


    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Log In", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(value=email, onValueChange = {email=it}, label = { Text("Email") })
        OutlinedTextField(value=password, onValueChange = {password=it}, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
        TextButton(onClick = {
            viewModel.onLogin(email, password)
            navController.navigate(Screen.Profile.route)
        }){
            Text("Log In")
        }
        Text("Don't have an account? ", modifier = Modifier.clickable { navController.navigate(Screen.SignIn.route) })
    }
}