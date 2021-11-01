package com.mozzarelly.cbthelper.analyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mozzarelly.cbthelper.CBTFragment
import com.mozzarelly.cbthelper.databinding.FragmentAnalyze4BehaviorSummaryBinding
import com.mozzarelly.cbthelper.observe

class AnalyzeBehaviorSummaryFragment : CBTFragment() {

    val viewModel: AnalyzeViewModel by activityViewModels()

    override val title = "Summary"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentAnalyze4BehaviorSummaryBinding.inflate(inflater, container, false).apply {

            eventName.display(viewModel.entryDesc)
            text1.display("You described the situation this way:")
            detail.display(viewModel.detail)
            text2.display("When this happened you thought:")
            thought.display(viewModel.thoughts)
            text3.display("It’s possible that thinking this way stemmed from core beliefs you may hold about yourself such as:")
            assumptions.display(viewModel.assumptions)
            text4.display("Regardless, thinking this way made you feel:")
            emotions.display(viewModel.emotionsFelt)

            observe(viewModel.thinkingErrors) {
                if (it.isEmpty()) {
                    text5.display("You identified your thoughts and beliefs as rational, which means feeling ${viewModel.emotionsFeltSimple.value ?: "what you felt"} was emotionally healthy, even if it may not be exactly what you would prefer to feel. Good job.")
                    thinkingErrors.visibility = View.GONE
                    text6.visibility = View.GONE
                    text7.visibility = View.GONE
                    replacementThoughts.visibility = View.GONE
                    text8.visibility = View.GONE
                    insteadFelt.visibility = View.GONE
                    text9.visibility = View.GONE
                }
                else {
                    text5.display("Looking back on it, you identified thinking errors in your thoughts and beliefs:")
                    thinkingErrors.display(viewModel.thinkingErrors) { it?.joinToString("\n") }
                    text6.display("which means feeling angry and ashamed in this situation was not as emotionally healthy as it could have been.")
                    text7.display("If you could have instead thought something like,")
                    replacementThoughts.display(viewModel.thinkInstead)
                    text8.display("then you would likely have felt")
                    insteadFelt.display(viewModel.feltInstead)
                    text9.display("That would have been more emotionally healthy, even if it may not be exactly what you would prefer to feel. Can you visualize yourself thinking this way in a similar situation in the future?")
                }
            }
            text10.display("When it comes to your behavior, you expressed your emotion by:")
            expressed.display(viewModel.expressed)
            text11.display("and people reacted by:")
            relationships.display(viewModel.relationships)

            observe(viewModel.behavingErrors){
                if (it.isEmpty()){
                    text12.display("You have identified your words and actions as rational, which is foundational for healthy relationships, even though you noted that in this instance the other people involved acted irrationally.")
                    behaviorErrors.visibility = View.GONE
                    text13.visibility = View.GONE
                    text14.visibility = View.GONE
                    moreRationalAction.visibility = View.GONE
                    text15.visibility = View.GONE
                    othersReactions.visibility = View.GONE
                    text16.visibility = View.GONE
                }
                else {
                    text12.display("You have identified behavioral errors in your words and actions:")
                    behaviorErrors.display(viewModel.behavingErrors)
                    text13.display("which means showing your emotion this way was likely not as emotionally healthy as it could have been. ")
                    text14.display("If you could have instead done or said something like")
                    moreRationalAction.display(viewModel.moreRationalAction)
                    text15.display("others would have likely reacted by")
                    othersReactions.display(viewModel.othersReactions)

                    if (viewModel.otherPeopleRational.value == 1)
                        text16.display("Acting this way instead would likely help produce healthier relationships, even though you noted that others might still respond to you irrationally.")
                    else
                        text16.display("Acting this way instead would likely help produce healthier relationships, especially because the other people involved, according to you, would also respond rationally.")
                }
            }

            text17.display("Keep in mind that this is just a summary—a review of one event in your life.")

        }.root
}
