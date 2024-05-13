package com.example.landmarkremark.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.landmarkremark.model.LandmarkInformation
import com.example.landmarkremark.viewmodel.BaseViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel() {

	private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

	private val _landmarkList = MutableLiveData<List<LandmarkInformation>>()
	val landmarkList: LiveData<List<LandmarkInformation>> = _landmarkList

	fun retrieveUserLandmark() {
		updateLoading(true)
		val dataNodeReference = databaseReference.child("data").child("landmarks")
		viewModelScope.launch(Dispatchers.IO) {
			dataNodeReference.addListenerForSingleValueEvent(object : ValueEventListener {
				override fun onDataChange(dataSnapshot: DataSnapshot) {
					val landmarkList = mutableListOf<LandmarkInformation>()
					for (accountSnapshot in dataSnapshot.children) {
						val accountName = accountSnapshot.key
						accountSnapshot.children.forEach { landmarkSnapshot ->
							val landmark =
								landmarkSnapshot.getValue(LandmarkInformation::class.java)
							landmark?.let {
								it.userName = accountName
								landmarkList.add(landmark)
								_landmarkList.postValue(landmarkList)
							}
						}
					}
					updateLoading(false)
				}

				override fun onCancelled(databaseError: DatabaseError) {
					updateLoading(false)
				}
			})
		}
	}

	fun createLandmark(username : String,landmarkInformation: LandmarkInformation) {
		updateLoading(true)
		viewModelScope.launch(Dispatchers.IO) {
			databaseReference
				.child("data")
				.child("landmarks")
				.child(username)
				.child(landmarkInformation.name!!)
				.setValue(landmarkInformation)
				.addOnSuccessListener {
					updateLoading(false)
				}
				.addOnFailureListener {
					updateLoading(false)
				}
		}
	}
}