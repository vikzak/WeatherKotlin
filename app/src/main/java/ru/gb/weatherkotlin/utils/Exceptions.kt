package ru.gb.weatherkotlin.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBarWithResources(
    fragment: Fragment,
    text: Int,
    actionText: Int,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, fragment.getString(text), length)
        .setAction(fragment.getString(actionText), action)
        .show()
}