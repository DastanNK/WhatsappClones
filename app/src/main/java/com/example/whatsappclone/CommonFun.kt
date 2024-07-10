package com.example.whatsappclone

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.grpc.Context


@Composable
fun Handle(viewModel: WhatsappViewModel){
    Toast.makeText(LocalContext.current, viewModel.popNotif.value, Toast.LENGTH_SHORT).show()
}

@Composable
fun Spinner(){
    Row(modifier = Modifier.fillMaxSize().clickable(enabled = false) {},verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        CircularProgressIndicator()
    }
}