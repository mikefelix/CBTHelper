package com.mozzarelly.cbthelper

import android.app.Activity
import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mozzarelly.cbthelper.analyze.AnalyzeEntryIntroFragment

abstract class CBTActivity<V : CBTViewModel> : AppCompatActivity() {

    protected abstract val layout: Int
    protected abstract val viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    val viewModelProvider: ViewModelProvider.NewInstanceFactory = object: ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.newInstance().also {
                (it as V).run {
                    applicationContext = this@CBTActivity.applicationContext
                    setup()
                }
            }
        }
    }

    inline fun <reified V : ViewModel> ViewModelProvider.NewInstanceFactory.getAndInit(): V = create(V::class.java)

    protected open fun V.setup(){ }

    inline fun <reified A: CBTActivity<*>> start(requestCode: Int) = startActivityForResult(Intent(this@CBTActivity, A::class.java), requestCode)

    inline fun <reified A: CBTActivity<*>> start(requestCode: Int, vararg extras: Pair<String, String>) {
        startActivityForResult(Intent(this@CBTActivity, A::class.java).apply {
            extras.forEach {
                putExtra(it.first, it.second)
            }
        }, requestCode)
    }

    inline fun <reified T: Any> Activity.extra(key: String, default: T? = null) = lazy {
        val value = intent?.extras?.get(key)
        if (value is T) value else default
    }

    inline fun <reified T: Any> Activity.extraNotNull(key: String, default: T? = null) = lazy {
        val value = intent?.extras?.get(key)
        requireNotNull(if (value is T) value else default) { key }
    }

    fun finish(result: Int) {
        setResult(result)
        finish()
    }

    fun getIdExtra() = intent.getStringExtra("id")?.toInt() ?: error("No id in intent.")

    inline fun <reified T: CBTFragment> showFragment() = supportFragmentManager.runTx {
        T::class.java.newInstance().let {
            add(R.id.contentFragment, it)
            show(it)
        }
    }
}