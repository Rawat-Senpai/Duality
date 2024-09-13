package com.example.dualityapplication.ui.forgotFragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.dualityapplication.R
import com.example.dualityapplication.databinding.DialogOtpPopupBinding
import com.example.dualityapplication.databinding.FragmentForgotScreenBinding
import com.example.dualityapplication.models.ForgotPasswordRequestModel
import com.example.dualityapplication.models.ForgotPasswordResponseModel
import com.example.dualityapplication.ui.AuthViewModel
import com.example.dualityapplication.utils.BaseUtils
import com.example.dualityapplication.utils.BaseUtils.Companion.showToast
import com.example.dualityapplication.utils.InternetConnection.isNetworkAvailable
import com.example.dualityapplication.utils.NetworkResult
import com.example.dualityapplication.utils.TokenManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ForgotFragmentScreen : Fragment() {


    private var _binding: FragmentForgotScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_forgot_screen, container, false)

        _binding = FragmentForgotScreenBinding.inflate(layoutInflater, container, false)

        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()

        bindObservers()

    }

    private fun bindObservers() {

        viewModel.userForgotPasswordLiveData.observe(requireActivity(), Observer {
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
                    tokenManager.saveId(it.data?.data?._id.toString())
                    openAlert(it.data)

                }

                null -> {}
            }

        })


    }

    private fun openAlert(data: ForgotPasswordResponseModel?) {
        val dialogBinding = DialogOtpPopupBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        builder.setView(dialogBinding.root)

        val dialog = builder.create()
        // Handle button click inside the dialog
        // Show the custom dialog
        dialog.show()
        // Optionally: Set the dialog window's background to transparent (if needed)
        // Convert DP to Pixels
        val heightInDp = 350 // Desired height in DP
        val heightInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, heightInDp.toFloat(), resources.displayMetrics
        ).toInt()

        // Ensure the dialog width wraps content and height is set to 300dp
        dialog.window?.apply {
            setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,  // Width wraps content
                heightInPx  // Set fixed height
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }

        dialogBinding.apply {
            val job = CoroutineScope(Dispatchers.Main).launch {
                // Set the first OTP digit after a 1-second delay
                delay(1000)
                dialogBinding.otp1.setText(data?.data?.otp?.get(0)?.toString())

                // Set the second OTP digit after another 1-second delay
                delay(1000)
                dialogBinding.otp2.setText(data?.data?.otp?.get(1)?.toString())

                // Set the third OTP digit after another 1-second delay
                delay(1000)
                dialogBinding.otp3.setText(data?.data?.otp?.get(2)?.toString())

                // Set the fourth OTP digit after another 1-second delay
                delay(1000)
                dialogBinding.otp4.setText(data?.data?.otp?.get(3)?.toString())

                dialog.dismiss()

                val bundle = Bundle()
                bundle.putString("otp", data?.data?.otp  )
                findNavController().navigate(R.id.action_forgotFragmentScreen_to_newPasswordFragmentScreen, bundle)

            }




        }
    }

    private fun bindViews() {

        binding.apply {

            continueBtn.setOnClickListener() {
                checkValidation()
            }

        }

    }

    private fun checkValidation() {

        val email = binding.etEmail.text.toString().trim()

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
        } else {
            if (isNetworkAvailable(requireContext())) {
                viewModel.forgotPassword(ForgotPasswordRequestModel(email))
            } else {
                showToast(requireContext(), "Please check you internet connection")
            }

        }

    }

    private fun makeAllValidationInvisible() {
        binding.emailValidation.visibility = View.GONE
    }

}