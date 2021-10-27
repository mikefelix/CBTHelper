package com.mozzarelly.cbthelper.behavior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorAnalogyBinding
import com.mozzarelly.cbthelper.databinding.FragmentBehaviorIntroBinding
import com.mozzarelly.cbthelper.makeBulletedList

class BehaviorAnalogyFragment : BehaviorFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentBehaviorAnalogyBinding.inflate(inflater, container, false).apply {
            beginButton.setOnClickListener { nextPage() }
            text1.display(R.string.behaviorAnalogy1)
            text2.display(R.string.behaviorAnalogy2)
            textBullets.display(makeBulletedList(8, requireContext(), R.string.behaviorAnalogyBullet1, R.string.behaviorAnalogyBullet2, R.string.behaviorAnalogyBullet3, R.string.behaviorAnalogyBullet4))
            text3.display(R.string.behaviorAnalogy3)
            text4.display(R.string.behaviorAnalogy4)
            text5.display(R.string.behaviorAnalogy5)
        }.root
}