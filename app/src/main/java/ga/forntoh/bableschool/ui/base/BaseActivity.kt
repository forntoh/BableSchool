package ga.forntoh.bableschool.ui.base

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import ga.forntoh.bableschool.App
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.notification.MessagingService
import ga.forntoh.bableschool.ui.category.profile.ProfileViewModel
import ga.forntoh.bableschool.ui.category.profile.ProfileViewModelFactory
import ga.forntoh.bableschool.ui.login.LoginActivity
import ga.forntoh.bableschool.utilities.Utils
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

@SuppressLint("VisibleForTests")
open class BaseActivity : ScopedActivity(), KodeinAware {

    override val kodein by lazy { (applicationContext as App).kodein }

    private val viewModelFactory: ProfileViewModelFactory by instance<ProfileViewModelFactory>()
    private lateinit var viewModel: ProfileViewModel

    private var currentApiVersion: Int = 0
    @SuppressLint("InlinedApi")
    private var flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
        currentApiVersion = Build.VERSION.SDK_INT
        val transition = LayoutTransition()
        transition.enableTransitionType(LayoutTransition.CHANGING)
        try {
            (findViewById<View>(android.R.id.content) as ViewGroup).layoutTransition = transition
        } catch (ignored: IllegalStateException) {
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(MessagingService.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
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
                viewModel.logout()
                onResume()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        launch {
            if (viewModel.user() == null) {
                Toast.makeText(this@BaseActivity, "Please login", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@BaseActivity, LoginActivity::class.java))
                finish()
            }
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
