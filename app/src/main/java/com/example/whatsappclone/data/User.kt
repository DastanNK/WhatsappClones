package com.example.whatsappclone.data


data class User(
    val userId: String?=null,
    val name: String?=null,
    val imageUrl:String?=null,
    val number:String?=null
){
    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "imageUrl" to imageUrl,
        "number" to number
    )
}
