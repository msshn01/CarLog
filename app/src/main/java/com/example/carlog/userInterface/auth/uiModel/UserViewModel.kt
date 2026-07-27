package com.example.carlog.userInterface.auth.uiModel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.Current

class UserViewModel(private var auth : FirebaseAuth = Firebase.auth,) : ViewModel() {
    fun logOut( ){
        viewModelScope.launch {

            auth.signOut()


        }

    }
}