package com.example.landmarkremark.util

import android.view.View

abstract class SingleClickListener : View.OnClickListener {
	private var lastTimeClick: Long = 0
	abstract fun onSingleClick(v: View)
	override fun onClick(v: View) {
		val currentClickTime = System.currentTimeMillis()
		val elapsedTime = currentClickTime - lastTimeClick
		if (elapsedTime <= MIN_CLICK_INTERVAL) {
			return
		}
		lastTimeClick = currentClickTime
		onSingleClick(v)
	}

	companion object {
		const val MIN_CLICK_INTERVAL: Long = 500
	}
}