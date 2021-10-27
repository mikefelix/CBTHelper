package com.mozzarelly.cbthelper.cogvalid

import android.widget.CheckBox
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.mozzarelly.cbthelper.PageFragment
import com.mozzarelly.cbthelper.databinding.*
import com.mozzarelly.cbthelper.observe
import com.mozzarelly.cbthelper.visible

abstract class CogValidFragment : PageFragment<CogValidViewModel>(){
    override val viewModel: CogValidViewModel by activityViewModels()

    /*
    protected fun AppCompatSeekBar.bind(answerData: MutableLiveData<Int?>, lowText: Int, highText: Int){
        setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
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
*/

    protected fun FragmentCogvalQuestion2RadiosBinding.bindQuestion(liveData: MutableLiveData<Int?>, questionText: Int, answer1: Int, answer2: Int){
        buttons.setupButtons(enableNextWhenFilled = liveData)

        question.display(questionText)

        radioAnswer1.bindTo(liveData, answer1, 1)
        radioAnswer2.bindTo(liveData, answer2, 2)
    }

    protected fun FragmentCogvalQuestion2RadiosBinding.bindQuestion(liveData: MutableLiveData<Int?>, questionText: String, answer1: Int, answer2: Int){
        buttons.setupButtons(enableNextWhenFilled = liveData)

        question.display(questionText)

        radioAnswer1.bindTo(liveData, answer1, 1)
        radioAnswer2.bindTo(liveData, answer2, 2)
    }

    protected fun FragmentCogvalQuestion4RadiosBinding.bindQuestion(liveData: MutableLiveData<Int?>, questionText: Int,
                                                                    answer1: Int, answer2: Int, answer3: Int, answer4: Int){
        buttons.setupButtons(enableNextWhenFilled = liveData)

        question.display(questionText)

        radioAnswer1.bindTo(liveData, answer1, 1)
        radioAnswer2.bindTo(liveData, answer2, 2)
        radioAnswer3.bindTo(liveData, answer3, 3)
        radioAnswer4.bindTo(liveData, answer4, 4)
    }

    protected fun FragmentCogvalQuestion5RadiosBinding.bindQuestion(liveData: MutableLiveData<Int?>, questionText: Int,
                                                                    answer1: Int, answer2: Int, answer3: Int, answer4: Int, answer5: Int){
        buttons.setupButtons(enableNextWhenFilled = liveData)

        question.display(questionText)

        radioAnswer1.bindTo(liveData, answer1, 1)
        radioAnswer2.bindTo(liveData, answer2, 2)
        radioAnswer3.bindTo(liveData, answer3, 3)
        radioAnswer4.bindTo(liveData, answer4, 4)
        radioAnswer5.bindTo(liveData, answer5, 5)
    }

    protected fun FragmentCogvalQuestion6RadiosBinding.bindQuestion(liveData: MutableLiveData<Int?>, questionText: Int,
                                                                    answer1: Int, answer2: Int, answer3: Int, answer4: Int, answer5: Int, answer6: Int){
        buttons.setupButtons(enableNextWhenFilled = liveData)

        question.display(questionText)

        radioAnswer1.bindTo(liveData, answer1, 1)
        radioAnswer2.bindTo(liveData, answer2, 2)
        radioAnswer3.bindTo(liveData, answer3, 3)
        radioAnswer4.bindTo(liveData, answer4, 4)
        radioAnswer5.bindTo(liveData, answer5, 5)
        radioAnswer6.bindTo(liveData, answer6, 6)
    }

    protected fun FragmentCogvalQuestion3CheckboxesBinding.bindQuestion(liveData: MutableLiveData<Int?>, questionText: Int,
                                                                    answer1: Int, answer2: Int, answer3: Int){
        buttons.setupButtons(enableNextWhenFilled = liveData)

        question.display(questionText)

        checkboxAnswer1.display(answer1)
        checkboxAnswer2.display(answer2)
        checkboxAnswer3.display(answer3)

        setupCheckboxBinding(liveData, listOf(checkboxAnswer1, checkboxAnswer2, checkboxAnswer3))
    }

