package com.example.landmarkremark.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.landmarkremark.R

class DialogAddLandmark (context : Context, private val onDialogClickListener: OnDialogClickListener) : Dialog(context) {

	interface OnDialogClickListener {
		fun onPositiveButtonClick(landmarkName : String)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		requestWindowFeature(Window.FEATURE_NO_TITLE)
		setContentView(R.layout.dialog_add_landmark)

		val edLandmarkName = findViewById<EditText>(R.id.dialog_ed_landmark_name)
		val btAddLandmark = findViewById<Button>(R.id.dialog_bt_add_landmark)
		val tvError = findViewById<TextView>(R.id.dialog_tv_error)

		btAddLandmark.setOnClickListener(object : SingleClickListener(){
			override fun onSingleClick(v: View) {
				val landmarkName = edLandmarkName.text.toString().trim()
				if (landmarkName.isEmpty()) {
					tvError.visibility = View.VISIBLE
				} else {
					tvError.visibility = View.GONE
					onDialogClickListener.onPositiveButtonClick(landmarkName)
					dismiss()
				}
			}

		})

	}

}