@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")

package com.mozzarelly.cbthelper.replacement

import android.content.Intent
import com.mozzarelly.cbthelper.*

class ReplacementThoughtsActivity : InterviewActivity<ReplacementThoughtsViewModel>() {

    override val fragmentTypes = listOf(
        ReplacementThoughts1Fragment::class,
        ReplacementThoughts2Fragment::class,
        ReplacementThoughts3Fragment::class,
        ReplacementThoughts4Fragment::class,
        ReplacementThoughts5Fragment::class
    )

    override val viewModel: ReplacementThoughtsViewModel by cbtViewModel()

    override fun ReplacementThoughtsViewModel.setup() {
        numberOfPages = LateInt(fragmentTypes.size)
        load(getIdExtra())
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }
}

