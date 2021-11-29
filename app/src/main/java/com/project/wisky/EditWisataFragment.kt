package com.project.wisky

import android.database.sqlite.SQLiteDatabase.deleteDatabase
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.project.wisky.Constants.loadImage
import com.project.wisky.databinding.FragmentDetailWisataBinding
import com.project.wisky.databinding.FragmentEditWisataBinding
import com.project.wisky.model.WisataModel

class EditWisataFragment : Fragment() {

    private var _binding: FragmentEditWisataBinding? = null
    private val binding get() = _binding!!
    private var category = ""
    private var uriImage: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var wisataId = ""
    private var wisataImage = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditWisataBinding.inflate(inflater, container, false)

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.drop_down_wisata_array)
        )
        binding.spinnerCategory.adapter = adapter
        binding.spinnerCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> category = ""
                        1 -> category = "Taman"
                        2 -> category = "Alam"
                        3 -> category = "Agrowisata"
                        4 -> category = "Budaya"
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }

        wisataId = arguments?.getString(Constants.WISATA_ID).toString()
        val wisataName = arguments?.getString(Constants.WISATA_NAME)
        category = arguments?.getString(Constants.WISATA_CATEGORY).toString()
        wisataImage = arguments?.getString(Constants.WISATA_IMAGE).toString()
        val wisataDesc = arguments?.getString(Constants.WISATA_DESC)
        val wisataAddress = arguments?.getString(Constants.WISATA_ADDRESS)
        val wisataContact = arguments?.getString(Constants.WISATA_CONTACT)
        val wisataWorkingHour = arguments?.getString(Constants.WISATA_WORKING_HOUR)

        with(binding){
            etName.setText(wisataName)
            ivAddWisata.loadImage(wisataImage)
            etDesc.setText(wisataDesc)
            etAddress.setText(wisataAddress)
            etContact.setText(wisataContact)
            etWorkingHour.setText(wisataWorkingHour)
        }

        binding.btnUpload.setOnClickListener {
            getResultImage.launch("image/*")
            binding.ivAddWisata.visibility = View.VISIBLE
        }

        binding.ivDelete.setOnClickListener {
            deleteDatabase(wisataId)
            findNavController().navigate(R.id.action_editWisataFragment_to_homeFragment)
            Constants.showSnackbar(
                requireContext(),
                binding.container,
                "Wisata berhasil dihapus"
            )
        }

        binding.btnEditWisata.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            if (binding.etName.text.toString().trim().isEmpty()) {
                binding.etName.error = "Masukkan nama wisata"
                binding.etName.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if (binding.etDesc.text.toString().trim().isEmpty()) {
                binding.etDesc.error = "Masukkan deskripsi wisata"
                binding.etDesc.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if (binding.etAddress.text.toString().trim().isEmpty()) {
                binding.etAddress.error = "Masukkan alamat wisata"
                binding.etAddress.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if (binding.etWorkingHour.text.toString().trim().isEmpty()) {
                binding.etWorkingHour.error = "Masukkan jam operasional"
                binding.etWorkingHour.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if (binding.etContact.text.toString().trim().isEmpty()) {
                binding.etContact.error = "Masukkan jam operasional"
                binding.etContact.requestFocus()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if (category == "") {
                Constants.showSnackbar(
                    requireContext(),
                    binding.container,
                    "Silakan pilih kategori"
                )
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if (binding.ivAddWisata.visibility == View.GONE){
                Constants.showSnackbar(
                    requireContext(),
                    binding.container,
                    "Silakan upload gambar"
                )
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            uploadPicture(uriImage)
        }

        return binding.root
    }

    private fun storeToDatabase(urlImage: String) {
        //simpan di database
        val reference = FirebaseDatabase.getInstance().reference.child("ListWisata")
            .child(wisataId)

        val value = WisataModel(
            wisataId = wisataId,
            wisataName = binding.etName.text.toString().trim(),
            wisataDesc = binding.etDesc.text.toString().trim(),
            wisataAddress = binding.etAddress.text.toString().trim(),
            wisataWorkingHour = binding.etWorkingHour.text.toString().trim(),
            wisataContact = binding.etContact.text.toString().trim(),
            wisataCategory = category,
            wisataImage = urlImage
        )

        reference.setValue(value).addOnCompleteListener {
            if (it.isSuccessful) {
                binding.progressBar.visibility = View.INVISIBLE
                findNavController().navigate(R.id.action_editWisataFragment_to_homeFragment)
                Constants.showSnackbar(
                    requireContext(),
                    binding.container,
                    "Wisata berhasil diubah"
                )
            } else {
                Constants.showSnackbar(requireContext(), binding.container, "Error from database")
                binding.progressBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun uploadPicture(
        imageURI: Uri?
    ) {
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        val tmp: StorageReference =
            storageReference.child("ListWisata/$wisataId.jpg")
        if (imageURI != null) {
            tmp.putFile(imageURI).addOnSuccessListener {
                tmp.downloadUrl.addOnSuccessListener {
                    val urlImage = it.toString()
                    //store to database
                    storeToDatabase(urlImage)
                }
            }.addOnFailureListener {
                Constants.showSnackbar(requireContext(), binding.container, "Error upload image")
            }
        } else {
            //kalo foto wisata gadiubah, maka url nya pake yang sama kaya didatabase
            storeToDatabase(wisataImage)
            Constants.showSnackbar(requireContext(), binding.container, "Error upload image")
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private val getResultImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        // Handle the returned Uri
        uriImage = it
        if (uriImage!==null){
            binding.ivAddWisata.setImageURI(uriImage)
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun deleteDatabase(wisataId: String){
        val reference = FirebaseDatabase.getInstance().reference
            .child("ListWisata").child(wisataId)
        reference.removeValue()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}