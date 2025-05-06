package com.swanky.teachit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.swanky.teachit.R
import com.swanky.teachit.databinding.FragmentHomeBinding
import com.swanky.teachit.viewmodels.HomePageViewmodel


class HomeFragment : Fragment() {

    private var _viewBinding: FragmentHomeBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val viewModel: HomePageViewmodel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Use a helper function to handle navigation
        setNavigation(viewBinding.myTopicsCardView, HomeFragmentDirections.actionHomeFragmentToTopicsFragment())
        setNavigation(viewBinding.myTestsCardView, HomeFragmentDirections.actionHomeFragmentToTestsFragment())
        setNavigation(viewBinding.achievementsCardView, HomeFragmentDirections.actionHomeFragmentToAchievementsFragment())
    }


    // Helper function to reduce repetitive code
    private fun setNavigation(view: View, action: NavDirections) {
        view.setOnClickListener {
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

}