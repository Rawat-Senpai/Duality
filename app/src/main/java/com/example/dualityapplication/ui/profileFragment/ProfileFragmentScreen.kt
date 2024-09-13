package com.example.dualityapplication.ui.profileFragment

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.dualityapplication.R
import com.example.dualityapplication.databinding.FragmentProfileScreenBinding
import com.example.dualityapplication.models.UserProfileRequestModel
import com.example.dualityapplication.ui.AuthViewModel
import com.example.dualityapplication.ui.UserViewModel
import com.example.dualityapplication.utils.BaseUtils
import com.example.dualityapplication.utils.BaseUtils.Companion.showToast
import com.example.dualityapplication.utils.InternetConnection.isNetworkAvailable
import com.example.dualityapplication.utils.NetworkResult
import com.example.dualityapplication.utils.Util
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragmentScreen : Fragment() {

    private var _binding: FragmentProfileScreenBinding? = null
    private val binding get() = _binding!!
    private var gender =""

//    private val viewModel by viewModels<AuthViewModel>()


    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>

    private var commonImageUri: Uri? = null
    private var imageUrl: String = "";

    private val viewModel by viewModels<UserViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile_screen, container, false)

        _binding = FragmentProfileScreenBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {

                    commonImageUri?.let { uri ->

                        binding.imageView.setImageURI(uri)
                    }
                } else {
                    Toast.makeText(requireContext(), "photo capture failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            Log.d("Checking", "selectedPic")

            if (uri != null) {
                commonImageUri = uri
                binding.imageView.setImageURI(commonImageUri!!)
            }

        }


        bindViews()
        bindObservers()

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
                    showToast(requireContext(),"success")

                    findNavController().navigate(R.id.action_profileFragmentScreen_to_homePageFragmentScreen)
                    Log.d("success",it.data.toString())

                }
                null -> {}
            }

        })


    }

    private fun bindViews() {
        binding.apply {

            imageView.setOnClickListener {
                chooseImage(requireActivity())
            }


            genderSelection.setOnClickListener(){
                if (genderSelectionLayout.visibility == View.GONE) {
                    genderSelectionLayout.visibility = View.VISIBLE
                } else {
                    genderSelectionLayout.visibility = View.GONE
                }
            }

            tvMale.setOnClickListener {
                gender = Util.MALE
                etGender.text = tvMale.hint.toString()
                genderSelectionLayout.visibility = View.GONE
            }

            // Handle female option selection
            tvFemale.setOnClickListener {
                gender = Util.FEMALE
                etGender.text = tvFemale.hint.toString()
                genderSelectionLayout.visibility = View.GONE
            }

            // Handle others option selection
            tvOthers.setOnClickListener {
                gender = Util.OTHERS
                etGender.text = tvOthers.hint.toString()
                genderSelectionLayout.visibility = View.GONE
            }

            continueBtn.setOnClickListener(){
                checkValidation()
            }


        }

    }

    private fun checkValidation() {
        val name = binding.etName.text.toString().trim()
        val gender = gender
        val age = binding.etAge.text.toString().trim()
        val bio = binding.etBio.text.toString().trim()


        if (name == "") {
            makeAllValidationInvisible()
            BaseUtils.showLabelValidation(requireContext(), binding.nameValidation, "Please enter name")
            return
        }
        else if (name.length < 3) {
            makeAllValidationInvisible()
            BaseUtils.showLabelValidation(requireContext(), binding.nameValidation, "Please enter a valid name")
            return
        } else if (gender == "") {
            makeAllValidationInvisible()
            BaseUtils.showLabelValidation(
                requireContext(),
                binding.genderValidation,
                "Please select your gender "
            )
            return
        }
        else if (age == "") {
            makeAllValidationInvisible()
            BaseUtils.showLabelValidation(
                requireContext(),
                binding.ageValidation,
                "Please select your age "
            )
            return
        }
        else if (bio == "") {
            makeAllValidationInvisible()
            BaseUtils.showLabelValidation(
                requireContext(),
                binding.bioValidation,
                "Please add something about your bio "
            )
            return
        }
        else {
            if(isNetworkAvailable(requireContext())){
                viewModel.loginUser(UserProfileRequestModel(age,bio, name,gender))
            } else {

                showToast(requireContext(),"Please check your internet connection ")

            }
        }
    }

    private fun chooseImage(context: Context) {
        val optionsMenu = arrayOf<CharSequence>(
            "Take Photo",
            "Choose from Gallery",
            "Exit"
        )
        val builder = AlertDialog.Builder(context)

        builder.setItems(
            optionsMenu
        ) { dialogInterface, i ->
            if (optionsMenu[i] == "Take Photo") {

                if (Util.checkPermission(requireActivity(), Manifest.permission.CAMERA)) {
                    takeImageFromCameraUri()
                } else {
                    Util.requestPermission(
                        requireActivity(),
                        Manifest.permission.CAMERA,
                        Util.CAMERA_PERMISSION_CODE
                    )
                }

            } else if (optionsMenu[i] == "Choose from Gallery") {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    imageChooser()
                } else {
                    if (Util.checkPermission(
                            requireActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ) {
                        imageChooser()
                    } else {
                        Util.requestPermission(
                            requireActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Util.STORAGE_PERMISSION_CODE
                        )
                    }
                }

            } else if (optionsMenu[i] == "Exit") {
                dialogInterface.dismiss()
            }
        }
        builder.show()
    }


    private fun makeAllValidationInvisible() {
        binding.nameValidation.visibility = View.GONE
        binding.genderValidation.visibility = View.GONE
        binding.ageValidation.visibility = View.GONE
        binding.bioValidation.visibility = View.GONE
    }
    private fun takeImageFromCameraUri() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "MyPicture")
        values.put(
            MediaStore.Images.Media.DESCRIPTION,
            "Photo taken on " + System.currentTimeMillis()
        )
        commonImageUri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        cameraLauncher.launch(commonImageUri)
    }

    private fun imageChooser() {
        galleryLauncher.launch("image/*")
    }


}