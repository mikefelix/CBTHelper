package com.mozzarelly.cbthelper.analyze

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mozzarelly.cbthelper.CBTFragment
import com.mozzarelly.cbthelper.cogvalid.CogValidActivity
import com.mozzarelly.cbthelper.databinding.FragmentAnalyzeIntroBinding

class IntroFragment : CBTFragment() {

    val viewModel: AnalyzeEntryViewModel by activityViewModels()// { act.viewModelProvider }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentAnalyzeIntroBinding.inflate(inflater, container, false).apply {
            situation.display(viewModel.situation)
            emotions.display(viewModel.emotionsChosen)
            testButton.setOnClickListener {
                startActivity(Intent(requireContext(), CogValidActivity::class.java).putExtra("id", viewModel.id))
            }
        }.root

}