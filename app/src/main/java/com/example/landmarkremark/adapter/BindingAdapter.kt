package com.example.landmarkremark.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.landmarkremark.model.LandmarkInformation


@BindingAdapter("submitLandmarkList")
fun RecyclerView.submitLandmarkList (landmarkList: List<LandmarkInformation>?) {
	landmarkList?.let {
		(adapter as LandmarkAdapter).submitList(it)
	}
}
