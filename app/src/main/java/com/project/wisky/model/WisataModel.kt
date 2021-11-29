package com.project.wisky.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WisataModel(
    var wisataId : String = "",
    var wisataName : String = "",
    var wisataDesc : String = "",
    var wisataAddress : String = "",
    var wisataWorkingHour : String = "",
    var wisataContact : String = "",
    var wisataCategory : String = "",
    var wisataImage : String = ""
) : Parcelable