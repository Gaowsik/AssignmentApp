package com.example.assignmentapp.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.assignmentapp.R
import com.example.assignmentapp.databinding.FragmentAllNewsBinding
import com.example.assignmentapp.presentation.core.BaseFragment


class AllNewsFragment : BaseFragment() {

    private lateinit var binding: FragmentAllNewsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

}