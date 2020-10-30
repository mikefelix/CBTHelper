package com.mozzarelly.cbthelper.analyze

import android.os.Bundle
import androidx.activity.viewModels
import com.mozzarelly.cbthelper.CBTActivity
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.runTx

class AnalyzeEntryActivity : CBTActivity<AnalyzeEntryViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analyze_entry)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        title = "Analyze entry"

        viewModel.id = (intent.getIntExtra("id", -1).takeIf { it > 0 } ?: error("Need id in intent"))

        supportFragmentManager.runTx {
            IntroFragment().let {
                add(R.id.contentFragment, it)
                show(it)
            }
        }
    }

//    override fun getActivityViewModel() = viewModelProvider.getAndInit<AnalyzeEntryViewModel>()
    override val viewModel: AnalyzeEntryViewModel by viewModels { viewModelProvider }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }

/*
    override fun onBackPressed() {
        finish()
    }
*/

}