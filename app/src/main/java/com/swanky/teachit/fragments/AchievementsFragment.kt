package com.swanky.teachit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.swanky.teachit.R
import com.swanky.teachit.databinding.FragmentAchievementsBinding
import com.swanky.teachit.viewmodels.AchievementsViewmodel


class AchievementsFragment : Fragment() {

    private var _viewBinding: FragmentAchievementsBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val viewModel: AchievementsViewmodel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _viewBinding = FragmentAchievementsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

}