package com.example.whatsappclone

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.whatsappclone.data.ChatData
import com.example.whatsappclone.data.ChatUser
import com.example.whatsappclone.data.Message
import com.example.whatsappclone.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

const val USER = "users"
const val CHATS = "chats"
const val MESSAGE = "message"

@HiltViewModel
class WhatsappViewModel @Inject constructor(
    val storage: FirebaseStorage,
    val auth: FirebaseAuth,
    val db: FirebaseFirestore
) : ViewModel() {
    val progress = mutableStateOf(false)
    val popNotif = mutableStateOf("")
    val successfulLogin = mutableStateOf(false)
    val userData = mutableStateOf<User?>(null)
    var chatsUserWithCurrent = mutableListOf<ChatUser>()
    var currentMessage : List<Message> = listOf()
    var chatDataMain:List<ChatData> = listOf<ChatData>()
    var currentOtherMessage : List<Message> = listOf()


    init {
        val currentUser = auth.currentUser?.uid
        successfulLogin.value = currentUser != null
        currentUser?.let {
            getUserData(currentUser)
        }
    }

    fun onLogin(email: String, password: String) {
        progress.value = true
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                auth.currentUser?.uid?.let { uid ->
                    getUserData(uid)
                }
                successfulLogin.value = true
                progress.value = false
            } else {
                handleException("can not login")
                progress.value = false
            }
        }
    }

    fun signUp(email: String, password: String, number: String, name: String) {
        progress.value = true
        if (email.isEmpty() && password.isEmpty() && number.isEmpty()) {
            handleException("fill all fields")
            return
        }
        db.collection(USER).whereEqualTo("name", name).get().addOnSuccessListener { document ->
            if (document.size() > 0) {
                handleException("already exist")
                progress.value = false
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        successfulLogin.value = true
                        createOrUpdateUser(name = name, number = number)
                        progress.value = false
                    } else {
                        handleException("can not create user")
                        progress.value = false
                    }
                }
            }
        }.addOnFailureListener {
            handleException("unable to create user")
            progress.value = false
        }
    }

    fun createOrUpdateUser(
        name: String? = null,
        imageUrl: String? = null,
        number: String? = null
    ) {
        progress.value = true
        val userId = auth.currentUser?.uid
        val user = User(
            userId,
            name ?: userData.value?.name,
            imageUrl = imageUrl ?: userData.value?.imageUrl,
            number = number ?: userData.value?.number
        )

        userId?.let {
            db.collection(USER).document(userId).get().addOnSuccessListener {
                if (it.exists()) {
                    it.reference.update(user.toMap()).addOnSuccessListener {
                        this.userData.value = user
                        progress.value = false
                    }.addOnFailureListener {
                        handleException("can not update user")
                        progress.value = false
                    }
                } else {
                    db.collection(USER).document(userId).set(user).addOnSuccessListener {
                        getUserData(userId)
                        progress.value = false
                    }.addOnFailureListener {
                        handleException("can not create 2 user")
                        progress.value = false
                    }
                }
            }.addOnFailureListener {
                handleException("can not find user")
                progress.value = false

            }
        }
    }

    fun getUserData(uid: String) {
        progress.value = true
        db.collection(USER).document(uid).get().addOnSuccessListener { document ->
            val user = document.toObject<User>()
            userData.value = user
            progress.value = false
            addAllChats()
        }.addOnFailureListener {
            handleException("unable to retrieve data")
            progress.value = false

        }
    }

    fun handleException(cause: String) {
        popNotif.value = cause
    }

    private fun uploadImage(uri: Uri, onSuccess: (Uri) -> Unit) {
        progress.value = true
        val imageRef = storage.reference
        val uuid = UUID.randomUUID()
        val image = imageRef.child("images/$uuid")
        image.putFile(uri).addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(onSuccess)
            progress.value = false
        }.addOnFailureListener {
            handleException(it.toString())
            progress.value = false
        }

    }

    fun uploadProfileImage(uri: Uri) {
        uploadImage(uri) { uri ->
            createOrUpdateUser(imageUrl = uri.toString())
        }
    }

    fun onLogout() {
        userData.value = null
        successfulLogin.value = false
        chatsUserWithCurrent = mutableListOf()
        chatDataMain= listOf()
        currentOtherMessage= listOf()
    }

    fun addChat(number: String) {
        progress.value = true
        val chatId = UUID.randomUUID().toString()
        db.collection(USER).whereEqualTo("number", number).get().addOnSuccessListener { document ->
            val user = document.toObjects<User>()
            val userOne = user.get(0)
            val chatUser1 = ChatUser(
                userId = userOne.userId,
                number = number,
                name = userOne.name,
                userImage = userOne.imageUrl ?: "",
            )
            val chatUser2 = ChatUser(
                userId = auth.currentUser?.uid,
                number = userData.value?.number,
                name = userData.value?.name,
                userImage = userData.value?.imageUrl
            )
            val chatData = ChatData(chatUser1, chatUser2, chatId)
            db.collection(CHATS).document(chatId).set(chatData).addOnSuccessListener { document ->
                addAllChats()
                handleException("live")
                progress.value = false
            }.addOnFailureListener {
                handleException("Failed to create chat")
                progress.value = false
            }
        }
    }

    fun addAllChats() {
        db.collection(CHATS).whereEqualTo("chatUser1.userId", auth.currentUser?.uid).get()
            .addOnSuccessListener { snapshot ->
                val chats = snapshot.toObjects<ChatData>()
                chatDataMain=chats
                chatsUserWithCurrent.clear()
                for (item in chats) {
                    chatsUserWithCurrent.add(item.chatUser2)
                }
            }.addOnFailureListener {
                handleException("unable to create chat")
            }

        db.collection(CHATS).whereEqualTo("chatUser2.userId", auth.currentUser?.uid).get()
            .addOnSuccessListener { snapshot ->
                val chat = snapshot.toObjects<ChatData>()
                chatDataMain+=chat
                for (item in chat) {
                    chatsUserWithCurrent.add(item.chatUser1)
                }
            }.addOnFailureListener {
                handleException("unable to create chat")
            }
    }

    fun writeMessage(message:String, chatId:String){
        val currentName=userData.value?.name
        val time=System.currentTimeMillis()
        val id=UUID.randomUUID().toString()
        val mess= Message(message=message, sentBy = currentName, time = time)
        if (currentName != null) {
            db.collection(CHATS).document(chatId).collection(MESSAGE).document(id).set(mess).addOnSuccessListener {
                retrieveMessage(chatId)
            }
        }
    }

    fun retrieveMessage(chatId:String){
        progress.value=true
        db.collection(CHATS).document(chatId).collection(MESSAGE).whereEqualTo("sentBy", userData.value?.name).get().addOnSuccessListener { document->
            val mess= document.toObjects<Message>()
            val messSorted=mess.sortedBy {it.time}
            currentMessage= listOf()
            currentMessage=messSorted
            progress.value=false
        }
    }

    fun retrieveOtherMessage(chatId:String, chatUser: ChatUser){
        progress.value=true
        db.collection(CHATS).document(chatId).collection(MESSAGE).whereEqualTo("sentBy", chatUser.name).get().addOnSuccessListener { document->
            val mess= document.toObjects<Message>()
            val messSorted=mess.sortedBy {it.time}
            currentOtherMessage= listOf()
            currentOtherMessage=messSorted
            progress.value=false
        }
    }

    fun toNotRepeat(chatData: ChatData, onOther:(ChatUser)->Unit){
        if(chatData.chatUser1.userId.equals(auth.currentUser?.uid)){
            onOther.invoke(chatData.chatUser2)
        }else{
            onOther.invoke(chatData.chatUser1)
        }
    }

    fun messageDeal(message:List<Message>){
        
    }


}