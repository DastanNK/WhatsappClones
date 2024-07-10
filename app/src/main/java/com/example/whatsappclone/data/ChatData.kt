package com.example.whatsappclone.data

data class ChatData(
    val chatUser1:ChatUser=ChatUser(),
    val chatUser2:ChatUser=ChatUser(),
    val chatId:String?=null
)
