package com.example.landmarkremark.fragment.not_login

import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.landmarkremark.BR
import com.example.landmarkremark.R
import com.example.landmarkremark.databinding.FragmentLoginBinding
import com.example.landmarkremark.fragment.BaseFragment
import com.example.landmarkremark.viewmodel.not_login.NotLoginViewModel


class LoginFragment : BaseFragment<NotLoginViewModel, FragmentLoginBinding>(R.layout.fragment_login) {
    override fun variableId(): Int = BR.loginViewModel

    override fun createViewModel(): Lazy<NotLoginViewModel> = activityViewModels()

    override fun bindView(view: View): FragmentLoginBinding = FragmentLoginBinding.bind(view)

}