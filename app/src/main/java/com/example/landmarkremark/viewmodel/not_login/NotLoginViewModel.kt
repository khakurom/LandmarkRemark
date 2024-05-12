package com.example.landmarkremark.viewmodel.not_login

import android.view.View
import androidx.lifecycle.viewModelScope
import com.example.landmarkremark.viewmodel.BaseViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotLoginViewModel : BaseViewModel() {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun getAccountNameIsRegistered(getAccountNameList: (List<String>) -> Unit) {
        updateLoading(true)
        val dataNodeReference = databaseReference.child("data").child("account")
        viewModelScope.launch(Dispatchers.IO) {
            dataNodeReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val accountNameList = mutableListOf<String>()
                    for (childSnapshot in snapshot.children) {
                        val accountName = childSnapshot.key
                        if (accountName != null) {
                            accountNameList.add(accountName)
                        }
                    }
                    getAccountNameList(accountNameList) // get all account name that is registered in database
                    updateLoading(false)
                }

                override fun onCancelled(error: DatabaseError) {
                    updateLoading(false)
                }
            })
        }
    }

    fun registerAccount(
        userName: String,
        password: String,
        registerSuccess: (Boolean) -> Unit
    ) {
        updateLoading(true)
        databaseReference.child("data").child("account").child(userName)
            .setValue(password) // upload user name and password
            .addOnSuccessListener {
                registerSuccess(true)
                updateLoading(false)
            }
            .addOnFailureListener {
                registerSuccess(false)
                updateLoading(false)
            }

    }

    fun getUserPassword (userName: String, getPasswordValue: (String?) -> Unit) {
        val dataNodeReference = databaseReference.child("data").child("account")
        updateLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            dataNodeReference.orderByKey().equalTo(userName)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.hasChild(userName)) {
                            val passwordValue = dataSnapshot.child(userName).value
                            getPasswordValue(passwordValue as? String)
                        } else {
                            getPasswordValue(null)
                        }
                        updateLoading(false)
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        getPasswordValue(null)
                        updateLoading(false)
                    }
                })
        }
    }
}