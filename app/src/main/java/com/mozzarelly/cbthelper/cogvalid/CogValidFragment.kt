package com.mozzarelly.cbthelper.cogvalid

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.mozzarelly.cbthelper.PageFragment
import com.mozzarelly.cbthelper.databinding.*
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

        question.setText(questionText)

        radioAnswer1.bindTo(liveData, answer1, 1)
        radioAnswer2.bindTo(liveData, answer2, 2)
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