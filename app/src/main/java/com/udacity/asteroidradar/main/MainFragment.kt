package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val factory = MainViewModelFactory(requireActivity().application)
        ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val adapter = AsteroidRecyclerAdapter(Listener{
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        })

        binding.asteroidRecycler.adapter = adapter

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(when(item.itemId) {
            R.id.show_saved_menu -> FilterStatus.SAVED
            R.id.show_today_menu -> FilterStatus.TODAY
            else -> FilterStatus.WEEK
        })
        return true
    }
}
