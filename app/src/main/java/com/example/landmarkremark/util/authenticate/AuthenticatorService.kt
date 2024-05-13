package com.example.landmarkremark.util.authenticate

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AuthenticatorService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}