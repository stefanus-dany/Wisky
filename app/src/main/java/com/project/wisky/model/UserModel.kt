package com.project.wisky.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var userId : String = "",
    var email : String = "",
    var username : String = "",
    var name : String = "",
) : Parcelable