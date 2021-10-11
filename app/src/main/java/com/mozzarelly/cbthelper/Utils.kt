package com.mozzarelly.cbthelper

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import java.lang.IllegalArgumentException
import java.util.*

import android.os.Binder
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.BundleCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import com.mozzarelly.cbthelper.analyze.AnalyzeActivity
import kotlinx.coroutines.CancellationException
import java.util.Calendar.DAY_OF_YEAR
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

fun FragmentManager.runTx(tx: FragmentTransaction.() -> Unit){
    val ft = beginTransaction()
    tx(ft)
    ft.commit()
}

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(v) {
        visibility = if (v) View.VISIBLE else View.GONE
    }

fun Context.getPrefs() = PreferenceManager.getDefaultSharedPreferences(this)

fun SharedPreferences.remove(key: String) {
    with (edit()){
        remove(key)
        commit()
    }
}

fun <T : Any> fragmentArgument(defaultValue: T? = null) = FragmentArgumentDelegate<T>(defaultValue)

/**
 * This is borrowed from some public git gist with minor modifications. Doesn't permit `null` values
 * but can be made to do so. Right now this is enforced at the property declaration level
 * (Kotlin won't let you use it because nullable types are not subtypes of [Any] which [T] is a subtype of).
 *
 * @param T property type
 */
class FragmentArgumentDelegate<T : Any>(private val defaultValue: T?) :
    kotlin.properties.ReadWriteProperty<Fragment, T> {

    override operator fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return getValueOrDefault(thisRef, property.name)
    }

    override operator fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        if (thisRef.arguments == null) {
            thisRef.arguments = Bundle()
        }
        val key = property.name
        thisRef.arguments?.apply {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Short -> putShort(key, value)
                is Long -> putLong(key, value)
                is Byte -> putByte(key, value)
                is ByteArray -> putByteArray(key, value)
                is Char -> putChar(key, value)
                is CharArray -> putCharArray(key, value)
                is CharSequence -> putCharSequence(key, value)
                is Float -> putFloat(key, value)
                is Bundle -> putBundle(key, value)
                is Binder -> BundleCompat.putBinder(this, key, value)
                is android.os.Parcelable -> putParcelable(key, value)
                is java.io.Serializable -> putSerializable(key, value)
//                is HttpUrl -> putString(key, value.toString())
                else -> throw IllegalStateException("Type ${value.javaClass.canonicalName} of property $key is not supported")
            }
        }
    }

    private fun getValueOrDefault(frag: Fragment, propName: String): T {
        if (defaultValue == null) {
            val args = frag.arguments
                ?: throw IllegalStateException("Cannot read fragment argument $propName: no arguments have been set")
            if (!args.containsKey(propName)) {
                throw IllegalStateException("Fragment argument '$propName' is missing")
            }
            @Suppress("UNCHECKED_CAST")
            return args.get(propName) as T
        }
        else {
            val args = frag.arguments ?: return defaultValue
            if (!args.containsKey(propName)) {
                return defaultValue
            }

            @Suppress("UNCHECKED_CAST")
            return args.get(propName) as T
        }
    }

}


