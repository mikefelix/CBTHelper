@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")

package com.mozzarelly.cbthelper.cogvalid

import android.content.Intent
import androidx.activity.viewModels
import com.mozzarelly.cbthelper.*

class CogValidActivity : PagingActivity<CogValidViewModel>() {

//    val id by extra<String>("entryId")

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
        CogValid10Fragment::class,
        CogValid11Fragment::class,
        CogValid12Fragment::class
    )

    override val layout = R.layout.activity_cogvalid_test

    override val viewModel: CogValidViewModel by viewModels { viewModelProvider }

    override fun CogValidViewModel.setup() {
        numberOfPages = LateInt(fragmentTypes.size)
        load(getIdExtra())
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }
}

