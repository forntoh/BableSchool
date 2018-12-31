package ga.forntoh.bableschool

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.Menu
import android.widget.TextView
import com.google.gson.Gson
import ga.forntoh.bableschool.fragments.*
import ga.forntoh.bableschool.model.Category

class CategoryActivity : BaseActivity() {

    private val toolbarTitle by lazy { findViewById<TextView>(R.id.title) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        disableFlags(true)

        val (id, title) = Gson().fromJson(intent.getStringExtra("category"), Category::class.java)
        toolbarTitle.text = title

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        enableWhiteStatusBar()

        when (id.toInt()) {
            1 -> loadFragment(NewsFragment())
            2 -> loadFragment(CourseNotesFragment())
            3 -> loadFragment(RankingFragment())
            4 -> loadFragment(ForumFragment())
            5 -> loadFragment(MyProfileFragment())
        }
    }

    fun loadFragment(fragment: Fragment): FragmentTransaction =
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, fragment)
                commit()
            }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_home, menu)
        return true
    }
}
