package com.mozzarelly.cbthelper.behavior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorAnalogyBinding
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorIntroBinding

class BehaviorAnalogyFragment : BehaviorFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentBehaviorAnalogyBinding.inflate(inflater, container, false).apply {
            buttons.setupButtons()
        }.root
}