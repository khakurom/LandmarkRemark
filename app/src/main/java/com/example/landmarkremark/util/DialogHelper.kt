package com.example.landmarkremark.util

import android.app.AlertDialog
import android.content.Context

class DialogHelper {
    companion object {
         fun showAlertDialog(context : Context,message: String) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Error")
            builder.setMessage(message)

            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
}