package com.mozzarelly.cbthelper.analyze

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.mozzarelly.cbthelper.CBTActivity
import com.mozzarelly.cbthelper.R
import com.mozzarelly.cbthelper.runTx

const val RequestCodeStartCogValidTest = 931

class AnalyzeEntryActivity : CBTActivity<AnalyzeEntryViewModel>() {

    override val layout: Int = R.layout.activity_analyze_entry
    override val viewModel: AnalyzeEntryViewModel by viewModels { viewModelProvider }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Analyze entry"

        viewModel.id = getIdExtra()

        supportFragmentManager.runTx {
            AnalyzeEntryIntroFragment().let {
                add(R.id.contentFragment, it)
                show(it)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == RequestCodeStartCogValidTest && resultCode > 0 -> {
                supportFragmentManager.runTx {
                    replace(R.id.contentFragment, CogValidSummaryFragment())
                }
            }
        }
    }
}