package com.mozzarelly.cbthelper.behavior

import com.mozzarelly.cbthelper.LateInt
import com.mozzarelly.cbthelper.InterviewActivity
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.cbtViewModel

class BehaviorActivity : InterviewActivity<BehaviorViewModel>() {

    override val fragmentTypes = listOf(
        BehaviorAnalogyFragment::class,
        BehaviorIntroFragment::class,
        Behavior1AFragment::class,
        Behavior1BFragment::class,
        Behavior1CFragment::class,
        Behavior1DFragment::class,
        Behavior2Fragment::class,
        Behavior3AFragment::class,
        Behavior3BFragment::class,
        Behavior3CFragment::class,
        Behavior3DFragment::class
    )

    override val viewModel: BehaviorViewModel by cbtViewModel()

    override fun BehaviorViewModel.setup() {
        numberOfPages = LateInt(fragmentTypes.size)
        load(getIdExtra())
    }

}
