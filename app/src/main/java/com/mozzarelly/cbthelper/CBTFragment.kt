package com.mozzarelly.cbthelper

import android.content.Intent
import android.graphics.text.LineBreaker
import android.os.Build
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mozzarelly.cbthelper.databinding.PopupBinding


abstract class CBTFragment : Fragment() {

    val act by lazy {
        @Suppress("UNCHECKED_CAST")
        requireActivity() as CBTActivity<InterviewViewModel>
    }

    abstract val title: String

    inline fun EditText.bindTo(liveData: MutableLiveData<String?>, crossinline transform: (String?) -> String? = { it?.toString() }){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
        }

        this.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.toString()?.takeIf { it != liveData.value }?.let {
                    liveData.value = transform(it)
                }
            }
        })

        liveData.observe(viewLifecycleOwner){ new ->
            if (new != this.text.toString())
                (this as TextView).text = new
        }
    }

    fun RadioButton.bindTo(liveData: MutableLiveData<Int?>, text: Int? = null, value: Int){
        text?.let { this.text = getString(it) }
        this.tag = value
        this.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && value != liveData.value)
                liveData.value = (value as? Int) ?: error("Need value for radio")
        }

        liveData.observe(viewLifecycleOwner) {
            if (this.tag == it)
                this.isChecked = true
        }
    }

    fun CheckBox.bindTo(liveData: MutableLiveData<Boolean>, text: Int? = null){
        text?.let { this.text = getString(it) }
        this.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked != liveData.value)
                liveData.value = isChecked
        }

        liveData.observe(viewLifecycleOwner){
            if (this.isChecked != it)
                this.isChecked = it
        }
    }

/*
    fun TextView.bindTextTo(liveData: MutableLiveData<String?>, transform: ((String?) -> String?) = { it ?: "" }){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
        }

        this.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.toString()?.takeIf { it != liveData.value }?.let {
                    liveData.value = it
                }
            }
        })

        liveData.observe(viewLifecycleOwner) {
            if (it != this.text.toString())
                this.text = transform(it)
        }
    }
*/

    fun TextView.display(string: Int, vararg formatArgs: String){
        display(getString(string, *formatArgs))
    }

    fun TextView.display(string: CharSequence){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD

        text = string
    }

    inline fun <reified V: View, reified T: Any?> V.display(liveData: LiveData<T>, crossinline transform: (T?) -> String? = { it?.toString() }){
        visibility = View.VISIBLE

        if (this is TextView) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
            }
        }

        viewLifecycleOwner.observe(liveData) {
            when (this){
                is TextView -> text = transform(it)
                is CheckBox -> isChecked = it.toBoolean()
                else -> error("Can't support ${this::class.qualifiedName}")
            }
        }
    }

    inline fun <reified V: View, reified T: Any?> LiveData<T>.displayIn(view: V){
        view.display(this)
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

    inline fun <reified A: CBTActivity<*>> start() = act.startActivityForResult(Intent(requireContext(), A::class.java), A::class.requestCode())

    inline fun <reified A: CBTActivity<*>> start(id: Int) {
        act.startActivityForResult(Intent(act, A::class.java).apply {
            putExtra("id", id.toString())
        }, A::class.requestCode())
    }

    inline fun <reified A: CBTActivity<*>> start(vararg extras: Pair<String, String>) {
        act.startActivityForResult(Intent(requireContext(), A::class.java).apply {
            extras.forEach {
                putExtra(it.first, it.second)
            }
        }, A::class.requestCode())
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

    fun showExplanationPopup(headingText: Int, explanation: Int){
        BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme).apply {
            setContentView(PopupBinding.inflate(layoutInflater, null, false).apply {
                heading.display(headingText)
                text.display(explanation)
                done.setOnClickListener { dismiss() }
            }.root)

            show()
        }
    }

    inline fun <reified V: ViewBinding> showInBottomSheet(binding: V, setup: V.(BottomSheetDialog) -> Unit){
        BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme).apply {
            setup(binding, this)
            setContentView(binding.root)
            show()
        }
    }

}
