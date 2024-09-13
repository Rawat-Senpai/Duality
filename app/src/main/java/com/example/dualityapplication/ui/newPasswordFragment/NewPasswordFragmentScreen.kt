package com.example.dualityapplication.ui.newPasswordFragment

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
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.dualityapplication.R
import com.example.dualityapplication.databinding.FragmentNewPasswordScreenBinding
import com.example.dualityapplication.models.ChangePasswordRequestModel
import com.example.dualityapplication.models.ForgotPasswordRequestModel
import com.example.dualityapplication.ui.AuthViewModel
import com.example.dualityapplication.utils.BaseUtils
import com.example.dualityapplication.utils.BaseUtils.Companion.showToast
import com.example.dualityapplication.utils.InternetConnection.isNetworkAvailable
import com.example.dualityapplication.utils.NetworkResult
import com.example.dualityapplication.utils.PasswordStrength
import com.example.dualityapplication.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import java.util.regex.Pattern
import javax.inject.Inject


@AndroidEntryPoint
class NewPasswordFragmentScreen : Fragment() {

    private var _binding: FragmentNewPasswordScreenBinding? = null
    private val binding get() = _binding!!
    var otp = ""
    private var strengthText = ""

    private var showPasswordCreate = false
    private var showPasswordConf = false

    private val viewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_new_password_screen, container, false)

        _binding = FragmentNewPasswordScreenBinding.inflate(layoutInflater, container, false)

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        otp = arguments?.getString("otp").toString()


        bindViews()
        bindObservers()


    }

    private fun bindObservers() {

        viewModel.changePasswordLiveData.observe(requireActivity(), Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Error -> {
                    Log.d("errorCheck", it.message.toString())
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {

                    binding.progressBar.isVisible = true

                }

                is NetworkResult.Success -> {
                    Log.d("success", it.data.toString())
                    findNavController().navigate(R.id.action_newPasswordFragmentScreen_to_passwordUpdatedFragmentScreen)

                }

                null -> {}
            }

        })


    }

    private fun bindViews() {
        binding.apply {

            backBtn.setOnClickListener(){
                findNavController().popBackStack()
            }

            ivViewPassword.setOnClickListener() {
                if (showPasswordCreate) {
                    showPasswordCreate = false
                    binding.ivViewPassword.setImageResource(R.drawable.ic_open_password)
                    binding.etPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                } else {
                    showPasswordCreate = true
                    binding.ivViewPassword.setImageResource(R.drawable.ic_show_pass)
                    binding.etPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                }
                binding.etPassword.setSelection(binding.etPassword.text.length)

            }

            ivViewConfPassword.setOnClickListener() {
                if (showPasswordConf) {
                    showPasswordConf = false
                    binding.ivViewConfPassword.setImageResource(R.drawable.ic_open_password)
                    binding.etConfirmPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                } else {
                    showPasswordConf = true
                    binding.ivViewConfPassword.setImageResource(R.drawable.ic_show_pass)
                    binding.etConfirmPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                }
                binding.etConfirmPassword.setSelection(binding.etConfirmPassword.text.length)
            }


            etConfirmPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    makeAllValidationInvisible()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            etPassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(editable: Editable?) {
                    val password = editable.toString()
                    val strength = calculatePasswordStrength(password)
                    displayPasswordStrength(strength)
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })

            etConfirmPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    makeAllValidationInvisible()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            continueBtn.setOnClickListener() {
                validationFunction()
            }


        }
    }

    private fun displayPasswordStrength(strength: PasswordStrength) {
        strengthText = when (strength) {
            PasswordStrength.WEAK -> {
                binding.passwordStrength.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
                BaseUtils.weak

            }

            PasswordStrength.MEDIUM -> {
                binding.passwordStrength.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.yellow
                    )
                )
                BaseUtils.medium

            }

            PasswordStrength.STRONG -> {
                binding.passwordStrength.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
                BaseUtils.strong

            }
        }
        binding.passwordStrength.text = "Password Strength: $strengthText"
    }

    private fun validationFunction() {
        val password = binding.etPassword.text.trim().toString()
        val confirmPass = binding.etConfirmPassword.text.trim().toString()

        if (password == "") {
            makeAllValidationInvisible()
            BaseUtils.showLabelValidation(
                requireContext(),
                binding.passwordValidation,
                "Please enter a strong password"
            )
            return
        } else if (confirmPass == "") {
            makeAllValidationInvisible()
            BaseUtils.showLabelValidation(
                requireContext(),
                binding.confirmPassValidation,
                "Please enter a Strong password"
            )
            return
        } else if (password.length < 7) {
            makeAllValidationInvisible()
            BaseUtils.showLabelValidation(
                requireContext(),
                binding.confirmPassValidation,
                "Password and Confirm password doesn't match"
            )
            return
        } else if (strengthText == BaseUtils.weak) {
            makeAllValidationInvisible()
            showToast(
                requireContext(),
                "Password must contain at least 1 uppercase character, i lowercase character, 1 special character and 1 number"
            )
//            openAlert(getString(R.string.password_instruction),false)
        } else if (password != confirmPass) {
            makeAllValidationInvisible()
            BaseUtils.showLabelValidation(
                requireContext(),
                binding.confirmPassValidation,
                "Password and Confirm password doesn't match"
            )
            return
        } else {

            if (isNetworkAvailable(requireContext())) {
                viewModel.changePassword(
                    ChangePasswordRequestModel(confirmPass, password, otp),
                    tokenManager.getId().toString()
                )
            } else {
                showToast(requireContext(), "Please check your internet connection")
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


    private fun makeAllValidationInvisible() {
        binding.passwordValidation.visibility = View.GONE
        binding.confirmPassValidation.visibility = View.GONE
    }

}