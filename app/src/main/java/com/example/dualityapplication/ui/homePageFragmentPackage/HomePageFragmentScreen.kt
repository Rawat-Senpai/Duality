package com.example.dualityapplication.ui.homePageFragmentPackage

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.navigation.fragment.findNavController
import com.example.dualityapplication.R
import com.example.dualityapplication.databinding.FragmentHomePageScreenBinding
import com.example.dualityapplication.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomePageFragmentScreen : Fragment() {


    private  var _binding :FragmentHomePageScreenBinding?= null
    private  val binding get() = _binding!!
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home_page_screen, container, false)

        _binding = FragmentHomePageScreenBinding.inflate(layoutInflater,container,false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      bindViews()

    }

    private fun bindViews() {
        binding.apply {
            continueBtn.setOnClickListener(){
                tokenManager.saveToken("")
                findNavController().navigate(R.id.action_homePageFragmentScreen_to_loginFragmentScreen)

//                findNavController().popBackStack(R.id.loginFragmentScreen,true)
            }
        }
    }


}