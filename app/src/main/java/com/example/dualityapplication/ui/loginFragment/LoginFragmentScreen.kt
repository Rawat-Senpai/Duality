package com.example.dualityapplication.ui.loginFragment

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.dualityapplication.R
import com.example.dualityapplication.databinding.FragmentLoginScreenBinding
import com.example.dualityapplication.models.LoginRequestModel
import com.example.dualityapplication.ui.AuthViewModel
import com.example.dualityapplication.utils.BaseUtils
import com.example.dualityapplication.utils.BaseUtils.Companion.showToast
import com.example.dualityapplication.utils.InternetConnection.isNetworkAvailable
import com.example.dualityapplication.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragmentScreen : Fragment() {

    private var _binding:FragmentLoginScreenBinding?= null
    private val binding get() = _binding!!
    private var showPassword = false
    private val viewModel by viewModels<AuthViewModel>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_login_screen, container, false)
        _binding = FragmentLoginScreenBinding.inflate(layoutInflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObservers()
        bindViews()
    }

    private fun bindViews() {
        binding.apply {

            llSignUp.setOnClickListener(){
                findNavController().navigate(R.id.action_loginFragmentScreen_to_registerFragmentScreen)
            }


            forgotPassword.setOnClickListener(){
                findNavController().navigate(R.id.action_loginFragmentScreen_to_forgotFragmentScreen)
            }

            continueBtn.setOnClickListener(){
                loginFunction()
            }



            ivViewPassword.setOnClickListener {
                if (showPassword) {
                    showPassword = false
                    ivViewPassword.setImageResource(R.drawable.ic_show_pass)
                    etPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                } else {
                    showPassword = true
                    ivViewPassword.setImageResource(R.drawable.ic_open_password)
                    etPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                }
                etPassword.setSelection(binding.etPassword.text.length)
            }


            init()


        }
    }

    private fun loginFunction() {

        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email == "") {

            makeAllValidationInvisible()
            BaseUtils.showLabelValidation(
                requireContext(),
                binding.emailValidation,
                "Please Use valid email"
            )
//            openAlert("Please enter email address", false)
            return
        } else if (!email.contains("@") || !email.contains(".com") || !email.contains(".com")) {
            makeAllValidationInvisible()
            BaseUtils.showLabelValidation(
                requireContext(),
                binding.emailValidation,
                "Please Use a valid email"
            )
            return
        } else if (email.length <= 4) {
            makeAllValidationInvisible()
            BaseUtils.showLabelValidation(
                requireContext(),
                binding.emailValidation,
                "Please enter a valid email"
            )
            return
        } else if (password == "") {
            makeAllValidationInvisible()
            BaseUtils.showLabelValidation(
                requireContext(),

                binding.passwordValidation,
                "Please enter password"
            )
            return
        } else {
            makeAllValidationInvisible()
            if (isNetworkAvailable(requireContext())) {

                viewModel.loginUser(LoginRequestModel(email,password))

//                callLogInApi(email, password)
//                progress!!.showProgressBar()
            } else {
                showToast(requireContext(),"Please check your internet connection")
//                openAlert(getString(R.string.please_check_your_internet), false)
            }

        }


    }


    private fun init(){
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                makeAllValidationInvisible()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                makeAllValidationInvisible()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun makeAllValidationInvisible() {
        binding.emailValidation.visibility = View.GONE
        binding.passwordValidation.visibility = View.GONE

    }
    private fun bindObservers() {

        viewModel.userResponseLiveData.observe(requireActivity(), Observer {
            binding.progressBar.isVisible=false
            when(it){
                is NetworkResult.Error -> {
                    Log.d("errorCheck",it.message.toString())
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {

                    binding.progressBar.isVisible= true

                }

                is NetworkResult.Success -> {
                    Log.d("success",it.data.toString())

                }
                null -> {}
            }

        })



    }

}