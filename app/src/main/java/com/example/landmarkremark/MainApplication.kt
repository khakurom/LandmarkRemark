package com.example.landmarkremark

import android.app.Application
import com.example.landmarkremark.util.authenticate.UserManager
import com.google.firebase.FirebaseApp

var application: MainApplication? = null


class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        UserManager.init(applicationContext)
        FirebaseApp.initializeApp(this)
        application = this
    }
}