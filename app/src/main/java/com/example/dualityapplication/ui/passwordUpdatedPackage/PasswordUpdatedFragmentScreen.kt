package com.example.dualityapplication.ui.passwordUpdatedPackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.dualityapplication.R
import com.example.dualityapplication.databinding.FragmentPasswordUpdatedScreenBinding

class PasswordUpdatedFragmentScreen : Fragment() {

    private var _binding :FragmentPasswordUpdatedScreenBinding?= null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_password_updated_screen, container, false)

        _binding = FragmentPasswordUpdatedScreenBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            continueBtn.setOnClickListener(){
//                findNavController().popBackStack()
                findNavController().navigate(R.id.action_passwordUpdatedFragmentScreen_to_loginFragmentScreen)
            }
        }
    }


}