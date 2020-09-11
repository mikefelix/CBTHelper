package com.mozzarelly.cbthelper

import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

enum class State {
    Initializing, AwaitingUserInput, IssuingRequest
}

abstract class LCEViewModel<X : Any> : ViewModel() {
    protected var lceState = State.Initializing
        set(value){
            field = value
            refreshing.value = value == State.IssuingRequest
        }

    val refreshing = MutableLiveData(false)
    val message = MutableLiveData<String>()
    val initialized = refreshing.map { model != null }

    open var model: X? = null

    protected inline fun ifState(state: State, block: () -> Unit){
        if (this.lceState == state){
            block()
        }
    }

    protected inline fun withState(state: State, block: () -> Unit){
        try {
            this.lceState = state
            block()
        }
        finally {
            this.lceState = State.AwaitingUserInput
        }
    }

    private val completeCallbacks = mutableSetOf<(X) -> Unit>()
    fun onLoadingComplete(block: (X) -> Unit){
        completeCallbacks.add(block)
    }

    private val beginLoadingCallbacks = mutableSetOf<() -> Unit>()
    fun onBeginLoading(block: () -> Unit){
        beginLoadingCallbacks.add(block)
    }

    protected val errorCallbacks = mutableSetOf<(Throwable) -> Unit>()
    fun onError(block: (t: Throwable) -> Unit){
        errorCallbacks.add(block)
    }

    protected inline fun launch(crossinline block: suspend () -> Unit, crossinline onError: (Throwable) -> Unit){
        viewModelScope.launch {
            try {
                lceState = State.IssuingRequest
                block()
            }
            catch (e: Throwable) {
                onError(e)
            }
            finally {
                lceState = State.AwaitingUserInput
            }
        }
    }

    protected inline fun launch(crossinline block: suspend () -> Unit) = launch(block, { postError(it) })

    fun refresh(full: Boolean) {
        launch {
            executeRefresh(full)
        }
    }

    fun refresh() = refresh(false)

    fun refreshFull() = refresh(true)

    protected open suspend fun executeRefresh(full: Boolean){
        beginLoadingCallbacks.callAll()
        refreshModel(full).let {
            model = it
            afterRefresh()
            completeCallbacks.callAll(it)
        }
    }

    open fun update() {
        beginLoadingCallbacks.callAll()

        launch {
            val new = doUpdate()
            model = new
            afterRefresh()
            completeCallbacks.callAll(new)
        }
    }

    protected fun postError(e: Throwable) {
        Log.e("LCEViewModel", "Oops: Something went wrong. " + e.message)
        errorCallbacks.forEach { it(e) }
    }

    protected abstract suspend fun refreshModel(full: Boolean): X
    protected abstract suspend fun doUpdate(): X

    open fun afterRefresh(){}

    private fun <A: Function0<*>> Set<A>?.callAll(){
        this?.forEach { handler.post(kotlinx.coroutines.Runnable { it.invoke() }) }
    }

    private fun <A, F: Function1<A, *>> Set<F>?.callAll(a: A){
        this?.forEach { handler.post(kotlinx.coroutines.Runnable { it.invoke(a) }) }
    }

    private fun <A, B, F: Function2<A, B, *>> Set<F>?.callAll(a: A, b: B){
        this?.forEach { handler.post(kotlinx.coroutines.Runnable { it.invoke(a, b) }) }
    }

    private val handler = Handler()

    @ExperimentalCoroutinesApi
    override fun onCleared(){
        completeCallbacks.clear()
        beginLoadingCallbacks.clear()
        errorCallbacks.clear()
        viewModelScope.cancel()
    }
}