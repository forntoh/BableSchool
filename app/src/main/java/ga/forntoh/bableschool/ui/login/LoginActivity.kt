package ga.forntoh.bableschool.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.ui.base.ScopedActivity
import ga.forntoh.bableschool.ui.category.CategoriesActivity
import ga.forntoh.bableschool.ui.category.profile.ProfileViewModel
import ga.forntoh.bableschool.ui.category.profile.ProfileViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class LoginActivity : ScopedActivity(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: ProfileViewModelFactory by instance()
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)
    }

    @Suppress("UNUSED_PARAMETER")
    @SuppressLint("InflateParams")
    fun login(view: View) = launch {

        val matriculation = et_uname.text.toString()
        val pass = et_pass.text.toString()

        if (matriculation.isEmpty() or pass.isEmpty()) {
            Toast.makeText(this@LoginActivity, "All fields are required", Toast.LENGTH_SHORT).show()
            return@launch
        }

        dialog.show()
        viewModel.login(matriculation, pass)
        startActivity(Intent(this@LoginActivity, CategoriesActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog.dismiss()
    }
}
