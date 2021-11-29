package com.project.wisky.authentication

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.project.wisky.R
import com.project.wisky.databinding.FragmentRegisterBinding
import com.project.wisky.model.UserModel

class RegisterFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnRegister -> {
                binding.progressBar.visibility = View.VISIBLE

                if (binding.etName.text.toString().trim().isEmpty()) {
                    binding.etName.error = "Please enter name"
                    binding.etName.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (binding.etEmail.text.toString().trim().isEmpty()) {
                    binding.etEmail.error = "Please enter email"
                    binding.etEmail.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString().trim())
                        .matches()
                ) {
                    binding.etEmail.error = "Please enter a valid email"
                    binding.etEmail.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (binding.etUsername.text.toString().trim().isEmpty()) {
                    binding.etUsername.error = "Please enter username"
                    binding.etUsername.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (binding.etPassword.text.toString().trim().isEmpty()) {
                    binding.etPassword.error = "Please enter password"
                    binding.etPassword.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (binding.etPassword.text.toString().trim().length < 6) {
                    binding.etPassword.error = "Password less than 6 character"
                    binding.etPassword.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                cekEmailRegistered()

            }
        }
    }

    private fun cekEmailRegistered() {
        //cek email has been registered?
        auth.fetchSignInMethodsForEmail(binding.etEmail.text.toString().trim())
            .addOnCompleteListener {
                Log.i("cekEmail", "cekEmailRegistered: ${it.result?.signInMethods?.size}")
                //cek email terdafar
                if (it.result?.signInMethods?.size != 0) {
                    showSnackbar(binding.container, "Email telah terdaftar!")
                    binding.progressBar.visibility = View.GONE
                } else {
                    createAccount()
                }
            }
    }

    private fun createAccount() {
        auth.createUserWithEmailAndPassword(
            binding.etEmail.text.toString().trim(),
            binding.etPassword.text.toString().trim()
        )
            .addOnCompleteListener(AuthenticationActivity()) { task ->
                if (task.isSuccessful) {
                    //get user
                    user = auth.currentUser as FirebaseUser

                    //simpan di database Users
                    val reference = FirebaseDatabase.getInstance().reference.child("Users")
                        .child(user.uid)

                    val value = UserModel(
                        user.uid,
                        binding.etEmail.text.toString().trim(),
                        binding.etUsername.text.toString().trim(),
                        binding.etName.text.toString().trim()
                    )

                    reference.setValue(value).addOnCompleteListener {
                        if (task.isSuccessful) {
                            binding.progressBar.visibility = View.INVISIBLE
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                            showSnackbar(binding.container, "Terima kasih sudah mendaftar!")

                            //untuk menghapus semua fragment sebelumnya
//                            val manager = requireActivity().supportFragmentManager
//                            val trans: FragmentTransaction = manager.beginTransaction()
//                            trans.remove(RegisterFragment())
//                            trans.commit()
//                            manager.popBackStack()
//                            fragmentManager?.popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                            val fm: FragmentManager = requireActivity().supportFragmentManager
//                            for (i in 0 until fm.backStackEntryCount) {
//                                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                            }
//
//                            val fragmentManager: FragmentManager = getSupportFragmentManager()
//                            //this will clear the back stack and displays no animation on the screen
//                            //this will clear the back stack and displays no animation on the screen
//                            fragmentManager.popBackStackImmediate(
//                                null,
//                                FragmentManager.POP_BACK_STACK_INCLUSIVE
//                            )

                        } else {
                            showSnackbar(binding.container, "Error from database")
                            binding.progressBar.visibility = View.INVISIBLE
                        }
                    }

                } else {
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
    }

    private fun showSnackbar(view: View, text: String) {
        val snackbar = Snackbar.make(
            view, text,
            Snackbar.LENGTH_SHORT
        ).setAction("Ok", null)
            .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.primary_color
            )
        )
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 14f
        snackbar.show()
    }

}