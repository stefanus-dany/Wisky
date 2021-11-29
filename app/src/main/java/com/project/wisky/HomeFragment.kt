package com.project.wisky

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.wisky.adapter.ListWisataAdapter
import com.project.wisky.databinding.FragmentHomeBinding
import com.project.wisky.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var listWisataAdapter: ListWisataAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[HomeViewModel::class.java]

        listWisataAdapter = ListWisataAdapter()
        with(binding.recyclerView) {
            layoutManager =
                GridLayoutManager(requireContext(), 2)
//            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@HomeFragment.listWisataAdapter
        }

        observeData()

        binding.btnAddWisata.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addWisataFragment)
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData() {
        viewModel.getDataWisata().observe(viewLifecycleOwner) {
            with(listWisataAdapter) {
                setdata(it)
                binding.progressBar.visibility = View.GONE
                notifyDataSetChanged()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}