package ga.forntoh.bableschool.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.ApiService
import ga.forntoh.bableschool.utils.StorageUtil
import ga.forntoh.bableschool.utils.Utils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LoginActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var popupWindow: PopupWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    @SuppressLint("InflateParams", "CheckResult")
    fun login(view: View) {

        val matriculation = et_uname.text.toString()
        val pass = et_pass.text.toString()

        if (matriculation.isEmpty() or pass.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        popupWindow = Utils.startPopUpWindow(
                LayoutInflater.from(this).inflate(R.layout.dialog_loading, null),
                findViewById(R.id.parent), null)

        val apiService = ApiService()

        launch {
            val (_, classS, _, profileData) = apiService.getUserProfile(matriculation, pass).await()
            StorageUtil.getInstance(baseContext).apply {
                saveMatriculation(profileData!!["Matriculation"])
                savePassword(profileData["Password"])
                saveClass(classS)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (popupWindow != null) {
            popupWindow!!.dismiss()
            popupWindow = null
        }
    }
}
