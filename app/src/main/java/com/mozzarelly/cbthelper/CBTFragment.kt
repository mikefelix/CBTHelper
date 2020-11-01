package com.mozzarelly.cbthelper

import android.content.Intent
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData

open class CBTFragment : Fragment() {

    val act by lazy {
        @Suppress("UNCHECKED_CAST")
        requireActivity() as CBTActivity<PagingViewModel>
    }

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
    inline fun <reified T: Any> Fragment.nullableExtra(key: String, default: T? = null) = lazy {
        val value = arguments?.get(key)
        if (value is T) value else default
    }

    inline fun <reified T: Any> Fragment.extra(key: String) = lazy {
        val value = arguments?.get(key)
        requireNotNull(value as T) { key }
    }
*/

    inline fun <reified A: CBTActivity<*>> start(requestCode: Int) = act.startActivityForResult(Intent(requireContext(), A::class.java), requestCode)

    inline fun <reified A: CBTActivity<*>> start(requestCode: Int, vararg extras: Pair<String, String>) {
        act.startActivityForResult(Intent(requireContext(), A::class.java).apply {
            extras.forEach {
                putExtra(it.first, it.second)
            }
        }, requestCode)
    }

    fun getIdExtra() = act.getIdExtra()

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
