package ga.forntoh.bableschool.ui

import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.main.Category
import ga.forntoh.bableschool.ui.courseNotes.CourseNotesFragment
import ga.forntoh.bableschool.ui.forum.ForumFragment
import ga.forntoh.bableschool.ui.news.NewsFragment
import ga.forntoh.bableschool.ui.profile.MyProfileFragment
import ga.forntoh.bableschool.ui.ranking.RankingFragment
import ga.forntoh.bableschool.utils.enableWhiteStatusBar

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
            0 -> loadFragment(NewsFragment())
            1 -> loadFragment(CourseNotesFragment())
            2 -> loadFragment(RankingFragment())
            3 -> loadFragment(ForumFragment())
            4 -> loadFragment(MyProfileFragment())
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
