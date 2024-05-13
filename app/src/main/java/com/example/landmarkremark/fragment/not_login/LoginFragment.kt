package com.example.landmarkremark.fragment.not_login

import android.app.AlertDialog
import android.content.Intent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.landmarkremark.BR
import com.example.landmarkremark.R
import com.example.landmarkremark.activity.MainActivity
import com.example.landmarkremark.databinding.FragmentLoginBinding
import com.example.landmarkremark.fragment.BaseFragment
import com.example.landmarkremark.util.Constant.KEY_USER_NAME
import com.example.landmarkremark.util.DelegatedPreferences
import com.example.landmarkremark.util.DialogHelper.Companion.showAlertDialog
import com.example.landmarkremark.util.SingleClickListener
import com.example.landmarkremark.util.Utils.isNetworkAvailable
import com.example.landmarkremark.util.authenticate.UserManager
import com.example.landmarkremark.viewmodel.not_login.NotLoginViewModel


class LoginFragment :
    BaseFragment<NotLoginViewModel, FragmentLoginBinding>(R.layout.fragment_login) {
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
                    checkNetworkAvailable()
                }
            })
        }
    }

    private fun checkNetworkAvailable() {
        if (isNetworkAvailable(requireContext())) {
            validateLoginInformation()
        } else {
            showAlertDialog(requireContext(),"Network is not available")
        }
    }

    private fun validateLoginInformation() {
        binding.apply {
            val userName = loginEdUsername.text.toString().trim()
            val password = loginEdPassword.text.toString().trim()
            if (userName.isEmpty() || password.isEmpty()) {
                showAlertDialog(requireContext(),"Please fill full your information")
            } else {
                loginAccount(userName, password)
            }
        }
    }

    private fun loginAccount(userName: String, password: String) {
        viewModel?.getUserPassword(
            userName,
            getPasswordValue = { passwordValue ->
                // check password is correct or not
                if (password == passwordValue) {
                    UserManager.getInstance(requireContext()).addAccount("1", "")
                    DelegatedPreferences(requireContext(),KEY_USER_NAME,"").setValue(userName)
                    startActivity(Intent(requireContext(),MainActivity::class.java))
                    activity?.finish()
                } else {
                    showAlertDialog(requireContext(),"Your account information is incorrect")
                }
            })
    }


}