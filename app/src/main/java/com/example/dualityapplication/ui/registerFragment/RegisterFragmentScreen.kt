package com.example.dualityapplication.ui.registerFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.dualityapplication.R
import com.example.dualityapplication.databinding.FragmentRegisterScreenBinding
import com.example.dualityapplication.models.SignUpRequestModel
import com.example.dualityapplication.ui.AuthViewModel
import com.example.dualityapplication.utils.BaseUtils
import com.example.dualityapplication.utils.BaseUtils.Companion.showToast
import com.example.dualityapplication.utils.InternetConnection.isNetworkAvailable
import com.example.dualityapplication.utils.NetworkResult
import com.example.dualityapplication.utils.PasswordStrength
import com.example.dualityapplication.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragmentScreen : Fragment() {

    private var _binding:FragmentRegisterScreenBinding?= null
    private var strengthText = ""
     val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()
    @Inject
    lateinit var tokenManager: TokenManager



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_register_screen, container, false)

        _binding = FragmentRegisterScreenBinding.inflate(layoutInflater,container,false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObservers()
        bindViews()

    }

    private fun bindObservers() {

        viewModel.userSignUpLiveData.observe(requireActivity(), Observer {
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
//                    showToast(requireContext(),"success")
                    tokenManager.saveToken(it.data?.data.toString())
                    findNavController().navigate(R.id.action_registerFragmentScreen_to_profileFragmentScreen)
                    Log.d("success",it.data.toString())

                }
                null -> {}
            }

        })

    }

    private fun bindViews() {
        binding.apply {

            icon.setOnClickListener(){
                findNavController().navigate(R.id.action_registerFragmentScreen_to_profileFragmentScreen)
            }

            loginBtn.setOnClickListener(){
                findNavController().popBackStack()
            }

            continueBtn.setOnClickListener(){
                signUpValidation()
            }

            etPassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(editable: Editable?) {
                    val password = editable.toString()
                    val strength = calculatePasswordStrength(password)
                    displayPasswordStrength(strength)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    makeAllValidationInvisible()
                }
            })

            etConfirmPassword.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    makeAllValidationInvisible()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            etEmail.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    makeAllValidationInvisible()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

        }
    }

    private fun signUpValidation() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPass = binding.etConfirmPassword.text.toString().trim()
//        val validName=containsSpecialCharacters(name)

      if (email.length <= 5) {
            makeAllValidationInvisible()
            BaseUtils.showLabelValidation(
                requireContext(),
                binding.emailValidation,
                "Please enter email address"
            )
            return
        } else if (!email.contains("@") || !email.contains(".com") || !email.endsWith(".com")) {
            makeAllValidationInvisible()
            BaseUtils.showLabelValidation(
                requireContext(),
                binding.emailValidation,
                "Please enter a valid email address"
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
        }else if (strengthText == BaseUtils.weak) {
            makeAllValidationInvisible()
          showToast(requireContext(),"Please enter a Strong password")
            return
        } else if (confirmPass == "") {
            makeAllValidationInvisible()
            BaseUtils.showLabelValidation(
                requireContext(),
                binding.confirmPassValidation,
                "Please enter confirm password"
            )
            return
        } else if (password != confirmPass) {
            makeAllValidationInvisible()
            BaseUtils.showLabelValidation(
                requireContext(),
                binding.confirmPassValidation,
                "Password and Confirm password doesn't match"
            )
            return
        } else if (!binding.licheckbox.isChecked) {
            makeAllValidationInvisible()
          showToast(requireContext(),"Please accept the Terms and Conditions ")
            return
        } else {
            if (isNetworkAvailable(requireContext())) {

                viewModel.signUpUser(SignUpRequestModel(email,password,confirmPass))
            } else {
                showToast(requireContext(),"Please check your internet connection")
//                openAlert(getString(R.string.please_check_your_internet),false)
            }
        }
    }



    private fun calculatePasswordStrength(password: String): PasswordStrength {
        val specialCharPattern = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>?]")
        val capitalLetterPattern = Pattern.compile("[A-Z]")
        val numberPattern = Pattern.compile("[0-9]")

        val hasSpecialChar = specialCharPattern.matcher(password).find()
        val hasCapitalLetter = capitalLetterPattern.matcher(password).find()
        val hasNumber = numberPattern.matcher(password).find()


        val length = if (password.length > 7) {
            1
        } else {
            0
        }
        val hasSpecialCharInt = if (hasSpecialChar) {
            1
        } else {
            0
        }

        val hasCapitalLetterInt = if (hasCapitalLetter) {
            1
        } else {
            0
        }

        val hasNumberInt = if (hasNumber) {
            1
        } else {
            0
        }

        val finalStrength = hasSpecialCharInt + hasCapitalLetterInt + hasNumberInt + length


        Log.d("checkingStrength", finalStrength.toString())
        val strength = when {
            finalStrength < 2 -> PasswordStrength.WEAK
            finalStrength <= 3 -> PasswordStrength.MEDIUM
            else -> PasswordStrength.STRONG
        }

        return strength
    }

    private fun displayPasswordStrength(strength: PasswordStrength) {
        strengthText = when (strength) {
            PasswordStrength.WEAK -> {
                binding.passwordStrength.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                BaseUtils.weak
            }
            PasswordStrength.MEDIUM -> {
                binding.passwordStrength.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))
                BaseUtils.medium
            }
            PasswordStrength.STRONG -> {
                binding.passwordStrength.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                BaseUtils.strong
            }
        }
        binding.passwordStrength.visibility = View.VISIBLE
        binding.passwordStrength.text = "Password Strength: $strengthText"
    }

    private fun makeAllValidationInvisible() {
        binding.emailValidation.visibility = View.GONE
        binding.passwordValidation.visibility = View.GONE
        binding.confirmPassValidation.visibility = View.GONE
    }

}