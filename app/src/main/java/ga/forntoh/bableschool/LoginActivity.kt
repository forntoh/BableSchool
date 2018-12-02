package ga.forntoh.bableschool

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Toast
import ga.forntoh.bableschool.utils.Utils
import io.reactivex.schedulers.Schedulers

class LoginActivity : AppCompatActivity() {

    private var popupWindow: PopupWindow? = null
    private val userName by lazy { findViewById<EditText>(R.id.et_uname) }
    private val password by lazy { findViewById<EditText>(R.id.et_pass) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    @SuppressLint("InflateParams")
    fun login(view: View) {

        val matriculation = userName!!.text.toString()
        val pass = password!!.text.toString()

        if (matriculation.isEmpty() or pass.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        popupWindow = Utils.startPopUpWindow(
                LayoutInflater.from(this).inflate(R.layout.dialog_loading, null),
                findViewById(R.id.parent), null)

        val service = RetrofitBuilder.createService(ApiService::class.java)

        service.getUserProfile(matriculation, pass)
                .subscribeOn(Schedulers.io())
                .subscribe({ (_, classS, _, profileData) ->
                    StorageUtil.getInstance(this).saveMatriculation(profileData!!["Matriculation"])
                    StorageUtil.getInstance(this).savePassword(profileData["Password"])
                    StorageUtil.getInstance(this).saveClass(classS)
                    runOnUiThread {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }, { t ->
                    runOnUiThread {
                        popupWindow!!.dismiss()
                        Toast.makeText(this, "Account does not exist or has been deactivated", Toast.LENGTH_LONG).show()
                        t.printStackTrace()
                    }
                })
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (popupWindow != null) {
            popupWindow!!.dismiss()
            popupWindow = null
        }
    }
}
