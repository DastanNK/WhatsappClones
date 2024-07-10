package com.example.whatsappclone.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.whatsappclone.WhatsappViewModel
import com.example.whatsappclone.data.ChatData
import com.example.whatsappclone.data.ChatUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewChatScreen (navController: NavController, viewModel: WhatsappViewModel, chatData:ChatData) {
    val message=remember{ mutableStateOf("")}
    var newChatUser:ChatUser?=null
    viewModel.toNotRepeat(chatData){chatUser ->
        newChatUser=chatUser
    }
    Column (modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(), verticalAlignment = Alignment.CenterVertically) {
                Image(imageVector = Icons.Filled.ArrowBack, contentDescription = "", modifier = Modifier.padding(8.dp).size(50.dp).clickable { navController.navigateUp() })
                Card(shape = CircleShape, modifier = Modifier.padding(8.dp)){
                    Image(painter= rememberImagePainter(newChatUser?.userImage), contentDescription = "", modifier = Modifier.size(50.dp))
                }
                Text(newChatUser?.name.toString(), fontSize = 36.sp, fontWeight = FontWeight.Normal, modifier = Modifier.padding(8.dp))
            }
            LazyColumn {
                items(items=viewModel.currentMessage){item->
                    Text(item.message.toString(), fontSize = 36.sp, fontWeight = FontWeight.Normal, modifier = Modifier.padding(8.dp))

                }
            }
            LazyColumn {
                items(items=viewModel.currentOtherMessage){item->
                    Text(item.message.toString(), fontSize = 24.sp, fontWeight = FontWeight.Normal, modifier = Modifier.padding(8.dp))

                }
            }
        }
        Column {
            Row{
                TextField(value=message.value, onValueChange = { message.value = it }, modifier = Modifier.padding(8.dp))
                Image(imageVector = Icons.Filled.Send, contentDescription = "", modifier = Modifier.padding(8.dp).size(50.dp).clickable {
                    viewModel.writeMessage(message.value, chatData.chatId.toString())
                    message.value=""
                })
            }
        }

    }
}