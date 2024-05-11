package com.example.landmarkremark.fragment.main

import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.landmarkremark.BR
import com.example.landmarkremark.R
import com.example.landmarkremark.databinding.FragmentLandmarksBinding
import com.example.landmarkremark.fragment.BaseFragment
import com.example.landmarkremark.viewmodel.main.MainViewModel

class LandmarksFragment : BaseFragment<MainViewModel, FragmentLandmarksBinding>(R.layout.fragment_landmarks) {
    override fun variableId(): Int = BR.landmarksViewModel

    override fun createViewModel(): Lazy<MainViewModel> = activityViewModels()

    override fun bindView(view: View): FragmentLandmarksBinding =
        FragmentLandmarksBinding.bind(view)

}