fun Context.preferences(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
inline fun <reified T> Context.setPreference(key: String, value: T) { preferences()[key] = value }
inline fun <reified T> Context.getPreference(key: String) = preferences().getAndInit<T>(key)
inline fun <reified T> Context.getPreferenceOrInit(key: String, orElse: () -> T) = preferences().getOrInit(key, orElse)
inline fun <reified T> Context.getPreferenceOrElse(key: String, orElse: () -> T) = preferences().getOrElse(key, orElse)

inline operator fun <reified T> SharedPreferences.set(key: String, value: T?) {
    with (edit()){
        if (value == null)
            remove(key)
        else
            when (T::class) {
                String::class -> putString(key, value as String)
                Boolean::class -> putBoolean(key, value as Boolean)
                Float::class -> putFloat(key, value as Float)
                Int::class -> putInt(key, value as Int)
                Long::class -> putLong(key, value as Long)
                else -> throw IllegalArgumentException(T::class.simpleName)
            }

        commit()
    }
}

inline fun <reified T> SharedPreferences.getAndInit(key: String): T? {
    return if (contains(key)) {
        when (T::class) {
            String::class -> getString(key, null) as T
            Boolean::class -> getBoolean(key, false) as T
            Float::class -> getFloat(key, 0.0f) as T
            Int::class -> getInt(key, 0) as T
            Long::class -> getLong(key, 0L) as T
            else -> throw IllegalArgumentException(T::class.simpleName)
        }
    }
    else {
        null
    }
}

inline fun <reified T> SharedPreferences.getOrElse(key: String, orElse: () -> T): T {
    return if (contains(key)) {
        when (T::class) {
            String::class -> getString(key, null) as T
            Boolean::class -> getBoolean(key, false) as T
            Float::class -> getFloat(key, 0.0f) as T
            Int::class -> getInt(key, 0) as T
            Long::class -> getLong(key, 0L) as T
            else -> throw IllegalArgumentException(T::class.simpleName)
        }
    }
    else {
        orElse.invoke()
    }
}

fun SharedPreferences.getBoolean(key: String): Boolean = when {
    !contains(key) -> false
    else -> getBoolean(key, false)
}

inline fun <reified T> SharedPreferences.getOrInit(key: String, orElse: () -> T): T {
    return if (contains(key)) {
        when (T::class) {
            String::class -> getString(key, null) as T
            Boolean::class -> getBoolean(key, false) as T
            Float::class -> getFloat(key, 0.0f) as T
            Int::class -> getInt(key, 0) as T
            Long::class -> getLong(key, 0L) as T
            else -> throw IllegalArgumentException(T::class.simpleName)
        }
    }
    else {
        val value = orElse.invoke()
        with (edit()){
            when (value){
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                else -> throw IllegalArgumentException(value.toString())
            }
        }.commit()

        value
    }
}

fun now(): Calendar = Calendar.getInstance().apply { time = Date() }
fun Calendar(millis: Long): Calendar = Calendar.getInstance().apply { time = Date(millis) }
fun Calendar.dayOfYear(): Int = this.get(Calendar.DAY_OF_YEAR)
fun Calendar.day(): Int = (this.timeInMillis / 1000 / 60 / 60 / 24).toInt()
fun Int.asCalendarDay(): Calendar = Calendar.getInstance().also {
    it.set(DAY_OF_YEAR, this)
}

operator fun Calendar.plus(millis: Long) = this.apply {
    timeInMillis += millis
}

fun Int.days(): Long = 86400000L * this
fun Int.hours(): Long = 3600000L * this
fun Int.minutes(): Long = 60000L * this

fun String?.toTime(): Time {
    val (hour, minute) = this?.split(":")
        ?.map { it.toInt() }
        ?: listOf(10, 0)

    return Time(hour, minute)
}

fun Calendar.toTime(): Time {
    return Time(get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE))
}

fun Intent.decorate(message: String?, title: String?) = apply {
    action = Intent.ACTION_MAIN
    flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
    message?.let { putExtra("contentText", it) }
    title?.let { putExtra("contentTitle", it) }
}

fun View.toggleVisibility() = if (visibility == View.VISIBLE) {
    visibility = View.GONE
    false
}
else {
    visibility = View.VISIBLE
    true
}

fun <T> LifecycleOwner.liveData(initVal: T, onChange: (T) -> Unit) = MutableLiveData<T>().apply {
    value = initVal
    observe(this@liveData, Observer { onChange(it) })
}

fun Context.setAlarm(time: Time, title: String, text: String){
    (getSystemService(Context.ALARM_SERVICE) as AlarmManager).set(
        AlarmManager.RTC,
        time.toLong(),
        intentToSendReminderNotification(this, title, text)
    )

    Toast.makeText(this, "Reminder set for $time.", Toast.LENGTH_SHORT).show()
}

