package com.mozzarelly.cbthelper

import androidx.lifecycle.ViewModel

abstract class CBTViewModel: ViewModel(){
    abstract val patientGuidePage: PatientGuide.Page?
}