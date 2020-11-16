package com.mozzarelly.cbthelper.behavior

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.mozzarelly.cbthelper.PageFragment
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorQuestion3RadiosBinding

abstract class BehaviorFragment : PageFragment<BehaviorViewModel>(){

    override val viewModel: BehaviorViewModel by activityViewModels()
    protected val myOurRegex = "^(my|our) ".toRegex(RegexOption.IGNORE_CASE)
    protected fun replaceMyOur(person: String): String = person.replace(myOurRegex) { match ->
        match.groups[1]!!.value.let {
            when (it) {
                "my" -> "your"
                "our" -> "your"
                else -> it
            }
        }
    }

    fun namePerson(person: String?): String? = when {
        person == null -> "the person you respect most"
        person.matches(myOurRegex) -> replaceMyOur(person)
        else -> person
    }

    protected fun FragmentBehaviorQuestion3RadiosBinding.bindPersonQuestion(liveData: MutableLiveData<Int?>, questionText: Int){
        buttons.setupButtons(enableNextWhenFilled = liveData)
        expression.displayDatum(viewModel.expression)

        question.display(viewModel.personValue) {
            getString(questionText, namePerson(it))
        }

        radioAnswer1.bindTo(liveData, R.string.yes, 1)
        radioAnswer2.bindTo(liveData, R.string.no, 2)
        radioAnswer3.bindTo(liveData, R.string.notSure, 3)
    }

}