package com.mozzarelly.cbthelper.behavior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.analyze.AnalyzeActivity
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorAnalogyBinding
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorIntroBinding
import com.mozzarelly.cbthelper.makeBulletedList
import com.mozzarelly.cbthelper.showAbout

class BehaviorAnalogyFragment : BehaviorFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentBehaviorAnalogyBinding.inflate(inflater, container, false).apply {
            beginButton.setOnClickListener { nextPage() }
            skipRationalButton.setOnClickListener {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Are you sure?")
                    setMessage(R.string.behaviorSkipAreYouSure)
                    setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    setPositiveButton("Skip") { _, _ ->
                        viewModel.skipTest = true
                        viewModel.changePage(12) // TODO: not hardcode somehow}
                    }
                }.show()
            }

            skipIrrationalButton.setOnClickListener {
                viewModel.changePage(12) // TODO: not hardcode somehow}
            }

            skipInvitation.display("Or you can skip this test because you already know that your behavior was...")
            text1.display(R.string.behaviorAnalogy1)
            text2.display(R.string.behaviorAnalogy2)
            textBullets.display(makeBulletedList(8, requireContext(), R.string.behaviorAnalogyBullet1, R.string.behaviorAnalogyBullet2, R.string.behaviorAnalogyBullet3, R.string.behaviorAnalogyBullet4))
            text3.display(R.string.behaviorAnalogy3)
            text4.display(R.string.behaviorAnalogy4)
            text5.display(R.string.behaviorAnalogy5)
        }.root
}