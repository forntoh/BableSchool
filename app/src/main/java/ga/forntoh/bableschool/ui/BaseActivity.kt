package ga.forntoh.bableschool.ui

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.utils.StorageUtil
import ga.forntoh.bableschool.utils.Utils

@SuppressLint("VisibleForTests")
open class BaseActivity : AppCompatActivity() {

    private var currentApiVersion: Int = 0
    @SuppressLint("InlinedApi")
    private var flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentApiVersion = Build.VERSION.SDK_INT
        val transition = LayoutTransition()
        transition.enableTransitionType(LayoutTransition.CHANGING)
        try {
            (findViewById<View>(android.R.id.content) as ViewGroup).layoutTransition = transition
        } catch (ignored: IllegalStateException) {
        }
    }

    fun disableFlags(b: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            this.flags = if (b) -1 else View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
            if (flags != -1) window.decorView.systemUiVisibility = flags
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.logout -> {
                StorageUtil.getInstance(baseContext).deleteMatriculation()
                onResume()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        val matriculation = StorageUtil.getInstance(baseContext).loadMatriculation()
        val pass = StorageUtil.getInstance(baseContext).loadPassword()

        if (matriculation.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        if (Utils.currentPopupWindow != null && Utils.currentPopupWindow!!.isShowing)
            Utils.currentPopupWindow!!.dismiss()
        else super.onBackPressed()
    }

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}
