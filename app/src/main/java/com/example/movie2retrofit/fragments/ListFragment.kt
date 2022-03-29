package com.example.movie2retrofit.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movie2retrofit.R
import com.example.movie2retrofit.adapter.movieAdapter
import com.example.movie2retrofit.api.ApiInterface
import com.example.movie2retrofit.databinding.FragmentListBinding
import com.example.movie2retrofit.model.Movies
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListFragment : Fragment(), movieAdapter.ItemClickListener {
    lateinit var binding: FragmentListBinding
    private val movieAdapter by lazy { movieAdapter(requireContext(), this) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentListBinding.inflate(inflater, container, false)

        val apiInterface = ApiInterface.create().getMovies("67ff61f491eeb461f0f90226a984be92")
        apiInterface.enqueue(object : Callback<Movies> {
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                binding.recyclerView.adapter = movieAdapter
                response.body()?.results?.let { movieAdapter.setList(it) }
                binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)


            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Log.d("test", "onFailure:${t.message}")
            }

        })

        return binding.root
    }

    override fun onItemClick(id: Int) {
        val bundle = Bundle()
        bundle.putInt("key", id)
        val fragment = DetailsFragment()
        fragment.arguments = bundle
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.nav_container, fragment)
            ?.addToBackStack(null)?.commit()
    }


}