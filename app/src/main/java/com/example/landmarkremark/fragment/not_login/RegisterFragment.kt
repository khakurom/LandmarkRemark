package com.example.landmarkremark.fragment.not_login

import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.landmarkremark.BR
import com.example.landmarkremark.R
import com.example.landmarkremark.databinding.FragmentRegisterBinding
import com.example.landmarkremark.fragment.BaseFragment
import com.example.landmarkremark.viewmodel.not_login.NotLoginViewModel

class RegisterFragment : BaseFragment<NotLoginViewModel,FragmentRegisterBinding>(R.layout.fragment_register) {
    override fun variableId(): Int = BR.registerViewModel

    override fun createViewModel(): Lazy<NotLoginViewModel> = activityViewModels ()

    override fun bindView(view: View): FragmentRegisterBinding = FragmentRegisterBinding.bind(view)

}