package com.project.wisky

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar

object Constants {

    const val CHECK_USERNAME = "check_username"
    const val REMEMBER_ME = "remember_me"
    const val WISATA_ID = "wisata_id"
    const val WISATA_NAME = "wisata_name"
    const val WISATA_DESC = "wisata_desc"
    const val WISATA_ADDRESS = "wisata_address"
    const val WISATA_WORKING_HOUR = "wisata_working_hour"
    const val WISATA_CONTACT = "wisata_contact"
    const val WISATA_CATEGORY = "wisata_category"
    const val WISATA_IMAGE = "wisata_image"

    fun showSnackbar(context: Context, view: View, text: String) {
        val snackbar = Snackbar.make(
            view, text,
            Snackbar.LENGTH_SHORT
        ).setAction("Ok", null)
            .setActionTextColor(ContextCompat.getColor(context, R.color.white))
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.primary_color
            )
        )
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 14f
        snackbar.show()
    }

    fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .into(this)
    }
}