package com.mozzarelly.cbthelper

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData

open class CBTFragment : Fragment() {

    inline fun <reified V: View, reified T: Any?> V.display(liveData: LiveData<T>, crossinline transform: (T?) -> String? = { it?.toString() }){
        viewLifecycleOwner.observe(liveData) {
            when (this){
                is TextView -> text = transform(it)
                is CheckBox -> isChecked = it.toBoolean()
                else -> error("Can't support ${this::class.qualifiedName}")
            }
        }
    }

    fun <T: Any?> T?.toBoolean(): Boolean = when (this){
        null -> false
        true -> true
        "true" -> true
        else -> false
    }

    /*
    fun <T: View, R> LiveData<R>.showValueIn(view: T, transform: (R?) -> String? = { it?.toString() }){
        observe(viewLifecycleOwner, Observer {
            when (view){
                is TextView -> view.text = transform(value)
                is CheckBox -> view.isChecked = value.toBoolean()
                else -> error("Can't support ${view::class.qualifiedName}")
            }
        })
    }
    */

}
