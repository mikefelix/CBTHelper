package com.mozzarelly.cbthelper.behavior

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.mozzarelly.cbthelper.PageFragment
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorQuestion3RadiosBinding

abstract class BehaviorFragment : PageFragment<BehaviorViewModel>() {

    override val viewModel: BehaviorViewModel by activityViewModels()

    private val myOurRegex = "^(my|our)\\b".toRegex(RegexOption.IGNORE_CASE)
    protected fun replaceMyOur(person: String): String = person.replace(myOurRegex) { match ->
        match.groups[1]!!.value.let {
            when (it) {
                "my" -> "your"
                "our" -> "your"
                else -> it
            }
        }
    }

    fun namePerson(person: String?) = when {
        person == null -> "the person you respect most"
        person.matches(myOurRegex) -> replaceMyOur(person)
        else -> person
    }

    protected fun FragmentBehaviorQuestion3RadiosBinding.bindPersonQuestion(liveData: MutableLiveData<Int?>, questionText: Int) {
        buttons.setupButtons(enableNextWhenFilled = liveData)
        expression./*
            val drawable = TopGravityDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.ic_quote))
            drawable.setTintList(foregroundTintList)
            drawable.setTintMode(PorterDuff.Mode.SRC_IN)
            setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            compoundDrawablePadding = 8
            compoundDrawableTintList = foregroundTintList
    */

            /*
                    viewLifecycleOwner.observe(liveData) {
                        val imageSpan = ImageSpan(context, R.drawable.ic_quote);
                        val ss = SpannableString(" " + it.toString());
                        ss.setSpan(imageSpan, 0, 1, 0);
                        text = ss
                    }
            */
        display(viewModel.expression)

        question.display(viewModel.personValue) {
            getString(questionText, namePerson(it))
        }

        radioAnswer1.bindTo(liveData, R.string.yes, 1)
        radioAnswer2.bindTo(liveData, R.string.no, 2)
        radioAnswer3.bindTo(liveData, R.string.notSure, 3)
    }

}