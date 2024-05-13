package com.example.landmarkremark.fragment.main

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.example.landmarkremark.BR
import com.example.landmarkremark.R
import com.example.landmarkremark.adapter.LandmarkAdapter
import com.example.landmarkremark.databinding.FragmentLandmarksBinding
import com.example.landmarkremark.fragment.BaseFragment
import com.example.landmarkremark.viewmodel.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

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
		binding.apply {
			landmarksRcv.adapter = adapter

			landmarksSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
				override fun onQueryTextSubmit(query: String?): Boolean {
					landmarksSearchView.clearFocus()
					return false
				}

				override fun onQueryTextChange(newText: String?): Boolean {
					filter(newText, adapter)
					return true
				}
			})
		}
	}

	private fun filter(query: String?, adapter: LandmarkAdapter) {
		val filteredList = flow {
			emit(viewModel?.landmarkList?.value?.filter { landmark ->
				landmark.name?.contains(query.orEmpty(), ignoreCase = true) == true ||
						landmark.userName?.contains(query.orEmpty(), ignoreCase = true) == true
			})
		}.flowOn(Dispatchers.IO)
		CoroutineScope(Dispatchers.IO).launch {
			filteredList.collectLatest {
				adapter.submitList(it)
			}
		}
	}
}