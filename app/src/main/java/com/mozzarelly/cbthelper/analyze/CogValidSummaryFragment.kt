package com.mozzarelly.cbthelper.analyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.mozzarelly.cbthelper.CBTDatabase
import com.mozzarelly.cbthelper.CBTFragment
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.cogvalid.CogValidViewModel
import com.mozzarelly.cbthelper.databinding.ValiditySummaryBinding
import com.mozzarelly.cbthelper.observe
import java.util.*

class CogValidSummaryFragment : CBTFragment() {

    val viewModel: AnalyzeEntryViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        ValiditySummaryBinding.inflate(inflater, container, false).apply {
            viewModel.title.value = "Cognition Validity summary"
//            viewModel.id = getIdExtra()

            observe(viewModel.thinkingErrors) {
                errors.text = it.joinToString(separator = "\n")
            }

            observe(viewModel.emotionsChosen) {
                text3.text = getString(R.string.validitySummary3, it.toLowerCase(Locale.US) ?: "emotions")
            }
        }.root


}
