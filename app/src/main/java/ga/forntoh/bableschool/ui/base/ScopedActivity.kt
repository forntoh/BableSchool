package ga.forntoh.bableschool.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ga.forntoh.bableschool.utilities.getLoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class ScopedActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job

    protected val dialog by lazy { getLoadingDialog() }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}