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
            adapter = this@HomeFragment.listWisataAdapter
        }

        observeData()

//        val listWisata = arrayListOf<WisataModel>()
//        listWisata.add(
//            WisataModel(
//            wisataId = "0",
//                wisataName = "Tempat wisata A",
//                wisataDesc = "Ini merupakan sebuah tempat wisata yang terletak di Kota Malang",
//                wisataAddress = "Jalan Kerto Rahayu di sebelah Indomaret",
//                wisataWorkingHour = "10.00 - 12.00",
//                wisataContact = "081394559813",
//                wisataCategory = "Alam",
//                wisataImage = "https://img.idxchannel.com/media/700/images/idx/2021/08/09/1628434191140.jpg"
//        ))
//
//        listWisata.add(
//            WisataModel(
//                wisataId = "1",
//                wisataName = "Tempat wisata B",
//                wisataDesc = "Ini merupakan sebuah tempat wisata yang terletak di Kota Malang",
//                wisataAddress = "Jalan Kerto Rahayu di sebelah Indomaret",
//                wisataWorkingHour = "10.00 - 12.00",
//                wisataContact = "081394559813",
//                wisataCategory = "Alam",
//                wisataImage = "https://img.idxchannel.com/media/700/images/idx/2021/08/09/1628434191140.jpg"
//            ))
//
//        listWisata.add(
//            WisataModel(
//                wisataId = "2",
//                wisataName = "Tempat wisata C",
//                wisataDesc = "Ini merupakan sebuah tempat wisata yang terletak di Kota Malang",
//                wisataAddress = "Jalan Kerto Rahayu di sebelah Indomaret",
//                wisataWorkingHour = "10.00 - 12.00",
//                wisataContact = "081394559813",
//                wisataCategory = "Alam",
//                wisataImage = "https://img.idxchannel.com/media/700/images/idx/2021/08/09/1628434191140.jpg"
//            ))
//
//        listWisata.add(
//            WisataModel(
//                wisataId = "3",
//                wisataName = "Tempat wisata D",
//                wisataDesc = "Ini merupakan sebuah tempat wisata yang terletak di Kota Malang",
//                wisataAddress = "Jalan Kerto Rahayu di sebelah Indomaret",
//                wisataWorkingHour = "10.00 - 12.00",
//                wisataContact = "081394559813",
//                wisataCategory = "Alam",
//                wisataImage = "https://img.idxchannel.com/media/700/images/idx/2021/08/09/1628434191140.jpg"
//            ))
//        listWisataAdapter.setdata(listWisata)

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