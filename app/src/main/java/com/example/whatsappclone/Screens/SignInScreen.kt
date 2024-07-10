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
fun SignInScreen(navController: NavController, viewModel: WhatsappViewModel) {
    var name by remember{ mutableStateOf("") }
    var email by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }
    var number by remember{ mutableStateOf("") }

    if(viewModel.successfulLogin.value==true){
        navController.navigate(Screen.Profile.route)
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Sign In", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(value=name, onValueChange = {name=it}, label = {Text("Name") })
        OutlinedTextField(value=number, onValueChange = {number=it}, label = {Text("Number") })
        OutlinedTextField(value=email, onValueChange = {email=it}, label = {Text("Email") })
        OutlinedTextField(value=password, onValueChange = {password=it}, label = {Text("Password") }, visualTransformation = PasswordVisualTransformation())
        TextButton(onClick = {
            viewModel.signUp(email,password, number, name)
            navController.navigate(Screen.Profile.route)
        }){
            Text("Sign In")
        }
        Text("Already have an account? ", modifier = Modifier.clickable { navController.navigate(Screen.LogIn.route) })
    }
}