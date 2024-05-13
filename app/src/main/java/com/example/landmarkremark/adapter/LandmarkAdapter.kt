package com.example.landmarkremark.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.landmarkremark.databinding.ItemLandmarkBinding
import com.example.landmarkremark.model.LandmarkInformation


class LandmarkAdapter :
    BaseAdapterDiffUtil<LandmarkInformation, ItemLandmarkBinding, LandmarkAdapter.LandmarkHolder>(ItemLandmarkDiffCallback()) {


    inner class LandmarkHolder (binding: ItemLandmarkBinding) :
        BaseViewHolder<LandmarkInformation, ItemLandmarkBinding>(binding) {
        override fun bind(item: LandmarkInformation) {
            binding.landmarkInfo = item
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): ItemLandmarkBinding {
        return ItemLandmarkBinding.inflate(inflater,parent,false)
    }


    override fun createViewHolder(binding: ItemLandmarkBinding): LandmarkHolder {
        return LandmarkHolder(binding)
    }
}


class ItemLandmarkDiffCallback : DiffUtil.ItemCallback<LandmarkInformation>() {
    override fun areItemsTheSame(oldItem: LandmarkInformation, newItem: LandmarkInformation): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: LandmarkInformation, newItem: LandmarkInformation): Boolean {
        return oldItem == newItem
    }
}