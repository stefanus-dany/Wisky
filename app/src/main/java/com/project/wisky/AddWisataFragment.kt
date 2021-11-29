package com.project.wisky

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.project.wisky.databinding.FragmentAddWisataBinding
import com.project.wisky.model.WisataModel

class AddWisataFragment : Fragment() {

    private var _binding: FragmentAddWisataBinding? = null
    private val binding get() = _binding!!
    private var alphabet: List<Char> = emptyList()
    private var idWisata: String
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var category = ""
    private var uriImage: Uri? = null

    init {
        alphabet = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        idWisata = List(20) { alphabet.random() }.joinToString("")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddWisataBinding.inflate(inflater)

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            resources.getStringArray(com.project.wisky.R.array.drop_down_wisata_array)
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

        binding.btnUpload.setOnClickListener {
            getResultImage.launch("image/*")
            binding.ivAddWisata.visibility = View.VISIBLE
        }

        binding.btnAddWisata.setOnClickListener {
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

    private fun uploadPicture(
        imageURI: Uri?
    ) {
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        val tmp: StorageReference =
            storageReference.child("ListWisata/$idWisata.jpg")
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
        }
    }

    private fun storeToDatabase(urlImage: String) {
        //simpan di database
        val reference = FirebaseDatabase.getInstance().reference.child("ListWisata")
            .child(idWisata)

        val value = WisataModel(
            wisataId = idWisata,
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
                findNavController().navigate(com.project.wisky.R.id.action_addWisataFragment_to_homeFragment)
                Constants.showSnackbar(
                    requireContext(),
                    binding.container,
                    "Wisata berhasil ditambahkan"
                )
            } else {
                Constants.showSnackbar(requireContext(), binding.container, "Error from database")
                binding.progressBar.visibility = View.INVISIBLE
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        uriImage = data?.data
//        if (uriImage !== null) {
//            binding.ivTambahProduk.scaleType = ImageView.ScaleType.FIT_XY
//            binding.ivTambahProduk.setImageURI(uriImage)
//        }
//    }

    private val getResultImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        // Handle the returned Uri
        uriImage = it
        if (uriImage!==null){
            binding.ivAddWisata.setImageURI(uriImage)
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            Constants.showSnackbar(requireContext(), binding.container, "Terjadi kesalahan")
        }
    }
//    val getResultImage =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            uriImage = it.data?.extras?.get("data") as Uri
//            if (uriImage !== null) {
////            binding.ivAddWisata.scaleType = ImageView.ScaleType.FIT_XY
//                binding.ivAddWisata.adjustViewBounds = true
//                binding.ivAddWisata.setImageURI(uriImage)
//            }
//        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}