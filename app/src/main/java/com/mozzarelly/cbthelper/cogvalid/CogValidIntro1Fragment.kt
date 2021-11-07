package com.mozzarelly.cbthelper.cogvalid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentCogvalIntro1Binding
import com.mozzarelly.cbthelper.makeBulletedList

class CogValidIntro1Fragment : CogValidFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentCogvalIntro1Binding.inflate(inflater, container, false).apply {
            nextButton.setOnClickListener { nextPage() }

            metaphor1.display(R.string.apples_metaphor_1)
            metaphor2.display(getString(R.string.apples_metaphor_2))
            metaphorBullets.display(makeBulletedList(8, requireContext(), R.string.apples_metaphor_bullets_1, R.string.apples_metaphor_bullets_2, R.string.apples_metaphor_bullets_3))
            metaphor3.display(R.string.apples_metaphor_3)
            testInvitation.display(R.string.check_for_apples)

            skipRationalButton.setOnClickListener {
                viewModel.skip(rational = true)
                act.finish(1)
            }

            skipIrrationalButton.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("Are you sure?")
                    .setMessage(R.string.validitySkipConfirm)
                    .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton(R.string.ok) { dialog, _ ->
                        viewModel.skip(rational = false)
                        act.finish(1)
                    }
                    .show()
            }
        }.root

}
