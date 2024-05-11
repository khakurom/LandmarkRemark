package com.example.landmarkremark.fragment.not_login

import android.content.Intent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.landmarkremark.BR
import com.example.landmarkremark.R
import com.example.landmarkremark.activity.MainActivity
import com.example.landmarkremark.databinding.FragmentLoginBinding
import com.example.landmarkremark.fragment.BaseFragment
import com.example.landmarkremark.util.SingleClickListener
import com.example.landmarkremark.viewmodel.not_login.NotLoginViewModel


class LoginFragment : BaseFragment<NotLoginViewModel, FragmentLoginBinding>(R.layout.fragment_login) {
    override fun variableId(): Int = BR.loginViewModel

    override fun createViewModel(): Lazy<NotLoginViewModel> = activityViewModels()

    override fun bindView(view: View): FragmentLoginBinding = FragmentLoginBinding.bind(view)

    override fun init() {
        super.init()
        binding.apply {
            loginBtRegister.setOnClickListener(object : SingleClickListener() {
                override fun onSingleClick(v: View) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
                }
            })
            loginBtLogin.setOnClickListener(object : SingleClickListener() {
                override fun onSingleClick(v: View) {
                    startActivity(Intent(requireContext(),MainActivity ::class.java))
                }
            })
        }
    }

}