package com.example.landmarkremark.util.authenticate

import android.accounts.Account
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle

class UserManager(private val context: Context) {
    private val accountManager: AccountManager = AccountManager.get(context)

    fun addAccount(id: String, token: String) {
        val data = Bundle().apply { this.putString(ID, id) }
            .apply { this.putString(TOKEN, token) }

        val account = Account(id, ACCOUNT_TYPE)
        accountManager.addAccountExplicitly(account, id, data)
        accountManager.setAuthToken(account, AUTH_TOKEN_TYPE, token)

    }

    private fun getAccount(): Account {
        return accountManager.getAccountsByType(ACCOUNT_TYPE)[0]
    }

    fun removeAccount () {
        accountManager.removeAccountExplicitly(getAccount())
    }

    /**
     * Get token
     */
    fun getToken(): String {
        return accountManager.getUserData(
            getAccount(),
            TOKEN
        )
    }

    /**
     * Get id
     */
    fun getID(): String {
        return accountManager.getUserData(
            getAccount(),
            ID
        )
    }

    /**
     * Check whether user is logged or not
     */
    fun isLogged(): Boolean {
        val accounts = accountManager.getAccountsByType(ACCOUNT_TYPE)
        if (accounts.isNotEmpty()) {
            return true
        }
        return false
    }

    companion object {
        const val AUTH_TOKEN_TYPE = "com.example.landmarkremark"
        const val ACCOUNT_TYPE = "com-example-landmarkremark"
        const val ID = "id"
        const val TOKEN = "token"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: UserManager? = null

        fun getInstance(context: Context): UserManager {
            return instance ?: synchronized(this) {
                instance ?: UserManager(context)
            }
        }

        fun init(context: Context) {
            instance ?: synchronized(this) {
                instance ?: UserManager(context)
            }
        }
    }
}