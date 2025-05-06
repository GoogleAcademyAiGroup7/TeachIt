package com.swanky.teachit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.swanky.teachit.R
import com.swanky.teachit.databinding.FragmentTopicsBinding
import com.swanky.teachit.viewmodels.TopicsViewModel


class TopicsFragment : Fragment() {

    private var _dataBinding: FragmentTopicsBinding? = null
    private val dataBinding get() = _dataBinding!!
    private val viewModel: TopicsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_topics, container, false)
        dataBinding.lifecycleOwner = this
        dataBinding.master = this
        return dataBinding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _dataBinding = null
    }

}