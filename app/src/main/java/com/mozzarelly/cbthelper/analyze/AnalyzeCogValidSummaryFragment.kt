package com.mozzarelly.cbthelper.analyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mozzarelly.cbthelper.*
import com.mozzarelly.cbthelper.behavior.BehaviorActivity
import com.mozzarelly.cbthelper.databinding.FragmentAnalyze2CogvalSummaryBinding
import com.mozzarelly.cbthelper.replacement.ReplacementThoughtsActivity
import java.util.*

class AnalyzeCogValidSummaryFragment : CBTFragment() {

    val viewModel: AnalyzeViewModel by activityViewModels()

    override val title = "Summary"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentAnalyze2CogvalSummaryBinding.inflate(inflater, container, false).apply {
            observe(viewModel.thinkingErrors, viewModel.emotionsChosen, viewModel.ratRepState){ errorsMade, emotions, ratRepState ->
                val wasValid = errorsMade.isNullOrEmpty()
                if (wasValid) {
                    text1.text = getString(R.string.validitySummary1Rational, viewModel.typeString.value)
                    errors.visibility = View.GONE
                    judgment.text = getString(R.string.validitySummary3Rational, emotionTextSimple(emotions))
                    text4.text = getString(R.string.validitySummary4Rational)

                    skipRationalButton.setOnClickListener {
                        start<BehaviorActivity>(viewModel.id)
                    }
                }
                else {
                    text1.text = getString(R.string.validitySummary1Irrational, viewModel.typeString.value)
                    errors.visibility = View.VISIBLE
                    errors.text = errorsMade?.joinToString(separator = "\n") ?: "None"
                    judgment.text = getString(R.string.validitySummary3Irrational)
                    text4.visibility = View.GONE

                    skipRationalButton.setOnClickListener {
                        start<BehaviorActivity>(viewModel.id)
                    }
                }

                when {
                    wasValid -> {
                        continueRationalButton.visibility = View.GONE
                        beginRationalButton.run {
                            text = getString(R.string.negative_emotion_button_text)
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
                        beginRationalButton.run {
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
            }
        }.root

}
