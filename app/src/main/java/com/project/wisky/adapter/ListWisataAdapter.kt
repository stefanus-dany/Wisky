package com.project.wisky.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.project.wisky.Constants
import com.project.wisky.Constants.loadImage
import com.project.wisky.R
import com.project.wisky.databinding.ItemWisataBinding
import com.project.wisky.model.WisataModel

class ListWisataAdapter :
    RecyclerView.Adapter<ListWisataAdapter.WisataViewHolder>() {
    private var dataWisata = ArrayList<WisataModel>()

    fun setdata(data: List<WisataModel>?) {
        if (data == null) return
        this.dataWisata.clear()
        this.dataWisata.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WisataViewHolder {
        val itemWisata =
            ItemWisataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WisataViewHolder(itemWisata)
    }

    override fun onBindViewHolder(holder: WisataViewHolder, position: Int) {
        val data = dataWisata[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = dataWisata.size

    class WisataViewHolder(private val binding: ItemWisataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: WisataModel) {
            with(binding) {
                ivWisata.loadImage(data.wisataImage)
                tvTitleWisata.text = data.wisataName
            }
            val mBundle = Bundle()
            mBundle.putString(Constants.WISATA_ID, data.wisataId)
            mBundle.putString(Constants.WISATA_NAME, data.wisataName)
            mBundle.putString(Constants.WISATA_DESC, data.wisataDesc)
            mBundle.putString(Constants.WISATA_ADDRESS, data.wisataAddress)
            mBundle.putString(Constants.WISATA_WORKING_HOUR, data.wisataWorkingHour)
            mBundle.putString(Constants.WISATA_CONTACT, data.wisataContact)
            mBundle.putString(Constants.WISATA_CATEGORY, data.wisataCategory)
            mBundle.putString(Constants.WISATA_IMAGE, data.wisataImage)

            itemView.setOnClickListener {
                it.findNavController().navigate(R.id.action_homeFragment_to_detailWisataFragment, mBundle)
            }
            binding.ivEdit.setOnClickListener {
                it.findNavController().navigate(R.id.action_homeFragment_to_editWisataFragment, mBundle)
            }
        }
    }
}