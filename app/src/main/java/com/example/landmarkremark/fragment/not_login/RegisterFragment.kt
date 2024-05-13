package com.example.landmarkremark.fragment.not_login

import android.app.AlertDialog
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.landmarkremark.BR
import com.example.landmarkremark.R
import com.example.landmarkremark.databinding.FragmentRegisterBinding
import com.example.landmarkremark.fragment.BaseFragment
import com.example.landmarkremark.util.DialogHelper.Companion.showAlertDialog
import com.example.landmarkremark.util.SingleClickListener
import com.example.landmarkremark.util.Utils.isNetworkAvailable
import com.example.landmarkremark.viewmodel.not_login.NotLoginViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment :
    BaseFragment<NotLoginViewModel, FragmentRegisterBinding>(R.layout.fragment_register) {


    override fun variableId(): Int = BR.registerViewModel

    override fun createViewModel(): Lazy<NotLoginViewModel> = activityViewModels()

    override fun bindView(view: View): FragmentRegisterBinding = FragmentRegisterBinding.bind(view)

    override fun init() {
        super.init()
        binding.apply {
            registerBtBack.setOnClickListener(object : SingleClickListener() {
                override fun onSingleClick(v: View) {
                    backToPreScreen()
                }
            })

            registerBtRegister.setOnClickListener(object : SingleClickListener() {
                override fun onSingleClick(v: View) {
                    checkNetworkAvailable()
                }
            })
        }
    }


    private fun checkNetworkAvailable() {
        if (isNetworkAvailable(requireContext())) {
            validateAccountInformation()
        } else {
            showAlertDialog(requireContext(),"Network is not available")
        }
    }

    private fun validateAccountInformation() {
        binding.apply {
            val userName = registerEdUsername.text?.trim().toString()
            val password = registerEdPassword.text?.trim().toString()
            val confirmPassword = registerEdConfirmPass.text?.trim().toString()

            if (userName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showAlertDialog(requireContext(),"Please fill full your information")
            } else {
                // check length of account name and password
                if (userName.length < 3) {
                    showAlertDialog(requireContext(),"Your account name must be larger than 2 characters")
                } else if (password.length < 4) {
                    showAlertDialog(requireContext(),"Your password must be larger than 3 characters")
                } else {
                    // check confirm password
                    if (password == confirmPassword) {
                        checkAccountNameRegistered(userName)
                    } else {
                        showAlertDialog(requireContext(),"Your confirm password does not match")
                    }
                }
            }
        }
    }

    // check account name is registered or not
    private fun checkAccountNameRegistered(userName: String) {
        viewModel?.getAccountNameIsRegistered { accountNameList ->
            if (accountNameList.contains(userName)) {
                showAlertDialog(requireContext(),"This account name is registered. Please use another name")
            } else {
                registerAccount(userName, binding.registerEdPassword.text.toString().trim())
            }
        }
    }

    private fun registerAccount(userName: String, password: String) {
        viewModel?.registerAccount(
            userName,
            password,
            registerSuccess = { isSuccess ->
                if (isSuccess) {
                    showAlertDialogRegisterSuccess ()
                } else {
                    showAlertDialog (requireContext(),"Register account unsuccessfully")
                }
            }
        )
    }


    private fun showAlertDialogRegisterSuccess () {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Register account successfully")
        builder.setCancelable(false)

        builder.setPositiveButton("OK") { dialog, _ ->
            backToPreScreen()
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}