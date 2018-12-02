package ga.forntoh.bableschool

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.transition.Fade
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

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

    fun setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val fade = Fade()
            fade.duration = 1000
            window.enterTransition = fade
            window.exitTransition = fade
        }
    }

    fun enableWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ResourcesCompat.getColor(resources, R.color.bgLightGrey, null)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.logout -> {
                StorageUtil.getInstance(this).deleteMatriculation()
                onResume()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        val matriculation = StorageUtil.getInstance(this).loadMatriculation()
        val pass = StorageUtil.getInstance(this).loadPassword()

        if (matriculation.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}
