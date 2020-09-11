package com.mozzarelly.cbthelper

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

abstract class AddEntryFragment(private val viewModel: EditEntryViewModel) : Fragment() {

    protected val act by lazy {
        requireActivity() as AddEntryActivity
    }

    fun <T: View, R> LiveData<R>.showValueIn(view: T, transform: ((R?) -> String?) = { it?.toString() }){
        observe(viewLifecycleOwner, Observer {
            when (view){
                is TextView -> view.text = transform(value)
                is CheckBox -> view.isChecked = value.toBoolean()
                else -> error("Can't support ${view::class.qualifiedName}")
            }
        })
    }

    val updateOps = mutableListOf<() -> Unit>()

    fun previousPage(){
        for (op in updateOps){
            op()
        }

        viewModel.previousPage()
    }

    fun nextPage(){
        for (op in updateOps){
            op()
        }

        viewModel.nextPage()
    }

    inline fun <reified V: View, reified T: Any?> V.bindTo(liveData: MutableLiveData<T>){
        when (this) {
            is CheckBox -> //updateOps += { liveData.value = isChecked as T }
                this.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked != liveData.value)
                        liveData.value = isChecked as T
                }
            is TextView -> //updateOps += { liveData.value = text.toString() as T }
                this.addTextChangedListener(object: TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val new = this@bindTo.text.toString()
                        if (new != liveData.value)
                            liveData.value = new as T
                    }
                })

            else -> error("Can't handle ${this::class.java.name} && ${T::class.java.name}")
        }

        liveData.observe(viewLifecycleOwner, Observer { new ->
            when (this){
                is CheckBox -> new.toBoolean().let {
                    if (this.isChecked != it)
                        this.isChecked = it
                }
                is TextView -> new?.toString()?.let {
                    if (it != this.text.toString())
                        this.text = it
                }
                else -> error("Can't support ${this::class.qualifiedName}")
            }
        })
    }

    fun <T: Any?> T?.toBoolean(): Boolean = when (this){
        null -> false
        true -> true
        "true" -> true
        else -> false
    }

    protected fun <T: Any?> Button.enableWhenHasValue(value: LiveData<T>) {
        viewLifecycleOwner.observe(value){
            isEnabled = it != null && it.toString().isNotBlank()
        }
    }

}