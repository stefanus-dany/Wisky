package com.project.wisky.authentication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.wisky.Constants
import com.project.wisky.Constants.showSnackbar
import com.project.wisky.MainActivity
import com.project.wisky.R
import com.project.wisky.databinding.FragmentLoginBinding

class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var userId = ""
    private var email = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        sharedPreferences = this.activity?.getSharedPreferences(
            "sharedPrefs",
            Context.MODE_PRIVATE
        ) as SharedPreferences
        val emailSaved = sharedPreferences.getString(Constants.CHECK_USERNAME, null)
        val rememberMe = sharedPreferences.getBoolean(Constants.REMEMBER_ME, false)

        if (rememberMe && emailSaved != null) {
            binding.etUsername.setText(emailSaved)
        }

        binding.rememberMe.isChecked = rememberMe
        binding.btnLogin.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
                binding.progressBar.visibility = View.VISIBLE

                val username = binding.etUsername.text.toString().trim()
                val password = binding.etPassword.text.toString().trim()

                if (username.isEmpty()) {
                    binding.etUsername.error = "Please enter username"
                    binding.etUsername.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (password.isEmpty()) {
                    binding.etPassword.error = "Please enter password"
                    binding.etPassword.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                //fungsi untuk get email. Caranya yaitu mendapatkan id terlebih dahulu, setelah itu dicari email dari id tersebut.
                //Kemudian user bisa login
                getUserId(username)
            }
        }
    }

    //mendapatkan userId yang nantinya akan dimasukan kedalam database untuk dicari email dari userId tersebut
    private fun getUserId(username: String) {
        val query = FirebaseDatabase.getInstance().reference.child("Users")
            .orderByChild("username")
            .equalTo(username)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                for (ds in data.children) {
                    userId = ds.key.toString()
                }

                //mendapatkan email
                getEmail(userId)

            }

            override fun onCancelled(data: DatabaseError) {
                Log.d("LoginFragment", data.message)
            }

        })
    }

    private fun getEmail(userId: String) {
        FirebaseDatabase.getInstance().reference.child("Users")
            .child(userId).child("email").get().addOnSuccessListener { er ->
                if (er.exists()) {
                    email = er.value.toString()
                    login()
                } else {
                    //username salah
                    showSnackbar(
                        requireContext(),
                        binding.container,
                        "Username atau password salah!"
                    )
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
    }

    private fun login() {

        auth.signInWithEmailAndPassword(
            email,
            binding.etPassword.text.toString().trim()
        )
            .addOnCompleteListener(AuthenticationActivity()) { task ->
                user = auth.currentUser
                if (task.isSuccessful) {
                    //get user
                    val reference =
                        FirebaseDatabase.getInstance().reference.child("Users")
                            .child(user!!.uid)
                    reference.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            binding.progressBar.visibility = View.INVISIBLE
                            val user = auth.currentUser
                            updateUI(user)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                            binding.progressBar.visibility = View.INVISIBLE
                        }
                    })


                } else {
                    //password salah
                    updateUI(null)
                    showSnackbar(
                        requireContext(),
                        binding.container,
                        "Username atau password salah!"
                    )
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            updateUI(auth.currentUser)
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {

        if (currentUser != null) {
            requireContext().startActivity(Intent(context, MainActivity::class.java))
            activity?.finish()
        }
    }

    override fun onPause() {
        super.onPause()
        val editor = sharedPreferences.edit()
        if (binding.rememberMe.isChecked) {
            editor.putString(Constants.CHECK_USERNAME, binding.etUsername.text.toString())
            editor.putBoolean(Constants.REMEMBER_ME, true)
            editor.apply()
        } else editor.clear().apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        auth.signOut()
    }
}