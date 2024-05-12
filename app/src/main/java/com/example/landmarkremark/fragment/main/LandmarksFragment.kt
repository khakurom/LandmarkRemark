package com.example.landmarkremark.fragment.main

import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.landmarkremark.BR
import com.example.landmarkremark.R
import com.example.landmarkremark.adapter.LandmarkAdapter
import com.example.landmarkremark.databinding.FragmentLandmarksBinding
import com.example.landmarkremark.fragment.BaseFragment
import com.example.landmarkremark.model.LandmarkInformation
import com.example.landmarkremark.viewmodel.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LandmarksFragment :
    BaseFragment<MainViewModel, FragmentLandmarksBinding>(R.layout.fragment_landmarks) {
    override fun variableId(): Int = BR.landmarksViewModel

    override fun createViewModel(): Lazy<MainViewModel> = activityViewModels()

    override fun bindView(view: View): FragmentLandmarksBinding =
        FragmentLandmarksBinding.bind(view)

    override fun init() {
        super.init()
        val adapter: LandmarkAdapter by lazy {
            LandmarkAdapter()
        }
        viewModel?.retrieveUserLandmark()
        binding.landmarksRcv.adapter = adapter

    }

}