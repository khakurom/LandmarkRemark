package com.example.landmarkremark.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


abstract class BaseViewModel : ViewModel() {
	private val _isLoading = MutableLiveData<Boolean?>()
	val isLoading: LiveData<Boolean?> = _isLoading

	protected fun updateLoading(value: Boolean) {
		_isLoading.value = value
	}

}