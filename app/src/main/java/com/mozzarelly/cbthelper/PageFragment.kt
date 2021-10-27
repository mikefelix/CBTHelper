@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.mozzarelly.cbthelper

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mozzarelly.cbthelper.databinding.EmotionSelectionBinding
import com.mozzarelly.cbthelper.databinding.NextPreviousBinding
import com.mozzarelly.cbthelper.databinding.QuestionSliderBinding

abstract class PageFragment<V: InterviewViewModel> : CBTFragment() {

    abstract val viewModel: V

    override val title: String
        get() = viewModel.title.value ?: "CBT Helper"

    fun previousPage(){
        viewModel.previousPage()
    }

    fun nextPage(){
        viewModel.nextPage()
    }

    protected fun <T: Any?> Button.enableWhenHasValue(value: LiveData<T>) {
        value.observe(viewLifecycleOwner) {
            isEnabled = it != null && it.toString().isNotBlank()
        }
    }

    protected fun <T: Any?, S: Any?> Button.enableWhenHasValue(value1: LiveData<T>, value2: LiveData<S>) {
        val mediator = MediatorLiveData<Boolean>().apply {
            fun enable() { value = !value1.value?.toString().isNullOrBlank() && !value2.value?.toString().isNullOrBlank() }
            addSource(value1){ enable() }
            addSource(value2){ enable() }
        }

        mediator.observe(viewLifecycleOwner) {
            isEnabled = it
        }
    }

    protected fun NextPreviousBinding.setupButtons(enableNextWhenFilled: LiveData<*>? = null){
        previous.setOnClickListener { previousPage() }

        next.run {
            enableNextWhenFilled?.let { enableWhenHasValue(it) }
            setOnClickListener { nextPage() }
        }
    }

    protected fun QuestionSliderBinding.bind(answerData: MutableLiveData<Int?>, questionText: Int, lowText: Int, highText: Int) {
        question.setText(questionText)
        minimumDesc.setText(lowText)
        maximumDesc.setText(highText)

        observe(answerData) {
            val value = it ?: 4
            answer.progress = value - 1
            answerVal.text = value.toString()
        }

        answer.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser)
                    answerData.value = progress + 1
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                answerData.value = answerData.value ?: 4
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    protected fun TextView.bind(emotion: MutableLiveData<Emotion?>) {
        listOf(this).bind(emotion)
    }

    @SuppressLint("SetTextI18n")
    protected fun List<TextView>.bind(vararg emotions: MutableLiveData<Emotion?>){
        fun show(){
            forEachIndexed { i, view ->
                view.run {
                    val value = emotions[i].value?.toString()
                    when {
                        value != null -> {
                            visibility = View.VISIBLE
                            text = value
                            setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_left_quote, 0, R.drawable.ic_right_quote, 0)
                        }
                        i == emotions.indexOfFirst { it.value == null } -> {
                            visibility = View.VISIBLE
                            text = if (i == 0) "Tap to add..." else "Tap to add more..."
                            setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
                        }
                        else -> {
                            visibility = View.GONE
                        }
                    }
                }
            }
        }

        fun reorder(){
            var values = emotions.mapNotNull { it.value }
            for (index in indices){
                emotions[index].value = values.firstOrNull()
                values = values.drop(1)
            }
        }

        forEachIndexed { i, view ->
            val emotion = emotions[i]
            view.setOnClickListener {
                if (emotion.value == null) {
                    showEmotionSelectionPopup(emotion) {
                        show()
                    }
                }
                else {
                    requireContext().confirmDialog("Delete ${emotion.value}?") {
                        emotion.value = null
                        reorder()
                        show()
                    }
                }
            }

        }

        show()
    }

    @SuppressLint("SetTextI18n")
    protected fun EmotionSelectionBinding.bind(emotion: MutableLiveData<Emotion?>, onDismiss: () -> Unit){
        fun addEmotion(): Boolean {
            writeIn.text?.toString()?.let {
                emotion.value = Emotion(it, intensity.progress + 1)
            }

            hideKeyboard(root)
            return true
        }

        var hasChosenIntensity = false

        writeIn.run {
            setOnEditorActionListener { v, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE, EditorInfo.IME_ACTION_SEND -> addEmotion()
                    else -> false
                }
            }

            addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    addButton.isEnabled = !s.isNullOrBlank() && hasChosenIntensity
                }
            })
        }

        emotionGroups.run {
            adapter = ArrayAdapter(act, android.R.layout.simple_spinner_item, Emotions.emotionGroups)

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    emotions.adapter = ArrayAdapter(act, android.R.layout.simple_spinner_item, emptyList<String>())
                    emotions.setSelection(0)
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    emotions.adapter = ArrayAdapter(act, android.R.layout.simple_spinner_item, Emotions.emotions[position])
                    emotions.setSelection(0)
                    emotions.visibility = if (position > 0) View.VISIBLE else View.GONE
                }
            }
        }

        emotions.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                writeIn.setText(emotions.selectedItem?.takeIf { position > 0 }?.toString() ?: "")
            }
        }

        addButton.isEnabled = emotion.value != null
        addButton.setOnClickListener {
            if (emotion.value == null)
                Toast.makeText(requireContext(), "Please select emotion and intensity.", Toast.LENGTH_SHORT).show()

            if (addEmotion())
                onDismiss()
        }

        fun setIntensityLabel(value: Int?){
            intensityLabel.text = "Intensity: ${value ?: ""}"
            hasChosenIntensity = true
        }

        intensity.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                seekBar?.progress?.plus(1)?.let {
                    setIntensityLabel(it)
                    addButton.isEnabled = !writeIn.text.isNullOrBlank()
                }
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setIntensityLabel(progress + 1)
                addButton.isEnabled = !writeIn.text.isNullOrBlank()
            }
        })
    }

    protected fun showEmotionSelectionPopup(emotion: MutableLiveData<Emotion?>, onClose: () -> Unit){
        BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme).apply {
            setContentView(EmotionSelectionBinding.inflate(layoutInflater, null, false).apply {
                bind(emotion, onDismiss = { onClose(); dismiss() })
            }.root)

            show()
        }
    }

    protected fun Context.confirmDialog(message: String, onConfirm: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("Please confirm")
            .setMessage(message)
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(R.string.ok) { _, _ -> onConfirm() }
            .show()
    }

}
