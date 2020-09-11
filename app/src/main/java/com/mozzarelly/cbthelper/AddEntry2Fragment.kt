package com.mozzarelly.cbthelper

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.mozzarelly.cbthelper.databinding.FragmentAdd2ExperienceBinding

class AddEntry2Fragment(private val viewModel: EditEntryViewModel) : AddEntryFragment(viewModel) {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentAdd2ExperienceBinding.inflate(inflater).apply {
            buttons.previous.setOnClickListener {
                previousPage()
            }

            buttons.next.run {
                enableWhenHasValue(viewModel.emotionsChosenValue)

                setOnClickListener {
                    nextPage()
                }
            }

            writeIn.setOnEditorActionListener(object: TextView.OnEditorActionListener {
                override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean = when (actionId) {
                    EditorInfo.IME_ACTION_DONE, EditorInfo.IME_ACTION_SEND -> {
                        val e = v?.text?.toString().takeIf { !it.isNullOrBlank() }
                        if (e != null) {
                            viewModel.editingEmotion?.value = e
                            emotionGroups.setSelection(0)
                            emotions.setSelection(0)
                            writeIn.setText("")
                            true
                        }
                        else {
                            false
                        }
                    }
                    else -> false
                }
            })

            emotionGroups.adapter = ArrayAdapter(act, android.R.layout.simple_spinner_item, Emotions.emotionGroups)

            emotionGroups.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.selectedEmotionGroupIndex.value = position
                    emotions.adapter = ArrayAdapter(act, android.R.layout.simple_spinner_item, Emotions.emotions[position])
                }
            }

            emotions.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val groupIndex = emotionGroups.selectedItemPosition
                    val emotion = position.takeIf { it > 0 }?.let { Emotions.emotions[groupIndex][it] }
                    viewModel.editingEmotion?.value = emotion

                    if (position > 0) {
                        emotionGroups.setSelection(0)
                        emotions.setSelection(0)
                    }
                }
            }

            observe(viewModel.selectedEmotionGroupIndex) {
                if (it != emotionGroups.selectedItemPosition)
                    emotionGroups.setSelection(it)

                viewModel.selectedEmotionIndex.value = 0
            }

            observe(viewModel.selectedEmotionIndex) {
                if (it != emotions.selectedItemPosition)
                    emotions.setSelection(it)
            }

            viewModel.emotionsChosenValue.showValueIn(emotionsChosen)

            emotionsChosen.setOnClickListener {
                requireContext().doAfterConfirm(R.string.deleteConfirm) {
                    viewModel.deleteLastEmotion()
                }
            }

            observe(viewModel.canAddEmotions) {
                emotionSelection.visibility = if (it) View.VISIBLE else View.INVISIBLE
            }

            observe(viewModel.situationTypeValue) {
                textView1.text = "What emotions did you feel during this ${if (it) "situation" else "conversation"}?"
            }

        }.root
}