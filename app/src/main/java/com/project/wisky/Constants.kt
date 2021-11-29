package com.project.wisky

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

object Constants {

    const val CHECK_USERNAME = "check_email"
    const val REMEMBER_ME = "remember_me"

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
}