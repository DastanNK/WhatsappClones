package com.example.whatsappclone.Screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.whatsappclone.*
import com.example.whatsappclone.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: WhatsappViewModel) {
    val userData = viewModel.userData.value
    val name = userData?.name ?: ""
    val number = userData?.number ?: ""
    val launch = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.uploadProfileImage(it)
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth().weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Back", modifier = Modifier.clickable {
                    navController.popBackStack()
                })
                Text("Save", modifier = Modifier.clickable {
                    viewModel.createOrUpdateUser(name = name, number = number)
                })
            }
            Divider(modifier = Modifier.fillMaxWidth())
            Card(
                modifier = Modifier.padding(16.dp).heightIn(min = 56.dp, max = 64.dp).widthIn(min = 56.dp, max = 64.dp)
                    .clickable { launch.launch("image/*") }, shape = CircleShape
            ) {
                if (userData?.imageUrl != null) {
                    Image(painter = rememberImagePainter(data = userData.imageUrl), contentDescription = null)
                } else {
                    Image(painter = painterResource(R.drawable.baseline_account_circle_24), contentDescription = null)
                }

            }
            Text("Change profile image")
            TextField(
                value = name,
                onValueChange = { viewModel.createOrUpdateUser(name = it) },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            TextField(
                value = number,
                onValueChange = { viewModel.createOrUpdateUser(number = it) },
                label = { Text("Number") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            Text("Logout", modifier = Modifier.padding(8.dp).clickable {
                viewModel.onLogout()
                navController.navigate(Screen.LogIn.route)
            })


        }
        BottomNavigation(navController, BottomNavigationItem.Profile)
    }
}