    protected fun FragmentCogvalQuestion4CheckboxesBinding.bindQuestion(liveData: MutableLiveData<Int?>, questionText: Int,
                                                                    answer1: Int, answer2: Int, answer3: Int, answer4: Int){
        buttons.setupButtons(enableNextWhenFilled = liveData)

        question.display(questionText)

        checkboxAnswer1.display(answer1)
        checkboxAnswer2.display(answer2)
        checkboxAnswer3.display(answer3)
        checkboxAnswer4.display(answer4)

        setupCheckboxBinding(liveData, listOf(checkboxAnswer1, checkboxAnswer2, checkboxAnswer3, checkboxAnswer4))
    }

    protected fun FragmentCogvalQuestion5CheckboxesBinding.bindQuestion(liveData: MutableLiveData<Int?>, questionText: Int,
                                                                    answer1: Int, answer2: Int, answer3: Int, answer4: Int, answer5: Int){
        buttons.setupButtons(enableNextWhenFilled = liveData)

        question.display(questionText)

        checkboxAnswer1.display(answer1)
        checkboxAnswer2.display(answer2)
        checkboxAnswer3.display(answer3)
        checkboxAnswer4.display(answer4)
        checkboxAnswer5.display(answer5)

        setupCheckboxBinding(liveData, listOf(checkboxAnswer1, checkboxAnswer2, checkboxAnswer3, checkboxAnswer4, checkboxAnswer5))
    }

    protected fun FragmentCogvalQuestion6CheckboxesBinding.bindQuestion(liveData: MutableLiveData<Int?>, questionText: Int,
                                                                    answer1: Int, answer2: Int, answer3: Int, answer4: Int, answer5: Int, answer6: Int){
        buttons.setupButtons(enableNextWhenFilled = liveData)

        question.display(questionText)

        checkboxAnswer1.display(answer1)
        checkboxAnswer2.display(answer2)
        checkboxAnswer3.display(answer3)
        checkboxAnswer4.display(answer4)
        checkboxAnswer5.display(answer5)
        checkboxAnswer6.display(answer6)

        setupCheckboxBinding(liveData, listOf(checkboxAnswer1, checkboxAnswer2, checkboxAnswer3, checkboxAnswer4,
            checkboxAnswer5, checkboxAnswer6))
    }

    protected fun FragmentCogvalQuestion7CheckboxesBinding.bindQuestion(liveData: MutableLiveData<Int?>, questionText: Int,
                                                                    answer1: Int, answer2: Int, answer3: Int, answer4: Int, answer5: Int, answer6: Int, answer7: Int){
        buttons.setupButtons(enableNextWhenFilled = liveData)

        question.display(questionText)

        checkboxAnswer1.display(answer1)
        checkboxAnswer2.display(answer2)
        checkboxAnswer3.display(answer3)
        checkboxAnswer4.display(answer4)
        checkboxAnswer5.display(answer5)
        checkboxAnswer6.display(answer6)
        checkboxAnswer7.display(answer7)

        setupCheckboxBinding(liveData, listOf(checkboxAnswer1, checkboxAnswer2, checkboxAnswer3, checkboxAnswer4,
            checkboxAnswer5, checkboxAnswer6, checkboxAnswer7))
    }

    private fun setupCheckboxBinding(liveData: MutableLiveData<Int?>, boxes: List<CheckBox>){
        var redisplaying = false
        fun redisplay(){
            redisplaying = true
            fanOutToCheckboxes(liveData.value ?: 0, boxes)
            redisplaying = false
        }

        boxes.forEach {
            it.setOnCheckedChangeListener { box, _ ->
                if (!redisplaying) {
                    if (box == boxes.last())
                        liveData.value = 0
                    else
                        aggregateFromCheckboxes(liveData, boxes)
                }
            }
        }

        observe(liveData){
            redisplay()
        }

        redisplay()
    }

    private fun fanOutToCheckboxes(value: Int?, checkboxes: List<CheckBox>){
        var mult = 1

        checkboxes.forEachIndexed { i, box ->
            box.isChecked = when {
                value == null -> false
                i == checkboxes.size - 1 -> value == 0
                else -> (value and mult) > 0
            }

            mult *= 2
        }
    }

    private fun aggregateFromCheckboxes(liveData: MutableLiveData<Int?>, checkboxes: List<CheckBox>){
        var mult = 1
        var value = 0

        checkboxes.forEachIndexed { i, box ->
            if (i < checkboxes.size - 1)
                value += mult * if (box.isChecked) 1 else 0

            mult *= 2
        }

        liveData.value = value
    }

