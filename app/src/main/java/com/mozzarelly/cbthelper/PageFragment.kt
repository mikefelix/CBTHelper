@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.mozzarelly.cbthelper

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mozzarelly.cbthelper.editentry.AddEntryActivity
import com.mozzarelly.cbthelper.editentry.EditEntryViewModel

abstract class PageFragment<V: PagingViewModel>() : CBTFragment() {
    abstract val viewModel: V

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

    protected fun <T: Any?> Button.enableWhenHasValue(value: LiveData<T>) {
        viewLifecycleOwner.observe(value){
            isEnabled = it != null && it.toString().isNotBlank()
        }
    }

    inline fun <reified V: View, reified T: Any?> V.bindTo(liveData: MutableLiveData<T>, text: Int? = null, value: Any? = null){
        when (this) {
            is RadioButton -> { //updateOps += { liveData.value = isChecked as T }
                text?.let { this.text = getString(it) }
                this.setTag(value)
                this.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked && value != liveData.value)
                        liveData.value = (value as? T) ?: error("Need value for radio")
                }
            }
            is CheckBox -> {//updateOps += { liveData.value = isChecked as T }
                text?.let { this.text = getString(it) }
                this.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked != liveData.value)
                        liveData.value = isChecked as T
                }
            }
            is TextView -> {//updateOps += { liveData.value = text.toString() as T }
                text?.let { this.text = getString(it) }
                this.addTextChangedListener(object: TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val new = this@bindTo.text.toString()
                        if (new != liveData.value)
                            liveData.value = new as T
                    }
                })
            }
            else -> error("Can't handle ${this::class.java.name} && ${T::class.java.name}")
        }

        liveData.observe(viewLifecycleOwner, Observer { new ->
            when (this){
                is RadioButton -> (new as? Int).let {
                    if (this.getTag() == it)
                        this.isChecked = true
                }
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
}
