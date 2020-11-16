@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.mozzarelly.cbthelper

import android.annotation.SuppressLint
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.SeekBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mozzarelly.cbthelper.databinding.EmotionSelectionBinding
import com.mozzarelly.cbthelper.databinding.NextPreviousBinding
import com.mozzarelly.cbthelper.databinding.QuestionSliderBinding
import com.mozzarelly.cbthelper.editentry.EmotionSelectionViewModel

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
        value.observe(viewLifecycleOwner, Observer {
            isEnabled = it != null && it.toString().isNotBlank()
        })
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
            var touched = false
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser)
                    answerData.value = progress + 1
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                touched = true
            }
        })
    }

    @SuppressLint("SetTextI18n")
    protected fun EmotionSelectionBinding.bind(viewModel: EmotionSelectionViewModel){
        fun addEmotion(): Boolean {
            val emotionVal = writeIn.text?.toString().takeIf { !it.isNullOrBlank() } ?: return false
            val intensityVal = intensity.progress + 1

            viewModel.editingEmotion?.run {
                first.value = emotionVal
                second.value = intensityVal
            }

            emotionGroups.setSelection(0)
            emotions.setSelection(0)
            writeIn.setText("")
            hideKeyboard()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                intensity.setProgress(4, true)
            else
                intensity.progress = 4

            return true
        }

        observe(viewModel.canAddEmotions) {
            (if (it) View.VISIBLE else View.INVISIBLE).let {
                groupLabel.visibility = it
                emotionLabel.visibility = it
                emotionGroups.visibility = it
                emotions.visibility = it
                writeLabel.visibility = it
                writeIn.visibility = it
            }
        }

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
                    addButton.isEnabled = !s.isNullOrBlank()
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
                }
            }
        }

        emotions.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                writeIn.setText(emotions.selectedItem?.takeIf { position > 0 }?.toString() ?: "")
            }
        }

        addButton.setOnClickListener {
            addEmotion()
/*
            val emotion = writeIn.text.takeIf { !it.isNullOrBlank() }?.toString() ?: return@setOnClickListener

            viewModel.editingEmotion?.run {
                first.value = emotion
                second.value = intensity.progress + 1
            }

            writeIn.setText("")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                intensity.setProgress(0, true)
            else
                intensity.progress = 0
*/
        }

        emotionsChosen.display(viewModel.emotionsChosen)

        emotionsChosen.setOnClickListener {
            requireContext().doAfterConfirm(R.string.deleteConfirm) {
                viewModel.deleteLastEmotion()
            }
        }

        fun setIntensityLabel(value: Int?){
            intensityLabel.text = "Intensity: ${value ?: ""}"
        }

        intensity.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setIntensityLabel(progress + 1)
            }
        })

//        setIntensityLabel(viewModel.editingEmotion?.second?.value)
    }

}
