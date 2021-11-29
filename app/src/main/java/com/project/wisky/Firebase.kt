package com.project.wisky

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.wisky.model.WisataModel

object Firebase {

    fun getDataWisata(): LiveData<MutableList<WisataModel>> {
        val mutableData = MutableLiveData<MutableList<WisataModel>>()
        val data = mutableListOf<WisataModel>()

        val reference = FirebaseDatabase.getInstance().reference
            .child("ListWisata")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    val value = dataSnapshot.getValue(WisataModel::class.java)
                    if (value != null) {
                        data.add(value)
                    }
                }
                mutableData.value = data
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        return mutableData
    }

}