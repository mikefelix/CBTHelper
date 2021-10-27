@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")

package com.mozzarelly.cbthelper.cogvalid

import android.content.Intent
import com.mozzarelly.cbthelper.*

class CogValidActivity : InterviewActivity<CogValidViewModel>() {

    override val fragmentTypes = listOf(
        CogValidIntroFragment::class,
        CogValid1Fragment::class,
        CogValid2Fragment::class,
        CogValid3Fragment::class,
        CogValid4Fragment::class,
        CogValid5Fragment::class,
        CogValid6Fragment::class,
        CogValid7Fragment::class,
        CogValid8Fragment::class,
        CogValid9Fragment::class,
        CogValid10Fragment::class
    )

    override val viewModel: CogValidViewModel by cbtViewModel()

    override fun CogValidViewModel.setup() {
        numberOfPages = LateInt(fragmentTypes.size)
        load(getIdExtra())
    }

}

