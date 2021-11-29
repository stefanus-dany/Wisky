package com.project.wisky.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.wisky.Firebase
import com.project.wisky.model.WisataModel

class HomeViewModel : ViewModel() {

    fun getDataWisata(): LiveData<MutableList<WisataModel>> {
        val mutableData = MutableLiveData<MutableList<WisataModel>>()
        Firebase.getDataWisata().observeForever {
            mutableData.value = it
        }
        Log.i("testFirebase", "getDataWIsata: $mutableData")
        return mutableData
    }
}