package com.example.movie2retrofit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movie2retrofit.R
import com.example.movie2retrofit.databinding.FragmentRootBinding


class RootFragment : Fragment() {
    lateinit var binding: FragmentRootBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =  FragmentRootBinding.inflate(inflater, container, false)
        return binding.root
    }


}