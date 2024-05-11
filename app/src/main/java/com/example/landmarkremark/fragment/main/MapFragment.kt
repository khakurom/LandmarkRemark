package com.example.landmarkremark.fragment.main

import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.landmarkremark.BR
import com.example.landmarkremark.R
import com.example.landmarkremark.databinding.FragmentMapBinding
import com.example.landmarkremark.fragment.BaseFragment
import com.example.landmarkremark.viewmodel.main.MainViewModel

class MapFragment : BaseFragment<MainViewModel, FragmentMapBinding>(R.layout.fragment_map) {
    override fun variableId(): Int = BR.mapViewModel

    override fun createViewModel(): Lazy<MainViewModel> = activityViewModels()

    override fun bindView(view: View): FragmentMapBinding = FragmentMapBinding.bind(view)

}