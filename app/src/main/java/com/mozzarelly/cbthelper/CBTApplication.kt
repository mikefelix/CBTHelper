package com.mozzarelly.cbthelper

import com.jakewharton.threetenabp.AndroidThreeTen

class CBTApplication : android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}