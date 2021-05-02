package com.mozzarelly.cbthelper

import androidx.lifecycle.*

fun <T> LiveData<T>.valueNotNull() = value!!

// Helper function to enable better Kotlin support for nullability checking in the presence of platform nulls.
inline fun <reified T> LiveData<T>.data(): T = value.let {
    if (null is T){
        it as T
    }
    else {
        it!!
    }
}

inline fun <reified T> LifecycleOwner.observe(data: LiveData<T>, crossinline lambda: (T) -> Unit) {
    data.observe(this, Observer {
//        if (it == null)
//            println("A ${T::class.simpleName} is null!")
//        else
            lambda(it)
    })
}


inline fun <T, R> List<LiveData<T>>.mapData(crossinline convert: (List<T?>) -> R) = MediatorLiveData<R>().apply {
    this@mapData.forEach {
        addSource<T>(it) {
            val values = this@mapData.map { it.value }
            value = convert(values)
        }
    }
}

fun mapAny(vararg ts: LiveData<Boolean>) = MediatorLiveData<Boolean>().apply {
    ts.forEach {
        addSource(it) {
            value = ts.map { it.value }.reduceRight { b, acc -> (b ?: false) || (acc ?: false) }
        }
    }
}

fun mapAll(vararg ts: LiveData<Boolean>) = MediatorLiveData<Boolean>().apply {
    ts.forEach {
        addSource(it) {
            value = ts.map { it.value }.reduceRight { b, acc -> (b ?: false) && (acc ?: false) }
        }
    }
}

/**
 * Return a LiveData that responds to changes in this LiveData by updating its value using the convert function.
 * If the value of this LiveData is null return null, else call the convert function.
 *
 * @param convert A function to convert T to R (with T as the receiver).
 */
inline fun <T: Any?, R> LiveData<T?>.mapNullOr(crossinline convert: (T) -> R?): LiveData<R?> = MediatorLiveData<R?>().apply {
    addSource<T>(this@mapNullOr) { t ->
        value = if (t == null) null else convert(t)
    }
}

/**
 * Return a LiveData that responds to changes in this LiveData by updating its value using the convert function.
 * If the value of this LiveData is null return null, else call the convert function with a smart-casted receiver value.
 *
 * @param convert A function to convert T? to R (with T as a receiver param).
 */
inline fun <T: Any?, R> LiveData<T?>.mapAsNullOr(crossinline convert: T.() -> R?): LiveData<R?> = MediatorLiveData<R?>().apply {
    addSource<T>(this@mapAsNullOr) {
        value = convert(it)
    }
}

/**
 * Return a LiveData that responds to changes in this LiveData by updating its value using the convert function.
 * If the value of this LiveData is null return null, else map to the convert function.
 *
 * @param convert A function to convert T? to R.
 */
inline fun <T, R> LiveData<T>.map(crossinline convert: (T) -> R): LiveData<R> = MediatorLiveData<R>().apply {
    addSource<T>(this@map) { t ->
        value = if (t == null) null else convert(t)
    }
}

/**
 * Return a LiveData that responds to changes in this LiveData by updating its value using the convert function.
 * If the value of this LiveData is null return null, else map to the convert function.
 *
 * @param convert A function to convert T? to R.
 */
inline fun <T, R> LiveData<T>.flatMap(crossinline convert: (T) -> LiveData<R>): LiveData<R> = Transformations.switchMap(this) {
    convert(it)
}

/**
 * Return a LiveData that responds to changes in this LiveData by updating its value using the convert function.
 * If the value of this LiveData is null return null, else map to the convert function.
 *
 * @param convert A function to convert T to R (with T as the receiver).
 */
inline fun <T, R> LiveData<T>.mapAs(crossinline convert: T.() -> R): LiveData<R> = MediatorLiveData<R>().apply {
    addSource<T>(this@mapAs) { t ->
        value = if (t == null) null else convert(t)
    }
}

/**
 * Return a LiveData that responds to changes in this LiveData by updating its value using the convert function.
 * If the value of this LiveData is null return null, else call the convert function with a smart-casted receiver value.
 *
 * @param convert A function to convert T to R (with T as the receiver).
 */
inline fun <T: Any?, R: Any?> LiveData<T?>.mapValueAs(crossinline convert: T.() -> R?): LiveData<R?> = MediatorLiveData<R?>().apply {
    addSource<T>(this@mapValueAs) { t ->
        value = if (t == null) null else convert(t)
    }
}

/**
 * Return a LiveData that responds to changes in this LiveData by updating its value using the convert function.
 * If the value of this LiveData is null return null, else call the convert function.
 *
 * @param convert A function to convert T to R (with T as the receiver).
 */
/*inline */fun <T: Any?, R> LiveData<T?>.mapValue(/*crossinline */convert: (T) -> R?): LiveData<R?> = MediatorLiveData<R?>().apply {
    addSource<T>(this@mapValue) { t ->
        value = if (t == null) null else convert(t)
    }
}

/**
 * Return a LiveData that responds to changes in two other LiveData by updating its value using the convert function.
 *
 * @param convert A function to convert (A, B) to R.
 */
 fun < A, B, R> Pair<LiveData<A>, LiveData<B>>.map( convert: (A?, B?) -> R): LiveData<R> = MediatorLiveData<R>().apply {
    val update: Function0<Unit> = {
        val a = first.value
        val b = second.value

        when {
//            a == null -> println("This $ {A::class.simpleName} shouldn't be but is null.")
//            b == null -> println("This $ {B::class.simpleName} shouldn't be but is null.")
            else -> value = convert(a, b)
        }
    }

    addSource(first) { update() }
    addSource(second) {update() }
}

inline fun <reified A, reified B, R> Pair<LiveData<A>, LiveData<B>>.flatMap(crossinline convert: (A, B) -> LiveData<R>): LiveData<R> = object: MediatorLiveData<R>(){
    private fun <T> hook(t: LiveData<T>){
        addSource(t, object : Observer<T?> {
            var result: LiveData<R>? = null
            override fun onChanged(x: T?) {
                val newResult: LiveData<R> = convert(first.data(), second.data())
                if (this.result === newResult)
                    return

                if (this.result != null)
                    removeSource(this.result!!)

                this.result = newResult
                if (this.result != null) {
                    addSource(this.result!!) {
                        value = it
                    }
                }
            }
        })
    }

    init {
        hook(first)
        hook(second)
    }
}

inline fun <reified A, reified B, reified C, R> Triple<LiveData<A>, LiveData<B>, LiveData<C>>.map(crossinline convert: (A?, B?, C?) -> R): LiveData<R> = MediatorLiveData<R>().apply {
    addSource(first) {
        value = convert(first.value, second.value, third.value)
    }
    addSource(second) {
        value = convert(first.value, second.value, third.value)
    }
    addSource(third) {
        value = convert(first.value, second.value, third.value)
    }
}

/*inline */fun observeAll(vararg liveDatas: LiveData<*>, /*crossinline */handler: () -> Unit) {
    for (liveData in liveDatas){
        liveData.observeForever {
            handler()
        }
    }
}

/*inline */fun LifecycleOwner.observeAll(vararg liveDatas: LiveData<*>, /*crossinline */handler: () -> Unit) {
    for (liveData in liveDatas){
        liveData.observe(this, Observer {
            handler()
        })
    }
}

inline fun <reified A, reified B> observe(a: LiveData<A>, b: LiveData<B>, crossinline handler: (A?, B?) -> Unit) {
    a.observeForever {
        handler(a.value, b.value)
    }
    b.observeForever {
        handler(a.value, b.value)
    }
}