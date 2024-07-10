package com.example.whatsappclone.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.whatsappclone.BottomNavigation
import com.example.whatsappclone.BottomNavigationItem
import com.example.whatsappclone.Screen
import com.example.whatsappclone.WhatsappViewModel
import com.example.whatsappclone.data.ChatData
import com.example.whatsappclone.data.ChatUser
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, viewModel: WhatsappViewModel) {
    var number by remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    val allChats = remember{viewModel.chatDataMain}
    Scaffold(bottomBar = {
        BottomNavigation(navController, BottomNavigationItem.Chat)
    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            showDialog.value = true
        }) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "")
        }
    }){
        LazyColumn(modifier = Modifier.fillMaxWidth().padding(it)) {
            items(items=allChats){allChat->
                EachChat(allChat, navController, viewModel)
            }
        }
        if(showDialog.value){
            AlertDialog(onDismissRequest = {
                showDialog.value = false
            }, confirmButton = {
                Button(onClick = {
                    viewModel.addChat(number)
                    showDialog.value = false
                }){
                    Text("Add chat")
                }
            }, dismissButton = {
                Button(onClick = {
                    showDialog.value = false
                }){
                    Text("Cancel")
                }
            }, text= {
                TextField(value = number, onValueChange = {number=it}, label = { "Number" })
            })
        }
    }


}

@Composable
fun EachChat(chat: ChatData, navController: NavController, viewModel: WhatsappViewModel){
    var newChatUser:ChatUser?=null
    viewModel.toNotRepeat(chat){chatUser ->
         newChatUser=chatUser
    }
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp).border(width = 1.dp,
        color = Color.Black,
        shape = RoundedCornerShape(10.dp)).padding(10.dp).clickable {
        viewModel.retrieveMessage(chat.chatId.toString())
        newChatUser?.let { viewModel.retrieveOtherMessage(chat.chatId.toString(), it) }
        navController.navigate(Screen.NewChat.createRoute(chat))
    }, verticalAlignment = Alignment.CenterVertically) {
        Card(shape = CircleShape, modifier = Modifier.padding(8.dp)) {

            Image(painter=rememberImagePainter(newChatUser?.userImage), contentDescription = "", modifier = Modifier.size(50.dp))
        }
        Text(newChatUser?.name.toString(), fontSize = 36.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(8.dp))


    }
}