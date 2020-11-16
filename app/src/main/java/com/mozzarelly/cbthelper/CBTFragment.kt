package com.mozzarelly.cbthelper

import android.content.Intent
import android.graphics.text.LineBreaker
import android.os.Build
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorQuestion3RadiosBinding


abstract class CBTFragment : Fragment() {

    val act by lazy {
        @Suppress("UNCHECKED_CAST")
        requireActivity() as CBTActivity<InterviewViewModel>
    }

    abstract val title: String

    inline fun <reified T: Any?> TextView.displayDatum(liveData: LiveData<T>){
/*
        val drawable = TopGravityDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.ic_quote))
        drawable.setTintList(foregroundTintList)
        drawable.setTintMode(PorterDuff.Mode.SRC_IN)
        setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        compoundDrawablePadding = 8
        compoundDrawableTintList = foregroundTintList
*/

/*
        viewLifecycleOwner.observe(liveData) {
            val imageSpan = ImageSpan(context, R.drawable.ic_quote);
            val ss = SpannableString(" " + it.toString());
            ss.setSpan(imageSpan, 0, 1, 0);
            text = ss
        }
*/
        display(liveData)
    }

    inline fun <reified V: View, reified T: Any?> V.bindTo(liveData: MutableLiveData<T>, text: Int? = null, value: Any? = null){
        when (this) {
            is RadioButton -> {
                text?.let { this.text = getString(it) }
                this.setTag(value)
                this.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked && value != liveData.value)
                        liveData.value = (value as? T) ?: error("Need value for radio")
                }
            }
            is CheckBox -> {
                text?.let { this.text = getString(it) }
                this.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked != liveData.value)
                        liveData.value = isChecked as T
                }
            }
            is TextView -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
                }

                text?.let { this.text = getString(it) }
                this.addTextChangedListener(object: TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        /*this@bindTo.text?*/s?.toString()?.takeIf { it != liveData.value }?.let {
                            liveData.value = it as T
                        }
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

    inline fun <reified V: View, reified T: Any?> V.display(liveData: LiveData<T>, crossinline transform: (T?) -> String? = { it?.toString() }){
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

    inline fun <reified V: View, reified T: Any?> LiveData<T>.displayIn(view: V, crossinline transform: (T?) -> String? = { it?.toString() }){
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

}
