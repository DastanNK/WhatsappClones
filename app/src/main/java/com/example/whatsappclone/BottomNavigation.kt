package com.example.whatsappclone

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

enum class BottomNavigationItem(val route: String, val icon:Int) {
    Status(Screen.Status.route, R.drawable.baseline_access_time_24),
    Chat(Screen.Chat.route, R.drawable.baseline_chat_bubble_outline_24),
    Profile(Screen.Profile.route, R.drawable.baseline_account_circle_24),
}

@Composable
fun BottomNavigation(navController: NavController, item: BottomNavigationItem) {
    Row(modifier = Modifier.fillMaxWidth().height(32.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
        for(i in BottomNavigationItem.values()){
            Image(painterResource(id = i.icon), contentDescription = null, colorFilter = if(i==item)ColorFilter.tint(Color.DarkGray) else ColorFilter.tint(Color.Gray) , modifier = Modifier.height(32.dp).width(32.dp).clickable {
                navController.navigate(i.route)
            })
        }
    }

}