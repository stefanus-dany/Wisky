package com.project.wisky

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.wisky.Constants.loadImage
import com.project.wisky.databinding.FragmentDetailWisataBinding

class DetailWisataFragment : Fragment() {

    private var _binding: FragmentDetailWisataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailWisataBinding.inflate(inflater)

        //supaya edit text gabisa diklik
        binding.etDesc.keyListener = null
        binding.etAddress.keyListener = null
        binding.etContact.keyListener = null
        binding.etWorkingHour.keyListener = null

        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        val wisataName = arguments?.getString(Constants.WISATA_NAME)
        val wisataImage = arguments?.getString(Constants.WISATA_IMAGE)
        val wisataDesc = arguments?.getString(Constants.WISATA_DESC)
        val wisataAddress = arguments?.getString(Constants.WISATA_ADDRESS)
        val wisataContact = arguments?.getString(Constants.WISATA_CONTACT)
        val wisataWorkingHour = arguments?.getString(Constants.WISATA_WORKING_HOUR)

        with(binding){
            tvTitleWisata.text = wisataName
            ivAddWisata.loadImage(wisataImage)
            etDesc.setText(wisataDesc)
            etAddress.setText(wisataAddress)
            etContact.setText(wisataContact)
            etWorkingHour.setText(wisataWorkingHour)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}