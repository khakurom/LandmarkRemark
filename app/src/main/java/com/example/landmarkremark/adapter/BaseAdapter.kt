package com.example.landmarkremark.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

interface OnItemSelectListener<T> {
    fun onItemSelected(item: T, position: Int)
}

interface OnItemRemoveListener<T> {
    fun onItemRemove(item: T, position: Int)
}

interface OnItemTurnOnListener<T> {
    fun onItemTurnOn(item: T, position: Int) : Boolean
    fun onItemTurnOff(item: T, position: Int)


}

/**
 * Use DiffUtil
 */
abstract class BaseAdapterDiffUtil<T, VB : ViewBinding, VH : BaseAdapterDiffUtil.BaseViewHolder<T, VB>>(diffCallback: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, VH>(diffCallback) {
    var onItemSelectListener: OnItemSelectListener<T>? = null
    var onItemRemoveListener: OnItemRemoveListener<T>? = null
    var onItemTurnOnListener: OnItemTurnOnListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = inflateBinding(inflater, parent, viewType)
        return createViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    abstract fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VB

    abstract fun createViewHolder(binding: VB): VH

    abstract class BaseViewHolder<T, VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: T)
    }
}
