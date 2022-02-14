package ru.gb.weatherkotlin.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar1(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}

/**
 * Функция-расширение, принимающая строковые ресурсы. Получает значения строковых ресурсом здесь.
 * Так же передается экземпляр фрагмента, который содержит функцию для получения строки из ресурса
 * */
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