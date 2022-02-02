package ru.gb.weatherkotlin.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    lenght: Int = Snackbar.LENGTH_INDEFINITE){
        Snackbar.make(this,text,lenght).setAction(actionText, action).show()
}