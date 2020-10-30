package com.mozzarelly.cbthelper

import android.content.Context
import androidx.lifecycle.ViewModel

abstract class CBTViewModel: ViewModel(){
    lateinit var applicationContext: Context

}