    protected fun FragmentCogvalQuestion3RadiosBinding.bindQuestion(liveData: MutableLiveData<Int?>, questionText: Int, answer1: Int, answer2: Int, answer3: Int){
        buttons.setupButtons(enableNextWhenFilled = liveData)

        question.setText(questionText)

        radioAnswer1.bindTo(liveData, answer1, 1)
        radioAnswer2.bindTo(liveData, answer2, 2)
        radioAnswer3.bindTo(liveData, answer3, 3)
    }

    protected fun FragmentCogvalQuestionTextBinding.bindQuestion(questionText: Int, answerData: MutableLiveData<String?>) {
        buttons.setupButtons(enableNextWhenFilled = answerData)

        question.setText(questionText)
        answer.bindTo(answerData)
    }

    protected fun FragmentCogvalQuestion1TextBinding.bindQuestion(questionText: Int, answerData: MutableLiveData<String?>
    ) {
        buttons.setupButtons(enableNextWhenFilled = answerData)

        question.setText(questionText)
        answer.bindTo(answerData)
    }

    protected fun FragmentCogvalQuestion2TextsBinding.bindQuestion(question1Text: Int, answer1Data: MutableLiveData<String?>,
                                                                   question2Text: Int, answer2Data: MutableLiveData<String?>
    ) {
        buttons.setupButtons(enableNextWhenFilled = answer2Data)

        question1.setText(question1Text)
        question2.setText(question2Text)

        answer1.bindTo(answer1Data)
        answer2.bindTo(answer2Data)
    }

    protected fun FragmentCogvalQuestion5NestedradiosBinding.bindQuestion(liveData: MutableLiveData<Int?>, questionText: Int, outerAnswers: Pair<Int, Int>, innerAnswers: List<Int>){
        buttons.setupButtons(enableNextWhenFilled = liveData)

        question.setText(questionText)

        radioAnswer1.text = getString(outerAnswers.first)
        innerGroup.visible = liveData.value in 1..5

        radioAnswer1.setOnCheckedChangeListener { _, isChecked ->
            innerGroup.visible = isChecked
            if (isChecked){
                liveData.value = null
                radioAnswer1a.isChecked = false
                radioAnswer1b.isChecked = false
                radioAnswer1c.isChecked = false
                radioAnswer1d.isChecked = false
                radioAnswer1e.isChecked = false
            }
        }

        radioAnswer1a.bindTo(liveData, innerAnswers[0], 1)
        radioAnswer1b.bindTo(liveData, innerAnswers[1], 2)
        radioAnswer1c.bindTo(liveData, innerAnswers[2], 3)
        radioAnswer1d.bindTo(liveData, innerAnswers[3], 4)
        radioAnswer1e.bindTo(liveData, innerAnswers[4], 5)

        radioAnswer2.bindTo(liveData, outerAnswers.second, 6)
    }

    protected fun FragmentCogvalQuestion4NestedradiosBinding.bindQuestion(liveData: MutableLiveData<Int?>, questionText: Int, outerAnswers: Pair<Int, Int>, innerAnswers: List<Int>){
        buttons.setupButtons(enableNextWhenFilled = liveData)

        question.setText(questionText)

        radioAnswer1.text = getString(outerAnswers.first)
        innerGroup.visible = liveData.value in 1..5

        radioAnswer1.setOnCheckedChangeListener { _, isChecked ->
            innerGroup.visible = isChecked
            if (isChecked){
                liveData.value = null
                radioAnswer1a.isChecked = false
                radioAnswer1b.isChecked = false
                radioAnswer1c.isChecked = false
                radioAnswer1d.isChecked = false
            }
        }

        radioAnswer1a.bindTo(liveData, innerAnswers[0], 1)
        radioAnswer1b.bindTo(liveData, innerAnswers[1], 2)
        radioAnswer1c.bindTo(liveData, innerAnswers[2], 3)
        radioAnswer1d.bindTo(liveData, innerAnswers[3], 4)

        radioAnswer2.bindTo(liveData, outerAnswers.second, 6)
    }

    protected fun FragmentCogvalQuestionSliderBinding.bindQuestion(answerData: MutableLiveData<Int?>, questionText: Int, lowText: Int, highText: Int) {
        buttons.setupButtons(enableNextWhenFilled = answerData)
        slider.bind(answerData, questionText, lowText, highText)
    }

}