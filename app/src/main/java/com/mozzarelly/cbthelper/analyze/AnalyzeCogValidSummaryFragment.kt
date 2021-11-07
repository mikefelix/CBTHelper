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
            observe(viewModel.thinkingErrors, viewModel.emotionsFelt, viewModel.ratRepState) { errorsMade, emotions, ratRepState ->
                val wasValid = errorsMade.isNullOrEmpty()
                observe(viewModel.typeString) {
                    if (wasValid) {
                        text1.display(getString(R.string.validitySummary1Rational, it ?: "situation"))
                        judgment.display(getString(R.string.validitySummary3Rational, emotionTextSimple(emotions)))
                    }
                    else {
                        text1.display(getString(R.string.validitySummary1Irrational, it))
                        judgment.display(getString(R.string.validitySummary3Irrational))
                    }
                }

                errors.showIf(wasValid)
                text4.showIf(wasValid) { display(getString(R.string.validitySummary4Rational)) }
                readMore.showIf(wasValid) {
                    setOnClickListener {
                        showExplanationPopup(R.string.negative_emotion_heading, R.string.negative_emotion_explanation)
                    }
                }

                beginRatRepButton.run {
                    setText(R.string.begin_replacement_thoughts)
                    setOnClickListener {
                        start<ReplacementThoughtsActivity>(viewModel.id)
                    }
                }

                skipToBehaviorButton.setOnClickListener {
                    start<BehaviorActivity>(viewModel.id)
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
            }
        }.root

}
