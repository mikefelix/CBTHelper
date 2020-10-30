package com.mozzarelly.cbthelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

abstract class CBTActivity<V : CBTViewModel> : AppCompatActivity() {

    val viewModelProvider: ViewModelProvider.NewInstanceFactory = object: ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.newInstance().also {
                (it as V).let {
                    it.applicationContext = applicationContext
                    it.setup()
                }
            }
        }
    }

    inline fun <reified V : ViewModel> ViewModelProvider.NewInstanceFactory.getAndInit(): V = create(V::class.java)

    protected open fun V.setup(){ }

    protected abstract val viewModel: V

    inline fun <reified A: CBTActivity<*>> start(requestCode: Int) = startActivityForResult(Intent(this@CBTActivity, A::class.java), requestCode)

    inline fun <reified A: CBTActivity<*>> start(requestCode: Int, vararg extras: Pair<String, String>) {
        startActivityForResult(Intent(this@CBTActivity, A::class.java).apply {
            extras.forEach {
                putExtra(it.first, it.second)
            }
        }, requestCode)
    }
}