package com.mozzarelly.cbthelper.analyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mozzarelly.cbthelper.*
import com.mozzarelly.cbthelper.behavior.BehaviorActivity
import com.mozzarelly.cbthelper.databinding.FragmentAnalyze2CogvalSummaryBinding
import com.mozzarelly.cbthelper.replacement.ReplacementThoughtsActivity

class AnalyzeCogValidSummaryFragment : CBTFragment() {

    val viewModel: AnalyzeViewModel by activityViewModels()

    override val title = "Summary"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentAnalyze2CogvalSummaryBinding.inflate(inflater, container, false).apply {

            observe(viewModel.thinkingErrors) { errorsMade ->
                val wasValid = errorsMade.isNullOrEmpty()

                errors.showIf(!wasValid) { display(errorsMade?.joinToString("\n") ?: "") }
                text4.showIf(wasValid) { display(getString(R.string.validitySummary4Rational)) }
                readMore.showIf(wasValid) {
                    setOnClickListener {
                        //showExplanationPopup(R.string.negative_emotion_heading, R.string.negative_emotion_explanation)
                        act.showPatientGuide()
                    }
                }

                beginRatRepButton.showIf(!wasValid) {
                    setOnClickListener {
                        start<ReplacementThoughtsActivity>(viewModel.id)
                    }
                }

                beginBehaviorButton.showIf(wasValid) {
                    setOnClickListener {
                        start<BehaviorActivity>(viewModel.id)
                    }
                }

            }

            observe(viewModel.typeString, viewModel.emotionsFelt, viewModel.thinkingErrors) { type, emotionsFelt, errorsMade ->
                if (errorsMade.isNullOrEmpty()) {
                    text1.display(getString(R.string.validitySummary1Rational, type ?: "situation"))
                    judgment.display(getString(R.string.validitySummary3Rational, emotionsFelt?.toSimpleText(), if ((emotionsFelt?.size ?: 0) == 1) "was" else "were"))
                }
                else {
                    text1.display(getString(R.string.validitySummary1Irrational, type))
                    judgment.display(getString(R.string.validitySummary3Irrational))
                }

                content.visibility = View.VISIBLE
            }

/*
                when {
                    wasValid -> {
                        continueRationalButton.visibility = View.GONE
                        beginRatRepButton.run {
                            setText(R.string.negative_emotion_button_text)
                            setOnClickListener {
                                showExplanationPopup(R.string.negative_emotion_heading, R.string.negative_emotion_explanation)
                            }
                        }
                        beginBehaviorButton.run {
                            setOnClickListener {
                                start<BehaviorActivity>(viewModel.id)
                            }
                        }
                    }
                    ratRepState == null -> {
                        continueRationalButton.visibility = View.GONE
                        beginBehaviorButton.visibility = View.GONE
                        beginRatRepButton.run {
                            setText(R.string.begin_replacement_thoughts)
                            setOnClickListener {
                                start<ReplacementThoughtsActivity>(viewModel.id)
                            }
                        }
                    }
                    ratRepState == true -> {
                        continueRationalButton.visibility = View.GONE
                        beginBehaviorButton.visibility = View.GONE
                        beginRationalButton.run {
                            setText(R.string.view_replacement_thoughts)
                            setOnClickListener {
                                act.showFragment<AnalyzeRatRepSummaryFragment>()
                            }
                        }
                    }
                    else -> {
                        continueRationalButton.run {
                            visibility = View.VISIBLE
                            setOnClickListener {
                                start<ReplacementThoughtsActivity>(viewModel.id)
                            }
                        }
                        beginBehaviorButton.visibility = View.GONE
                        beginRationalButton.run {
                            setText(R.string.continue_replacement_thoughts)
                            setOnClickListener {
                                start<ReplacementThoughtsActivity>(viewModel.id)
                            }
                        }
                    }
                }
*/
        }.root

}
