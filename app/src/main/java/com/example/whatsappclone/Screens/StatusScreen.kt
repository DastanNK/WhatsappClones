package com.example.whatsappclone.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.whatsappclone.BottomNavigation
import com.example.whatsappclone.BottomNavigationItem
import com.example.whatsappclone.WhatsappViewModel

@Composable
fun StatusScreen(navController: NavController, viewModel: WhatsappViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Text("status")
        }
        BottomNavigation(navController, BottomNavigationItem.Status )
    }
}