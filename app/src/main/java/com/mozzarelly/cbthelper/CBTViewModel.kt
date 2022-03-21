package com.mozzarelly.cbthelper

import androidx.lifecycle.ViewModel

abstract class CBTViewModel: ViewModel(){
    abstract fun patientGuidePage(): PatientGuide.Page?
}