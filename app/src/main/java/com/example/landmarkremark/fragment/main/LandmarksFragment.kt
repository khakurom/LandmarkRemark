package com.example.landmarkremark.fragment.main

import android.app.AlertDialog
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.example.landmarkremark.BR
import com.example.landmarkremark.R
import com.example.landmarkremark.adapter.LandmarkAdapter
import com.example.landmarkremark.databinding.FragmentLandmarksBinding
import com.example.landmarkremark.fragment.BaseFragment
import com.example.landmarkremark.util.Utils
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
		getAllLandmark()
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

			//
			with(landmarkSwipeRefresh) {
				setOnRefreshListener {
					isRefreshing = true
					getAllLandmark()
					isRefreshing = false
				}
			}
		}
	}

	// get all landmark that are created by user
	private fun getAllLandmark () {
		if (Utils.isNetworkAvailable(requireContext())) {
			viewModel?.retrieveUserLandmark()
		} else {
			showAlertDialog("Network is not available")
		}
	}

	private fun filter(query: String?, adapter: LandmarkAdapter) {
		// filter a search list as a text of landmark and user name.
		val filteredList = flow {
			emit(viewModel?.landmarkList?.value?.filter { landmark ->
				landmark.name?.contains(query?.trim().orEmpty(), ignoreCase = true) == true ||
						landmark.userName?.contains(query?.trim().orEmpty(), ignoreCase = true) == true
			})
		}.flowOn(Dispatchers.IO)
		CoroutineScope(Dispatchers.IO).launch {
			filteredList.collectLatest {
				adapter.submitList(it)
			}
		}
	}

	private fun showAlertDialog(message: String) {
		val builder = AlertDialog.Builder(requireContext())
		builder.setTitle("Error")
		builder.setMessage(message)

		builder.setPositiveButton("OK") { dialog, _ ->
			dialog.dismiss()
		}

		val dialog: AlertDialog = builder.create()
		dialog.show()
	}
}