fun intentToGoToApp(context: Context): PendingIntent = PendingIntent.getActivity(
    context, 0,
    Intent(context, MainActivity::class.java),
    PendingIntent.FLAG_UPDATE_CURRENT
)

fun intentToSendReminderNotification(context: Context, title: String?, message: String?): PendingIntent = PendingIntent.getBroadcast(
    context, 0,
    Intent(context, ReminderReceiver::class.java).decorate(message, title),
    PendingIntent.FLAG_UPDATE_CURRENT
)

fun emotionTextSimple(vararg emotions: String?): String? {
    val filtered = emotions.filterNotNull()
    return when (filtered.size){
        0 -> null
        1 -> filtered[0]
        2 -> "${filtered[0]} and ${filtered[1]}"
        3 -> "${filtered[0]}, ${filtered[1]} and ${filtered[2]}"
        else -> "${filtered[0]}, ${filtered[1]}, ${filtered[2]}..."
    }
}

fun emotionText(vararg emotions: Pair<String?, Int?>): String? {
    fun Pair<String?, Int?>.text() = if (second == null) first else "$first ($second/10)"
    
    val filtered = emotions.filter { it.first != null }
    return when (filtered.size){
        0 -> null
        1 -> filtered[0].text()
        2 -> "${filtered[0].text()} and ${filtered[1].text()}"
        3 -> "${filtered[0].text()}, ${filtered[1].text()} and ${filtered[2].text()}"
        else -> "${filtered[0].text()}, ${filtered[1].text()}, ${filtered[2].text()}..."
    }
}

fun Context.presentChoice(message: Int, choice1: Int = R.string.ok, choice2: Int = R.string.cancel, choice1Action: () -> Unit, choice2Action: () -> Unit){
    AlertDialog.Builder(this)
        .setMessage(message)
        .setNegativeButton(choice1) { _, _ -> choice1Action() }
        .setPositiveButton(choice2) { _, _ -> choice2Action() }
        .create()
        .show()
}

fun Context.doAfterConfirm(message: Int, ok: Int = R.string.ok, cancel: Int = R.string.cancel, action: () -> Unit){
    AlertDialog.Builder(this)
        .setMessage(message)
        .setNegativeButton(cancel) { _, _ ->  }
        .setPositiveButton(ok) { _, _ -> action() }
        .create()
        .show()
}

fun Activity.hideKeyboard() {
    currentFocus?.let {
        val imm = ContextCompat.getSystemService(this, InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun Fragment.hideKeyboard() {
    activity?.currentFocus?.let {
        val imm = ContextCompat.getSystemService(requireActivity(), InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

inline fun <T> MutableList<T>.removeWhere(filter: (T) -> Boolean): Boolean {
    var removed = false
    val each = iterator()
    while (each.hasNext()) {
        if (filter(each.next())) {
            each.remove()
            removed = true
        }
    }
    return removed
}

fun Exception.rethrowIfCancellation() {
    if (this is CancellationException)
        throw this
}

fun View.longSnackbar(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
}

fun View.shortSnackbar(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_SHORT).show()
}

fun KClass<*>.requestCode() = (hashCode() and 0x0000ffff).also { println("requestCode for $simpleName is $it") }

fun <V : CBTViewModel> CBTActivity<V>.showSavedEntryDialog(id: Int){
    AlertDialog.Builder(this).apply {
        if (id >= 0) {
            setTitle("Entry saved")
            setMessage("Your entry has been recorded. Would you like to analyze it now or later?")
            setNegativeButton("Analyze later"){ dialog, _ -> dialog.dismiss() }
            setPositiveButton("Analyze now") { _, _ -> start<AnalyzeActivity>(id) }
        }
        else {
            setTitle(if (id >= 0) "Entry saved" else "Entry not saved!")
            setMessage(if (id >= 0) "Your entry has been recorded. Would you like to analyze it now or later?" else "Something went wrong. Your entry could not be saved.")
            setNegativeButton("OK"){ dialog, _ -> dialog.dismiss() }
        }

        show()
    }

}