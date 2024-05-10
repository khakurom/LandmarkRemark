package com.example.landmarkremark.fragment

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.landmarkremark.viewmodel.BaseViewModel


abstract class BaseFragment<VM : BaseViewModel, VB : ViewDataBinding>(@LayoutRes fragmentLayoutId: Int) : Fragment(fragmentLayoutId) {

	abstract fun variableId(): Int
	abstract fun createViewModel(): Lazy<VM>?
	abstract fun bindView(view: View): VB

	var viewModel: VM? = null
	lateinit var binding: VB

	private var isLoading = false


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding = bindView(requireView()).apply {
			lifecycleOwner = viewLifecycleOwner
			viewModel = createViewModel()?.value
			viewModel?.let { setVariable(variableId(), it) }
		}
		init()
	}


	open fun init() {

	}

	open fun backToPreScreen() {
		requireActivity().onBackPressedDispatcher.onBackPressed()
	}


	protected fun observeLoadingState() {
		viewModel?.isLoading!!.observe(this) { loading ->
			isLoading = if (loading!!) {
				requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
				true
			} else {
				requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
				false
			}
		}
	}

	protected fun handleOnBack() {
		val callback = object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				if (isLoading)
					return
				else {
					remove()
					activity?.onBackPressedDispatcher?.onBackPressed()
				}
			}
		}
		requireActivity().onBackPressedDispatcher.addCallback(this, callback)
	}


}